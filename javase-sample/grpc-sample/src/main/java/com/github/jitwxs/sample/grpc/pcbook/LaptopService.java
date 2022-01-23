package com.github.jitwxs.sample.grpc.pcbook;

import com.github.jitwxs.sample.protobuf.grpc.pcbook.*;
import com.google.protobuf.ByteString;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LaptopService extends LaptopServiceGrpc.LaptopServiceImplBase {

    private LaptopStore laptopStore;
    private ImageStore imageStore;
    private RatingStore ratingStore;

    public LaptopService(LaptopStore laptopStore, ImageStore imageStore, RatingStore ratingStore) {
        this.laptopStore = laptopStore;
        this.imageStore = imageStore;
        this.ratingStore = ratingStore;
    }

    @Override
    public void createLaptop(CreateLaptopRequest request, StreamObserver<CreateLaptopResponse> responseObserver) {
        Laptop laptop = request.getLaptop();

        String id = laptop.getId();
        log.info("got a create-laptop request with ID: " + id);

        UUID uuid;
        if (id.isEmpty()) {
            uuid = UUID.randomUUID();
        } else {
            try {
                uuid = UUID.fromString(id);
            } catch (IllegalArgumentException e) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription(e.getMessage())
                                .asRuntimeException()
                );
                return;
            }
        }

//        // heavy processing
//        try {
//            TimeUnit.SECONDS.sleep(6);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        if (Context.current().isCancelled()) {
            log.info("request is cancelled");
            responseObserver.onError(
                    Status.CANCELLED
                            .withDescription("request is cancelled")
                            .asRuntimeException()
            );
            return;
        }

        Laptop other = laptop.toBuilder().setId(uuid.toString()).build();
        try {
            laptopStore.Save(other);
        } catch (AlreadyExistsException e) {
            responseObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
            return;
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
            return;
        }

        CreateLaptopResponse response = CreateLaptopResponse.newBuilder().setId(other.getId()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("saved laptop with ID: " + other.getId());
    }

    @Override
    public void searchLaptop(SearchLaptopRequest request, StreamObserver<SearchLaptopResponse> responseObserver) {
        Filter filter = request.getFilter();
        log.info("got a search-laptop request with filter:\n" + filter);

        laptopStore.Search(Context.current(), filter, new LaptopStream() {
            @Override
            public void Send(Laptop laptop) {
                log.info("found laptop with ID: " + laptop.getId());
                SearchLaptopResponse response = SearchLaptopResponse.newBuilder().setLaptop(laptop).build();
                responseObserver.onNext(response);
            }
        });

        responseObserver.onCompleted();
        log.info("search laptop completed");
    }

    @Override
    public StreamObserver<UploadImageRequest> uploadImage(StreamObserver<UploadImageResponse> responseObserver) {
        return new StreamObserver<UploadImageRequest>() {
            private static final int maxImageSize = 1 << 20; // 1 megabyte
            private String laptopID;
            private String imageType;
            private ByteArrayOutputStream imageData;

            @Override
            public void onNext(UploadImageRequest request) {
                if (request.getDataCase() == UploadImageRequest.DataCase.INFO) {
                    ImageInfo info = request.getInfo();
                    log.info("receive image info:\n" + info);

                    laptopID = info.getLaptopId();
                    imageType = info.getImageType();
                    imageData = new ByteArrayOutputStream();

                    // Check laptop exists
                    Laptop found = laptopStore.Find(laptopID);
                    if (found == null) {
                        responseObserver.onError(
                                Status.NOT_FOUND
                                .withDescription("laptop ID doesn't exist")
                                .asRuntimeException()
                        );
                    }

                    return;
                }

                ByteString chunkData = request.getChunkData();
                log.info("receive image chunk with size: " + chunkData.size());

                if (imageData == null) {
                    log.info("image info wasn't sent before");
                    responseObserver.onError(
                            Status.INVALID_ARGUMENT
                            .withDescription("image info wasn't sent before")
                            .asRuntimeException()
                    );
                    return;
                }

                int size = imageData.size() + chunkData.size();
                if (size > maxImageSize) {
                    log.info("image is too large: " + size);
                    responseObserver.onError(
                            Status.INVALID_ARGUMENT
                            .withDescription("image is too large: " + size)
                            .asRuntimeException()
                    );
                    return;
                }

                try {
                    chunkData.writeTo(imageData);
                } catch (IOException e) {
                    responseObserver.onError(
                            Status.INTERNAL
                            .withDescription("cannot write chunk data: " + e.getMessage())
                            .asRuntimeException()
                    );
                    return;
                }
            }

            @Override
            public void onError(Throwable t) {
                log.warn(t.getMessage());
            }

            @Override
            public void onCompleted() {
                String imageID = "";
                int imageSize = imageData.size();

                try {
                    imageID = imageStore.Save(laptopID, imageType, imageData);
                } catch (IOException e) {
                    responseObserver.onError(
                            Status.INTERNAL
                            .withDescription("cannot save image to the store: " + e.getMessage())
                            .asRuntimeException()
                    );
                    return;
                }

                UploadImageResponse response = UploadImageResponse.newBuilder()
                        .setId(imageID)
                        .setSize(imageSize)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<RateLaptopRequest> rateLaptop(StreamObserver<RateLaptopResponse> responseObserver) {
        return new StreamObserver<RateLaptopRequest>() {
            @Override
            public void onNext(RateLaptopRequest request) {
                String laptopId = request.getLaptopId();
                double score = request.getScore();

                log.info("received rate-laptop request: id = " + laptopId + ", score = " + score);

                Laptop found = laptopStore.Find(laptopId);
                if (found == null) {
                    responseObserver.onError(
                            Status.NOT_FOUND
                            .withDescription("laptop ID doesn't exist")
                            .asRuntimeException()
                    );
                    return;
                }

                Rating rating = ratingStore.Add(laptopId, score);
                RateLaptopResponse response = RateLaptopResponse.newBuilder()
                        .setLaptopId(laptopId)
                        .setRatedCount(rating.getCount())
                        .setAverageScore(rating.getSum() / rating.getCount())
                        .build();

                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                log.warn(t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
