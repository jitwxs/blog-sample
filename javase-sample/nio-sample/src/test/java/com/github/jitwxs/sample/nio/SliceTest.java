package com.github.jitwxs.sample.nio;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * @author jitwxs
 * @date 2021-08-29 20:39
 */
public class SliceTest {
    /**
     * {@link ByteBuffer#slice()} 切片出新的 buffer，但底层和原 buffer 公用同一存储，会互相影响
     */
    @Test
    public void testSlice() {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte) i);
        }

        byteBuffer.position(3);
        byteBuffer.limit(6);

        final ByteBuffer sliceBuffer = byteBuffer.slice();

        for (int i = 0; i < sliceBuffer.capacity(); i++) {
            byte b = sliceBuffer.get(i);
            b *= 2;
            sliceBuffer.put(i, b);
        }

        byteBuffer.clear();
        while (byteBuffer.hasRemaining()) {
            System.out.println(byteBuffer.get());
        }
    }
}
