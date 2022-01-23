package com.github.jitwxs.sample.grpc.example1;

import com.github.jitwxs.sample.grpc.common.ProtobufUtils;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import com.github.jitwxs.sample.grpc.common.Constant;
import com.github.jitwxs.sample.grpc.UserRpcProto;
import com.github.jitwxs.sample.grpc.UserRpcServiceGrpc;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Grpc 客户端
 * @author jitwxs
 * @date 2019年12月20日 1:06
 */
@Slf4j
public class Example1Client {
    public static void main(String[] args) throws Exception {
        // STEP1 构造 Channel 和 BlockingStub
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", Constant.RUNNING_PORT)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid needing certificates.
                .usePlaintext()
                .build();

        UserRpcServiceGrpc.UserRpcServiceBlockingStub blockingStub = UserRpcServiceGrpc.newBlockingStub(channel);

        int requestAge = 20;
        log.info("Will try to query age = " + requestAge + " ...");

        // STEP2 发起 gRPC 请求
        UserRpcProto.AgeRequest request = UserRpcProto.AgeRequest.newBuilder().setAge(20).build();
        try {
            UserRpcProto.UserResponse response = blockingStub.listByAge(request);
            log.info("Response: " + ProtobufUtils.toJson(response));
        } catch (StatusRuntimeException e) {
            log.error("RPC failed: {}", e.getStatus());
        } finally {
            // STEP3 关闭 Channel
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
