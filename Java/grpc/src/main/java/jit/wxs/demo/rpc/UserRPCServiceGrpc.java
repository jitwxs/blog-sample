package jit.wxs.demo.rpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.26.0)",
    comments = "Source: UserRPC.proto")
public final class UserRPCServiceGrpc {

  private UserRPCServiceGrpc() {}

  public static final String SERVICE_NAME = "UserRPCService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<UserRPCProto.Request,
      UserRPCProto.Response> getListByAgeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "listByAge",
      requestType = UserRPCProto.Request.class,
      responseType = UserRPCProto.Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<UserRPCProto.Request,
      UserRPCProto.Response> getListByAgeMethod() {
    io.grpc.MethodDescriptor<UserRPCProto.Request, UserRPCProto.Response> getListByAgeMethod;
    if ((getListByAgeMethod = UserRPCServiceGrpc.getListByAgeMethod) == null) {
      synchronized (UserRPCServiceGrpc.class) {
        if ((getListByAgeMethod = UserRPCServiceGrpc.getListByAgeMethod) == null) {
          UserRPCServiceGrpc.getListByAgeMethod = getListByAgeMethod =
              io.grpc.MethodDescriptor.<UserRPCProto.Request, UserRPCProto.Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "listByAge"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  UserRPCProto.Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  UserRPCProto.Response.getDefaultInstance()))
              .setSchemaDescriptor(new UserRPCServiceMethodDescriptorSupplier("listByAge"))
              .build();
        }
      }
    }
    return getListByAgeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UserRPCServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserRPCServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserRPCServiceStub>() {
        @java.lang.Override
        public UserRPCServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserRPCServiceStub(channel, callOptions);
        }
      };
    return UserRPCServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UserRPCServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserRPCServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserRPCServiceBlockingStub>() {
        @java.lang.Override
        public UserRPCServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserRPCServiceBlockingStub(channel, callOptions);
        }
      };
    return UserRPCServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UserRPCServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserRPCServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserRPCServiceFutureStub>() {
        @java.lang.Override
        public UserRPCServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserRPCServiceFutureStub(channel, callOptions);
        }
      };
    return UserRPCServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class UserRPCServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void listByAge(UserRPCProto.Request request,
                          io.grpc.stub.StreamObserver<UserRPCProto.Response> responseObserver) {
      asyncUnimplementedUnaryCall(getListByAgeMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getListByAgeMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                UserRPCProto.Request,
                UserRPCProto.Response>(
                  this, METHODID_LIST_BY_AGE)))
          .build();
    }
  }

  /**
   */
  public static final class UserRPCServiceStub extends io.grpc.stub.AbstractAsyncStub<UserRPCServiceStub> {
    private UserRPCServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserRPCServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserRPCServiceStub(channel, callOptions);
    }

    /**
     */
    public void listByAge(UserRPCProto.Request request,
                          io.grpc.stub.StreamObserver<UserRPCProto.Response> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListByAgeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class UserRPCServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<UserRPCServiceBlockingStub> {
    private UserRPCServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserRPCServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserRPCServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public UserRPCProto.Response listByAge(UserRPCProto.Request request) {
      return blockingUnaryCall(
          getChannel(), getListByAgeMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class UserRPCServiceFutureStub extends io.grpc.stub.AbstractFutureStub<UserRPCServiceFutureStub> {
    private UserRPCServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserRPCServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserRPCServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<UserRPCProto.Response> listByAge(
        UserRPCProto.Request request) {
      return futureUnaryCall(
          getChannel().newCall(getListByAgeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LIST_BY_AGE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final UserRPCServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(UserRPCServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LIST_BY_AGE:
          serviceImpl.listByAge((UserRPCProto.Request) request,
              (io.grpc.stub.StreamObserver<UserRPCProto.Response>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class UserRPCServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UserRPCServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return UserRPCProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UserRPCService");
    }
  }

  private static final class UserRPCServiceFileDescriptorSupplier
      extends UserRPCServiceBaseDescriptorSupplier {
    UserRPCServiceFileDescriptorSupplier() {}
  }

  private static final class UserRPCServiceMethodDescriptorSupplier
      extends UserRPCServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    UserRPCServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (UserRPCServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UserRPCServiceFileDescriptorSupplier())
              .addMethod(getListByAgeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
