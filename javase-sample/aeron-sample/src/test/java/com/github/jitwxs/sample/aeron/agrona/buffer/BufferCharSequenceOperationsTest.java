package com.github.jitwxs.sample.aeron.agrona.buffer;

import org.agrona.ExpandableArrayBuffer;
import org.agrona.ExpandableDirectByteBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.stream.Stream;

import static org.agrona.BitUtil.SIZE_OF_INT;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BufferCharSequenceOperationsTest {
    private static final int BUFFER_CAPACITY = 256;
    private static final int INDEX = 8;

    private static Stream<MutableDirectBuffer> buffers() {
        return Stream.of(
                new UnsafeBuffer(new byte[BUFFER_CAPACITY]),
                new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_CAPACITY)),
                new ExpandableArrayBuffer(BUFFER_CAPACITY),
                new ExpandableDirectByteBuffer(BUFFER_CAPACITY));
    }

    /**
     * 测试 {@link MutableDirectBuffer#putStringAscii} 和 {@link MutableDirectBuffer#getStringAscii}
     */
    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldInsertNonAsciiAsQuestionMark(final MutableDirectBuffer buffer) {
        final CharSequence value = new StringBuilder("Hello World £");
        final CharSequence expected = "Hello World ?";

        buffer.putStringAscii(INDEX, value);
        assertEquals(buffer.getStringAscii(INDEX), expected);
    }

    /**
     * 测试 {@link MutableDirectBuffer#putStringWithoutLengthAscii} 和 {@link MutableDirectBuffer#getStringWithoutLengthAscii}
     */
    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldAppendAsciiStringInParts(final MutableDirectBuffer buffer) {
        final CharSequence value = new StringBuilder("Hello World Test");
        final String expected = "Hello World Test";

        int stringIndex = 0;
        int bufferIndex = INDEX + SIZE_OF_INT;

        bufferIndex += buffer.putStringWithoutLengthAscii(bufferIndex, value, stringIndex, 5);

        stringIndex += 5;
        bufferIndex += buffer.putStringWithoutLengthAscii(bufferIndex, value, stringIndex, 5);

        stringIndex += 5;
        bufferIndex += buffer.putStringWithoutLengthAscii(bufferIndex, value, stringIndex, value.length() - stringIndex);

        assertEquals(bufferIndex, expected.length() + INDEX + SIZE_OF_INT);
        buffer.putInt(INDEX, expected.length());

        assertEquals(buffer.getStringWithoutLengthAscii(INDEX + SIZE_OF_INT, expected.length()), expected);
        assertEquals(buffer.getStringAscii(INDEX), expected);
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldRoundTripAsciiStringNativeLength(final MutableDirectBuffer buffer) {
        final CharSequence value = new StringBuilder("Hello World");
        final String expected = "Hello World";

        buffer.putStringAscii(INDEX, value);

        assertEquals(buffer.getStringAscii(INDEX), expected);
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldRoundTripAsciiStringBigEndianLength(final MutableDirectBuffer buffer) {
        final CharSequence value = new StringBuilder("Hello World");
        final String expected = "Hello World";

        buffer.putStringAscii(INDEX, value, ByteOrder.BIG_ENDIAN);

        assertEquals(buffer.getStringAscii(INDEX, ByteOrder.BIG_ENDIAN), expected);
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldRoundTripAsciiStringWithoutLength(final MutableDirectBuffer buffer) {
        final CharSequence value = new StringBuilder("Hello World");
        final String expected = "Hello World";

        buffer.putStringWithoutLengthAscii(INDEX, value);

        assertEquals(buffer.getStringWithoutLengthAscii(INDEX, value.length()), expected);
    }
}
