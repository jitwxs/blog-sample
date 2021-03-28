package com.github.jitwxs.sample.protobuf;

import com.github.jitwxs.sample.protobuf.EnumProto;
import com.github.jitwxs.sample.protobuf.MessageProto;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Arrays;

/**
 * 测试 ProtoBuf 序列化与反序列化
 * @author jitwxs
 * @date 2019年12月20日 1:22
 */
public class ProtobufTest {
    public static void main(String[] args) {
        final MessageProto.User.Builder newBuilder = MessageProto.User.newBuilder();

        newBuilder.setName("zhangsan");
        newBuilder.setAge(18);
        newBuilder.setSex(EnumProto.SexEnum.MALE);

        MessageProto.User user = newBuilder.build();
        System.out.println("before: " + user.toString());

        byte[] byteArray = user.toByteArray();
        System.out.println("=======");
        System.out.println("User Byte: " + Arrays.toString(byteArray));
        System.out.println("=======");

        try {
            MessageProto.User user2 = MessageProto.User.parseFrom(byteArray);
            System.out.println("after: " + user2.toString());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
