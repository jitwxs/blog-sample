package com.github.jitwxs.sample.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Protobuf <--> ByteArray
 */
public class ByteConvertTest extends BaseTest {
    @Test
    public void testSerialize() {
        final MessageProto.User user = randomUser();

        byte[] byteArray = user.toByteArray();

        System.out.println(Arrays.toString(byteArray));
    }

    @Test
    public void testDeserialize() throws InvalidProtocolBufferException {
        final MessageProto.User user1 = randomUser();

        byte[] byteArray = user1.toByteArray();

        MessageProto.User user2 = MessageProto.User.parseFrom(byteArray);

        assertEquals(user1, user2);
    }
}