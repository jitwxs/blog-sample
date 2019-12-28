package jit.wxs.demo.rpc.impl;


import io.grpc.stub.StreamObserver;
import jit.wxs.demo.dto.UserProto;
import jit.wxs.demo.enums.SexEnumProto;
import jit.wxs.demo.rpc.UserRPCProto;
import jit.wxs.demo.rpc.UserRPCServiceGrpc;

/**
 * @author jitwxs
 * @date 2019年12月20日 0:53
 */
public class UserRPCServiceImpl extends UserRPCServiceGrpc.UserRPCServiceImplBase {
    @Override
    public void listByAge(UserRPCProto.Request request, StreamObserver<UserRPCProto.Response> responseObserver) {
        System.out.println("Server receive request.");

        // 模拟业务逻辑
        UserRPCProto.Response response = UserRPCProto.Response.newBuilder()
                .setCode(0)
                .setMsg("success")
                .addUser(UserProto.User.newBuilder()
                        .setName("wangwu")
                        .setAge(request.getAge())
                        .setSex(SexEnumProto.SexEnum.MALE))
                .addUser(UserProto.User.newBuilder()
                        .setName("limei")
                        .setAge(request.getAge())
                        .setSex(SexEnumProto.SexEnum.FEMALE))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
