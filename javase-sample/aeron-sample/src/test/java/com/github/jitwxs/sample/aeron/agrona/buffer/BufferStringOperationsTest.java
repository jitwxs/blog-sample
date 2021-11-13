package com.github.jitwxs.sample.aeron.agrona.buffer;

import org.agrona.ExpandableArrayBuffer;
import org.agrona.ExpandableDirectByteBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collection;

import static org.agrona.BitUtil.SIZE_OF_INT;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BufferStringOperationsTest {
    private static final int BUFFER_CAPACITY = 256;
    private static final int INDEX = 8;

    private final MutableDirectBuffer buffer;

    public BufferStringOperationsTest(MutableDirectBuffer buffer) {
        this.buffer = buffer;
    }

    @Parameterized.Parameters
    public static Collection<MutableDirectBuffer> buffers() {
        return Arrays.asList(
                new UnsafeBuffer(new byte[BUFFER_CAPACITY]),
                new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_CAPACITY)),
                new ExpandableArrayBuffer(BUFFER_CAPACITY),
                new ExpandableDirectByteBuffer(BUFFER_CAPACITY));
    }

    @Test
    public void shouldInsertNonAsciiAsQuestionMark() {
        final String value = "Hello World £";

        buffer.putStringAscii(INDEX, value);
        assertEquals(buffer.getStringAscii(INDEX), "Hello World ?");
    }

    @Test
    public void shouldAppendAsciiStringInParts() {
        final String value = "Hello World Test";

        int stringIndex = 0;
        int bufferIndex = INDEX + SIZE_OF_INT;

        bufferIndex += buffer.putStringWithoutLengthAscii(bufferIndex, value, stringIndex, 5);

        stringIndex += 5;
        bufferIndex += buffer.putStringWithoutLengthAscii(bufferIndex, value, stringIndex, 5);

        stringIndex += 5;
        bufferIndex += buffer.putStringWithoutLengthAscii(
                bufferIndex, value, stringIndex, value.length() - stringIndex);

        assertEquals(bufferIndex, value.length() + INDEX + SIZE_OF_INT);
        buffer.putInt(INDEX, value.length());

        assertEquals(buffer.getStringWithoutLengthAscii(INDEX + SIZE_OF_INT, value.length()), value);
        assertEquals(buffer.getStringAscii(INDEX), value);
    }

    @Test
    public void shouldRoundTripAsciiStringNativeLength() {
        final String value = "Hello World";

        buffer.putStringAscii(INDEX, value);

        assertEquals(buffer.getStringAscii(INDEX), value);
    }

    @Test
    public void shouldRoundTripAsciiStringBigEndianLength() {
        final String value = "Hello World";

        buffer.putStringAscii(INDEX, value, ByteOrder.BIG_ENDIAN);

        assertEquals(buffer.getStringAscii(INDEX, ByteOrder.BIG_ENDIAN), value);
    }

    @Test
    public void shouldRoundTripAsciiStringWithoutLength() {
        final String value = "Hello World";

        buffer.putStringWithoutLengthAscii(INDEX, value);

        assertEquals(buffer.getStringWithoutLengthAscii(INDEX, value.length()), value);
    }

    @Test
    public void shouldRoundTripUtf8StringNativeLength() {
        final String value = "Hello£ World £";

        buffer.putStringUtf8(INDEX, value);

        assertEquals(buffer.getStringUtf8(INDEX), value);
    }

    @Test
    public void shouldRoundTripUtf8StringBigEndianLength() {
        final String value = "Hello£ World £";

        buffer.putStringUtf8(INDEX, value, ByteOrder.BIG_ENDIAN);

        assertEquals(buffer.getStringUtf8(INDEX, ByteOrder.BIG_ENDIAN), value);
    }

    @Test
    public void shouldRoundTripUtf8StringWithoutLength() {
        final String value = "Hello£ World £";

        final int encodedLength = buffer.putStringWithoutLengthUtf8(INDEX, value);

        assertEquals(buffer.getStringWithoutLengthUtf8(INDEX, encodedLength), value);
    }

    @Test
    public void shouldGetAsciiToAppendable() {
        final String value = "Hello World";

        buffer.putStringAscii(INDEX, value);

        final Appendable appendable = new StringBuilder();
        final int encodedLength = buffer.getStringAscii(INDEX, appendable);

        assertEquals(encodedLength, value.length());
        assertEquals(appendable.toString(), value);
    }

    @Test
    public void shouldGetAsciiWithByteOrderToAppendable() {
        final String value = "Hello World";

        buffer.putStringAscii(INDEX, value, ByteOrder.BIG_ENDIAN);

        final Appendable appendable = new StringBuilder();
        final int encodedLength = buffer.getStringAscii(INDEX, appendable, ByteOrder.BIG_ENDIAN);

        assertEquals(encodedLength, value.length());
        assertEquals(appendable.toString(), value);
    }

    @Test
    public void shouldGetAsciiToAppendableForLength() {
        final String value = "Hello World";

        buffer.putStringAscii(INDEX, value);

        final int length = 5;
        final Appendable appendable = new StringBuilder();
        final int encodedLength = buffer.getStringAscii(INDEX, length, appendable);

        assertEquals(encodedLength, length);
        assertEquals(appendable.toString(), value.substring(0, length));
    }

    @Test
    public void shouldAppendWithInvalidChar() {
        final String value = "Hello World";

        buffer.putStringAscii(INDEX, value);
        buffer.putByte(INDEX + SIZE_OF_INT + 5, (byte) 163);

        final Appendable appendable = new StringBuilder();
        final int encodedLength = buffer.getStringAscii(INDEX, appendable);

        assertEquals(encodedLength, value.length());
        assertEquals(appendable.toString(), "Hello?World");
    }

    @Test
    public void shouldAppendWithInvalidCharWithoutLength() {
        final String value = "Hello World";

        buffer.putStringAscii(INDEX, value);
        buffer.putByte(INDEX + SIZE_OF_INT + 3, (byte) 163);

        final int length = 5;
        final Appendable appendable = new StringBuilder();
        final int encodedLength = buffer.getStringWithoutLengthAscii(INDEX + SIZE_OF_INT, length, appendable);

        assertEquals(encodedLength, length);
        assertEquals(appendable.toString(), "Hel?o");
    }
}
