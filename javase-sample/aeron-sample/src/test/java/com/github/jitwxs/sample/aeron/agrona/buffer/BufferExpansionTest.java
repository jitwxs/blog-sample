package com.github.jitwxs.sample.aeron.agrona.buffer;

import org.agrona.ExpandableArrayBuffer;
import org.agrona.ExpandableDirectByteBuffer;
import org.agrona.MutableDirectBuffer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class BufferExpansionTest {

    private final MutableDirectBuffer buffer;

    public BufferExpansionTest(MutableDirectBuffer buffer) {
        this.buffer = buffer;
    }

    @Parameterized.Parameters
    public static Collection<MutableDirectBuffer> buffers() {
        return Arrays.asList(
                new ExpandableArrayBuffer(),
                new ExpandableDirectByteBuffer()
        );
    }

    /**
     * buffer 使用默认大小，put 时需要扩容
     */
    @Test
    public void shouldExpand() {
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
