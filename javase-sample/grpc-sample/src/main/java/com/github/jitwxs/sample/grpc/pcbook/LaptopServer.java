package com.github.jitwxs.sample.grpc.pcbook;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.ClientAuth;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.github.jitwxs.sample.grpc.common.Constant.RESOURCE_BATH_PATH;
import static com.github.jitwxs.sample.grpc.common.Constant.RUNNING_PORT;

@Slf4j
public class LaptopServer {
    private final int port;
    private final Server server;

    public LaptopServer(int port, LaptopStore laptopStore, ImageStore imageStore, RatingStore ratingStore, SslContext sslContext) {
        this(sslContext != null ? NettyServerBuilder.forPort(port).sslContext(sslContext) : NettyServerBuilder.forPort(port),
                port, laptopStore, imageStore, ratingStore);
    }

    public LaptopServer(ServerBuilder<?> serverBuilder, int port, LaptopStore laptopStore, ImageStore imageStore, RatingStore ratingStore) {
        this.port = port;
        this.server = serverBuilder
                .addService(new LaptopService(laptopStore, imageStore, ratingStore))
                .build();
    }

    public void start() throws IOException {
        server.start();
        log.info("server started on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("shut down gRPC server because JVM shuts down");
            try {
                LaptopServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("server shut down");
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static SslContext loadTLSCredentials() throws SSLException {
        File serverCertFile = new File(RESOURCE_BATH_PATH + "cert/server-cert.pem");
        File serverKeyFile = new File(RESOURCE_BATH_PATH + "cert/server-key.pem");
        File clientCACertFile = new File(RESOURCE_BATH_PATH + "cert/ca-cert.pem");

        if (serverCertFile.exists() && serverKeyFile.exists() && clientCACertFile.exists()) {
            return GrpcSslContexts
                    .configure(SslContextBuilder.forServer(serverCertFile, serverKeyFile)
                            .clientAuth(ClientAuth.REQUIRE)
                            .trustManager(clientCACertFile))
                    .build();
        } else {
            log.info("LaptopServer loadTLSCredentials failed, not exist cert file");
            return null;
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        InMemoryLaptopStore laptopStore = new InMemoryLaptopStore();
        DiskImageStore imageStore = new DiskImageStore(RESOURCE_BATH_PATH);
        InMemoryRatingStore ratingStore = new InMemoryRatingStore();

        SslContext sslContext = LaptopServer.loadTLSCredentials();

        final LaptopServer server = new LaptopServer(RUNNING_PORT, laptopStore, imageStore, ratingStore, sslContext);

        server.start();
        server.blockUntilShutdown();
    }
}
