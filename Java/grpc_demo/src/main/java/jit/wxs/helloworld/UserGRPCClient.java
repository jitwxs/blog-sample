package jit.wxs.helloworld;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import jit.wxs.demo.rpc.UserRPCProto;
import jit.wxs.demo.rpc.UserRPCServiceGrpc;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Grpc 客户端
 * @author jitwxs
 * @date 2019年12月20日 1:06
 */
public class UserGRPCClient {

    private static final Logger logger = Logger.getLogger(UserGRPCClient.class.getName());

    private final ManagedChannel channel;
    private final UserRPCServiceGrpc.UserRPCServiceBlockingStub blockingStub;

    public static void main(String[] args) throws Exception {
        UserGRPCClient client = new UserGRPCClient("localhost", UserGRPCServer.PORT);
        try {
            client.request(26);
        } finally {
            client.shutdown();
        }
    }

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public UserGRPCClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid needing certificates.
                .usePlaintext()
                .build());
    }

    /** Construct client for accessing HelloWorld server using the existing channel. */
    public UserGRPCClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = UserRPCServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /** Send Request */
    public void request(int age) {
        logger.info("Will try to query age = " + age + " ...");
        UserRPCProto.Request request = UserRPCProto.Request.newBuilder().setAge(age).build();
        UserRPCProto.Response response;
        try {
            response = blockingStub.listByAge(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Response: " + response.toString());
    }
}
