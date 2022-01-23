package com.github.jitwxs.sample.grpc.example4;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import com.github.jitwxs.sample.grpc.common.Constant;
import com.github.jitwxs.sample.grpc.common.UserRpcServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author jitwxs
 * @date 2020年04月05日 19:42
 */
@Slf4j
public class Example4Server {
    private Server server;

    public static void main(String[] args) throws IOException, InterruptedException {
        final Example4Server server = new Example4Server();
        server.start();
        server.blockUntilShutdown();
    }

    private void start() throws IOException {
        server = ServerBuilder.forPort(Constant.RUNNING_PORT)
                .addService(new UserRpcServiceImpl())
                .build()
                .start();
        log.info("Server started...");

        // 程序停止钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            log.error("*** shutting down gRPC server since JVM is shutting down");
            try {
                this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            log.error("*** server shut down");
        }));
    }

    /**
     * 停止服务
     */
    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
