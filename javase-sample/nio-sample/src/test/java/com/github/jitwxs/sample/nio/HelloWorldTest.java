package com.github.jitwxs.sample.nio;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * @author jitwxs
 * @date 2021-08-29 20:29
 */
public class HelloWorldTest {
    /**
     * 首个程序
     */
    @Test
    public void testHelloWorld() {
        // 初始化大小为 10 IntBuffer
        int bufferSize = 10, elementSize = 5;

        final IntBuffer intBuffer = IntBuffer.allocate(bufferSize);

        for (int i = 0; i < elementSize; i++) {
            intBuffer.put(new SecureRandom().nextInt(20));
        }

        System.out.printf("before flip limit: %s, capacity: %s, position: %s\n", intBuffer.limit(), intBuffer.capacity(), intBuffer.position());

        Assert.assertEquals(bufferSize, intBuffer.limit());
        Assert.assertEquals(bufferSize, intBuffer.capacity());
        Assert.assertEquals(elementSize, intBuffer.position());

        // 读写翻转
        intBuffer.flip();

        System.out.println("after flip limit size: " + intBuffer.limit());
        System.out.println("starting loop buffer...");

        while (intBuffer.hasRemaining()) {
            System.out.printf("position: %s, limit: %s, capacity: %s, value: %s\n",
                    intBuffer.position(), intBuffer.limit(), intBuffer.capacity(), intBuffer.get());
        }
    }

    /**
     * 特定类型的 put/get 方法，读取时需要按照 put 的顺序，否则会出错
     */
    @Test
    public void testPutAndGet() {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        byteBuffer.putInt(1);
        byteBuffer.putDouble(3.2);
        byteBuffer.putChar('张');
        byteBuffer.putShort((short) 2);

        byteBuffer.flip();

        Assert.assertEquals(1, byteBuffer.getInt());
        Assert.assertEquals(3.2, byteBuffer.getDouble(), 0D);
        Assert.assertNotEquals('张', byteBuffer.getInt());
    }
}
