package jit.wxs.demo.test;

import com.google.protobuf.InvalidProtocolBufferException;
import jit.wxs.demo.dto.UserProto;
import jit.wxs.demo.enums.SexEnumProto;

import java.util.Arrays;

/**
 * 测试 ProtoBuf 序列化与反序列化
 * @author jitwxs
 * @date 2019年12月20日 1:22
 */
public class ProtoBufTest {
    public static void main(String[] args) {
        UserProto.User.Builder builder =  UserProto.User.newBuilder();
        builder.setName("zhangsan");
        builder.setAge(18);
        builder.setSex(SexEnumProto.SexEnum.MALE);

        UserProto.User user = builder.build();
        System.out.println("before: " + user.toString());

        byte[] byteArray = user.toByteArray();
        System.out.println("=======");
        System.out.println("User Byte: " + Arrays.toString(byteArray));
        System.out.println("=======");

        try {
            UserProto.User user2 = UserProto.User.parseFrom(byteArray);
            System.out.println("after: " + user2.toString());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
