package com.github.jitwxs.sample.aeron.agrona.buffer;

import org.agrona.ExpandableArrayBuffer;
import org.agrona.ExpandableDirectByteBuffer;
import org.agrona.MutableDirectBuffer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BufferExpansionTest {
    private static Stream<MutableDirectBuffer> buffers() {
        return Stream.of(
                new ExpandableArrayBuffer(),
                new ExpandableDirectByteBuffer()
        );
    }

    /**
     * buffer 使用默认大小，put 时需要扩容
     */
    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldExpand(final MutableDirectBuffer buffer) {
        final int capacity = buffer.capacity();

        final int index = capacity + 50;
        final int value = 777;
        buffer.putInt(index, value);

        assertTrue(buffer.capacity() > capacity);
        assertEquals(buffer.getInt(index), value);
    }

    /**
     * {@link ExpandableArrayBuffer} 设置初始大小为 0，put 时需要扩容
     */
    @Test
    public void shouldExpandArrayBufferFromZeroCapacity() {
        final MutableDirectBuffer buffer = new ExpandableArrayBuffer(0);
        buffer.putByte(0, (byte) 4);

        assertTrue(buffer.capacity() > 0);
    }

    /**
     * {@link ExpandableArrayBuffer} put 第二个元素时，触发扩容
     */
    @Test
    public void shouldExpandArrayBufferFromOneCapacity() {
        final MutableDirectBuffer buffer = new ExpandableArrayBuffer(1);
        buffer.putByte(0, (byte) 4);
        buffer.putByte(1, (byte) 2);
    }

    /**
     * {@link ExpandableDirectByteBuffer} 设置初始大小为 0，put 时需要扩容
     */
    @Test
    public void shouldExpandDirectBufferFromZeroCapacity() {
        final MutableDirectBuffer buffer = new ExpandableDirectByteBuffer(0);
        buffer.putByte(0, (byte) 4);

        assertTrue(buffer.capacity() > 0);
    }

    /**
     * {@link ExpandableDirectByteBuffer} put 第二个元素时，触发扩容
     */
    @Test
    public void shouldExpandDirectBufferFromOneCapacity() {
        final MutableDirectBuffer buffer = new ExpandableDirectByteBuffer(1);
        buffer.putByte(0, (byte) 4);
        buffer.putByte(1, (byte) 2);
    }
}
