package com.github.jitwxs.sample.nio;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

/**
 * @author jitwxs
 * @date 2021-08-29 20:40
 */
public class ReadOnlyBufferTest {
    /**
     * 普通buffer可以通过调用 {@link ByteBuffer#asReadOnlyBuffer()} 返回成只读 buffer；但只读 buffer 不能转换回普通 buffer
     */
    @Test
    public void testReadOnlyBuffer() {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        System.out.println("Init Class:" + byteBuffer.getClass());

        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte) i);
        }

        // 方法传递过程很有用，能控制接收方不能修改
        final ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();

        System.out.println("After asReadOnlyBuffer() Class:" + readOnlyBuffer.getClass());

        readOnlyBuffer.position(0);

        Assert.assertThrows(ReadOnlyBufferException.class, () -> readOnlyBuffer.put((byte) 2));
    }
}
