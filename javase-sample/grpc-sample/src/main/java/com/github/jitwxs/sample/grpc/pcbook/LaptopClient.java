package com.github.jitwxs.sample.grpc.pcbook;

import com.github.jitwxs.sample.protobuf.grpc.pcbook.*;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.github.jitwxs.sample.grpc.common.Constant.RESOURCE_BATH_PATH;
import static com.github.jitwxs.sample.grpc.common.Constant.RUNNING_PORT;

@Slf4j
public class LaptopClient {
    private final ManagedChannel channel;
    private final LaptopServiceGrpc.LaptopServiceBlockingStub blockingStub;
    private final LaptopServiceGrpc.LaptopServiceStub asyncStub;

    /**
     * @param sslContext SSL 配置，未启用传空
     */
    public LaptopClient(String host, int port, SslContext sslContext) {
        final NettyChannelBuilder builder = NettyChannelBuilder.forAddress(host, port);

        if (sslContext == null) {
            builder.usePlaintext();
        } else {
            builder.sslContext(sslContext);
        }

        channel = builder.build();

        blockingStub = LaptopServiceGrpc.newBlockingStub(channel);
        asyncStub = LaptopServiceGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void createLaptop(Laptop laptop) {
        CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();
        CreateLaptopResponse response;

        try {
            response = blockingStub.withDeadlineAfter(5, TimeUnit.SECONDS).createLaptop(request);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.ALREADY_EXISTS) {
                // not a big deal
                log.info("laptop ID already exists");
                return;
            }
            log.error("request failed: " + e.getMessage());
            return;
        } catch (Exception e) {
            log.error("request failed: " + e.getMessage());
            return;
        }

        log.info("laptop created with ID: " + response.getId());
    }

    public void searchLaptop(Filter filter) {
        log.info("search started");

        SearchLaptopRequest request = SearchLaptopRequest.newBuilder().setFilter(filter).build();

        try {
            Iterator<SearchLaptopResponse> iterator = blockingStub
                    .withDeadlineAfter(5, TimeUnit.SECONDS)
                    .searchLaptop(request);

            while (iterator.hasNext()) {
                SearchLaptopResponse response = iterator.next();
                Laptop laptop = response.getLaptop();
                log.info("- found: " + laptop.getId());
            }
        } catch (Exception e) {
            log.error("request failed: " + e.getMessage());
            return;
        }

        log.info("search completed");
    }

