package com.github.jitwxs.sample.grpc.common;

import io.grpc.stub.StreamObserver;
import com.github.jitwxs.sample.grpc.MessageProto;
import com.github.jitwxs.sample.grpc.UserRpcProto;
import com.github.jitwxs.sample.grpc.UserRpcServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author jitwxs
 * @date 2019年12月20日 0:53
 */
@Slf4j
public class UserRpcServiceImpl extends UserRpcServiceGrpc.UserRpcServiceImplBase {
    @Override
    public void listByAge(UserRpcProto.AgeRequest request, StreamObserver<UserRpcProto.UserResponse> responseObserver) {
        log.info("Server Rec listByAge request...");

        // 构造响应，模拟业务逻辑
        UserRpcProto.UserResponse response = UserRpcProto.UserResponse.newBuilder()
                .setCode(0)
                .setMsg("success")
                .addUser(MessageProto.User.newBuilder()
                        .setName(RandomStringUtils.randomAlphabetic(5))
                        .setAge(request.getAge()).build())
                .addUser(MessageProto.User.newBuilder()
                        .setName(RandomStringUtils.randomAlphabetic(5))
                        .setAge(request.getAge()).build())
                .addUser(MessageProto.User.newBuilder()
                        .setName(RandomStringUtils.randomAlphabetic(5))
                        .setAge(request.getAge()).build())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listByAgeStream(UserRpcProto.AgeRequest request, StreamObserver<UserRpcProto.UserResponse> responseObserver) {
        log.info("Server Rec listByAgeStream request...");

        // 构造响应，模拟业务逻辑
        for(int i = 0; i < 10; i++) {
            List<MessageProto.User> userList = new ArrayList<>(6);
            IntStream.range(0, RandomUtils.nextInt(1, 5)).forEach(e -> {
                userList.add(MessageProto.User.newBuilder()
                        .setName(RandomStringUtils.randomAlphabetic(5))
                        .setAge(request.getAge()).build());
            });

            UserRpcProto.UserResponse response = UserRpcProto.UserResponse.newBuilder()
                    .setCode(0)
                    .setMsg("success")
                    .addAllUser(userList)
                    .build();
            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<UserRpcProto.AgeRequest> streamListByAge(StreamObserver<UserRpcProto.UserListResponse> responseObserver) {
        // 单线程，暂不考虑线程安全问题
        List<UserRpcProto.AgeRequest> allClientRequest = new ArrayList<>();

        // 当接收到客户端请求时的回调处理
        return new StreamObserver<UserRpcProto.AgeRequest>() {
            @Override
            public void onNext(UserRpcProto.AgeRequest ageRequest) {
                allClientRequest.add(ageRequest);
                log.info("Server Rec streamListByAge request, age: " + ageRequest.getAge());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            /**
             * 客户端请求全部发送完毕
             */
            @Override
            public void onCompleted() {
                // 根据每次请求，构造出所有请求的请求响应
                List<UserRpcProto.UserResponse> responseList = new ArrayList<>(allClientRequest.size());
                allClientRequest.forEach(request -> {
                    List<MessageProto.User> userList = new ArrayList<>(6);
                    IntStream.range(0, RandomUtils.nextInt(1, 5)).forEach(e -> {
                        userList.add(MessageProto.User.newBuilder()
                                .setName(RandomStringUtils.randomAlphabetic(5))
                                .setAge(request.getAge()).build());
                    });

                    responseList.add(UserRpcProto.UserResponse.newBuilder()
                            .setCode(0)
                            .setMsg("success")
                            .addAllUser(userList)
                            .build());
                });

                UserRpcProto.UserListResponse response = UserRpcProto.UserListResponse.newBuilder().addAllResponse(responseList).build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<UserRpcProto.AgeRequest> streamListByAgeStream(StreamObserver<UserRpcProto.UserResponse> responseObserver) {
        // 当接收到客户端请求时的回调处理
        return new StreamObserver<UserRpcProto.AgeRequest>() {
            @Override
            public void onNext(UserRpcProto.AgeRequest ageRequest) {
                log.info("Server Rec streamListByAgeStream request, age: " + ageRequest.getAge());
                List<MessageProto.User> userList = new ArrayList<>(6);
                IntStream.range(0, RandomUtils.nextInt(1, 5)).forEach(e -> {
                    userList.add(MessageProto.User.newBuilder()
                            .setName(RandomStringUtils.randomAlphabetic(5))
                            .setAge(ageRequest.getAge()).build());
                });

                responseObserver.onNext(UserRpcProto.UserResponse.newBuilder()
                        .setCode(0)
                        .setMsg("success")
                        .addAllUser(userList)
                        .build());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            /**
             * 客户端请求全部发送完毕
             */
            @Override
            public void onCompleted() {
                log.info("Server Rec streamListByAgeStream complete");
                responseObserver.onCompleted();
            }
        };
    }
}
