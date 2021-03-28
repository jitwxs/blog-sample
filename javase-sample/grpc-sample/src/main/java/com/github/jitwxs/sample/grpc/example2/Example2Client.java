package com.github.jitwxs.sample.grpc.example2;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import com.github.jitwxs.sample.grpc.common.Constant;
import com.github.jitwxs.sample.grpc.common.ProtoUtils;
import com.github.jitwxs.sample.grpc.UserRpcProto;
import com.github.jitwxs.sample.grpc.UserRpcServiceGrpc;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Grpc 客户端
 * @author jitwxs
 * @date 2019年12月20日 1:06
 */
public class Example2Client {

    private static final Logger logger = Logger.getLogger(Example2Client.class.getName());

    public static void main(String[] args) throws Exception {
        // STEP1 构造 Channel 和 BlockingStub
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", Constant.RUNNING_PORT)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid needing certificates.
                .usePlaintext()
                .build();

        UserRpcServiceGrpc.UserRpcServiceBlockingStub blockingStub = UserRpcServiceGrpc.newBlockingStub(channel);

        int requestAge = 20;
        logger.info("Will try to query age = " + requestAge + " ...");

        // STEP2 发起 gRPC 请求
        UserRpcProto.AgeRequest request = UserRpcProto.AgeRequest.newBuilder().setAge(20).build();
        try {
            Iterator<UserRpcProto.UserResponse> iterator = blockingStub.listByAgeStream(request);
            while (iterator.hasNext()) {
                UserRpcProto.UserResponse response = iterator.next();
                logger.info("Response: " + ProtoUtils.toStr(response));
            }
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
        } finally {
            // STEP3 关闭 Channel
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
