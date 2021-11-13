package com.github.jitwxs.sample.aeron.agrona.buffer;

import org.agrona.ExpandableArrayBuffer;
import org.agrona.ExpandableDirectByteBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BufferCompareTest {
    @Test
    public void shouldEqualOnSameTypeAndValue() {
        final MutableDirectBuffer lhsBuffer = new ExpandableArrayBuffer();
        final MutableDirectBuffer rhsBuffer = new ExpandableArrayBuffer();

        lhsBuffer.putStringUtf8(0, "Hello World");
        rhsBuffer.putStringUtf8(0, "Hello World");

        assertEquals(lhsBuffer.compareTo(rhsBuffer), 0);
    }

    @Test
    public void shouldEqualOnDifferentTypeAndValue() {
        final MutableDirectBuffer lhsBuffer = new ExpandableArrayBuffer();
        final MutableDirectBuffer rhsBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(lhsBuffer.capacity()));

        lhsBuffer.putStringUtf8(0, "Hello World");
        rhsBuffer.putStringUtf8(0, "Hello World");

        assertEquals(lhsBuffer.compareTo(rhsBuffer), 0);
    }

    @Test
    public void shouldEqualOnDifferentExpandableTypeAndValue() {
        final MutableDirectBuffer lhsBuffer = new ExpandableArrayBuffer();
        final MutableDirectBuffer rhsBuffer = new ExpandableDirectByteBuffer();

        lhsBuffer.putStringUtf8(0, "Hello World");
        rhsBuffer.putStringUtf8(0, "Hello World");

        assertEquals(lhsBuffer.compareTo(rhsBuffer), 0);
    }

    @Test
    public void shouldBeGreater() {
        final MutableDirectBuffer lhsBuffer = new ExpandableArrayBuffer();
        final MutableDirectBuffer rhsBuffer = new ExpandableArrayBuffer();

        lhsBuffer.putStringUtf8(0, "123");
        rhsBuffer.putStringUtf8(0, "124");

        assertTrue(lhsBuffer.compareTo(rhsBuffer) < 0);
    }

    @Test
    public void shouldBeLess() {
        final MutableDirectBuffer lhsBuffer = new ExpandableArrayBuffer();
        final MutableDirectBuffer rhsBuffer = new ExpandableArrayBuffer();

        lhsBuffer.putStringUtf8(0, "124");
        rhsBuffer.putStringUtf8(0, "123");

        assertTrue(lhsBuffer.compareTo(rhsBuffer) > 0);
    }
}