    public void uploadImage(String laptopID, String imagePath) throws InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);

        StreamObserver<UploadImageRequest> requestObserver = asyncStub.withDeadlineAfter(5, TimeUnit.SECONDS)
                .uploadImage(new StreamObserver<UploadImageResponse>() {
                    @Override
                    public void onNext(UploadImageResponse response) {
                        log.info("receive response:\n" + response);
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.error("upload failed: " + t);
                        finishLatch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        log.info("image uploaded");
                        finishLatch.countDown();
                    }
                });

        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(imagePath);
        } catch (FileNotFoundException e) {
            log.error("cannot read image file: " + e.getMessage());
            return;
        }

        String imageType = imagePath.substring(imagePath.lastIndexOf("."));
        ImageInfo info = ImageInfo.newBuilder().setLaptopId(laptopID).setImageType(imageType).build();
        UploadImageRequest request = UploadImageRequest.newBuilder().setInfo(info).build();

        try {
            requestObserver.onNext(request);
            log.info("sent image info:\n" + info);

            byte[] buffer = new byte[1024];
            while (true) {
                int n = fileInputStream.read(buffer);
                if (n <= 0) {
                    break;
                }

                if (finishLatch.getCount() == 0) {
                    return;
                }

                request = UploadImageRequest.newBuilder()
                        .setChunkData(ByteString.copyFrom(buffer, 0, n))
                        .build();
                requestObserver.onNext(request);
                log.info("sent image chunk with size: " + n);
            }
        } catch (Exception e) {
            log.error("unexpected error: " + e.getMessage());
            requestObserver.onError(e);
            return;
        }

        requestObserver.onCompleted();

        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            log.warn("request cannot finish within 1 minute");
        }
    }

    public void rateLaptop(String[] laptopIDs, double[] scores) throws InterruptedException {
        CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<RateLaptopRequest> requestObserver = asyncStub.withDeadlineAfter(5, TimeUnit.SECONDS)
                .rateLaptop(new StreamObserver<RateLaptopResponse>() {
                    @Override
                    public void onNext(RateLaptopResponse response) {
                        log.info("laptop rated: id = " + response.getLaptopId() +
                                ", count = " + response.getRatedCount() +
                                ", average = " + response.getAverageScore());
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.error("rate laptop failed: " + t.getMessage());
                        finishLatch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        log.info("rate laptop completed");
                        finishLatch.countDown();
                    }
                });

        int n = laptopIDs.length;
        try {
            for (int i = 0; i < n; i++) {
                RateLaptopRequest request = RateLaptopRequest.newBuilder()
                        .setLaptopId(laptopIDs[i])
                        .setScore(scores[i])
                        .build();
                requestObserver.onNext(request);
                log.info("sent rate-laptop request: id = " + request.getLaptopId() + ", score = " + request.getScore());
            }
        } catch (Exception e) {
            log.error("unexpected error: " + e.getMessage());
            requestObserver.onError(e);
            return;
        }

        requestObserver.onCompleted();
        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            log.warn("request cannot finish within 1 minute");
        }
    }

    public static void testCreateLaptop(LaptopClient client, Generator generator) {
        Laptop laptop = generator.NewLaptop();
        client.createLaptop(laptop);
    }

    public static void testSearchLaptop(LaptopClient client, Generator generator) {
        for (int i = 0; i < 10; i++) {
            Laptop laptop = generator.NewLaptop();
            client.createLaptop(laptop);
        }

        Memory minRam = Memory.newBuilder()
                .setValue(8)
                .setUnit(Memory.Unit.GIGABYTE)
                .build();
        Filter filter = Filter.newBuilder()
                .setMaxPriceUsd(3000)
                .setMinCpuCores(4)
                .setMinCpuGhz(2.5)
                .setMinRam(minRam)
                .build();
        client.searchLaptop(filter);
    }

    public static void testUploadImage(LaptopClient client, Generator generator) throws InterruptedException {
        Laptop laptop = generator.NewLaptop();
        client.createLaptop(laptop);
        client.uploadImage(laptop.getId(), RESOURCE_BATH_PATH + "laptop.jpg");
    }

    public static void testRateLaptop(LaptopClient client, Generator generator) throws InterruptedException {
        int n = 3;
        String[] laptopIDs = new String[n];

        for (int i = 0; i < n; i++) {
            Laptop laptop = generator.NewLaptop();
            laptopIDs[i] = laptop.getId();
            client.createLaptop(laptop);
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            log.info("rate laptop (y/n)? ");
            String answer = scanner.nextLine();
            if (answer.toLowerCase().trim().equals("n")) {
                break;
            }

            double[] scores = new double[n];
            for (int i = 0; i < n; i++) {
                scores[i] = generator.NewLaptopScore();
            }

            client.rateLaptop(laptopIDs, scores);
        }
    }

    public static SslContext loadTLSCredentials() throws SSLException {
        File serverCACertFile = new File(RESOURCE_BATH_PATH + "cert/ca-cert.pem");
        File clientCertFile = new File(RESOURCE_BATH_PATH + "cert/client-cert.pem");
        File clientKeyFile = new File(RESOURCE_BATH_PATH + "cert/client-key.pem");

        if (serverCACertFile.exists() && clientCertFile.exists() && clientKeyFile.exists()) {
            return GrpcSslContexts.forClient()
                    .keyManager(clientCertFile, clientKeyFile)
                    .trustManager(serverCACertFile)
                    .build();
        } else {
            log.info("LaptopClient loadTLSCredentials failed, not exist cert file");
            return null;
        }
    }

    public static void main(String[] args) throws InterruptedException, SSLException {
        final SslContext sslContext = LaptopClient.loadTLSCredentials();

        final LaptopClient client = new LaptopClient("localhost", RUNNING_PORT, sslContext);

        Generator generator = new Generator();

        try {
//            testCreateLaptop(client, generator);
//            testSearchLaptop(client, generator);
            testUploadImage(client, generator);
//            testRateLaptop(client, generator);
        } finally {
            client.shutdown();
        }
    }
}