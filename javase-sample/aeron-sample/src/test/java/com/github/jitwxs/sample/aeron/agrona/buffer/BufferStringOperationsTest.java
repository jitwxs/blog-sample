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

public class BufferStringOperationsTest {
    private static final int BUFFER_CAPACITY = 256;
    private static final int INDEX = 8;

    private static Stream<MutableDirectBuffer> buffers() {
        return Stream.of(
                new UnsafeBuffer(new byte[BUFFER_CAPACITY]),
                new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_CAPACITY)),
                new ExpandableArrayBuffer(BUFFER_CAPACITY),
                new ExpandableDirectByteBuffer(BUFFER_CAPACITY));
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldInsertNonAsciiAsQuestionMark(final MutableDirectBuffer buffer) {
        final String value = "Hello World £";

        buffer.putStringAscii(INDEX, value);
        assertEquals(buffer.getStringAscii(INDEX), "Hello World ?");
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldAppendAsciiStringInParts(final MutableDirectBuffer buffer) {
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

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldRoundTripAsciiStringNativeLength(final MutableDirectBuffer buffer) {
        final String value = "Hello World";

        buffer.putStringAscii(INDEX, value);

        assertEquals(buffer.getStringAscii(INDEX), value);
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldRoundTripAsciiStringBigEndianLength(final MutableDirectBuffer buffer) {
        final String value = "Hello World";

        buffer.putStringAscii(INDEX, value, ByteOrder.BIG_ENDIAN);

        assertEquals(buffer.getStringAscii(INDEX, ByteOrder.BIG_ENDIAN), value);
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldRoundTripAsciiStringWithoutLength(final MutableDirectBuffer buffer) {
        final String value = "Hello World";

        buffer.putStringWithoutLengthAscii(INDEX, value);

        assertEquals(buffer.getStringWithoutLengthAscii(INDEX, value.length()), value);
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldRoundTripUtf8StringNativeLength(final MutableDirectBuffer buffer) {
        final String value = "Hello£ World £";

        buffer.putStringUtf8(INDEX, value);

        assertEquals(buffer.getStringUtf8(INDEX), value);
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldRoundTripUtf8StringBigEndianLength(final MutableDirectBuffer buffer) {
        final String value = "Hello£ World £";

        buffer.putStringUtf8(INDEX, value, ByteOrder.BIG_ENDIAN);

        assertEquals(buffer.getStringUtf8(INDEX, ByteOrder.BIG_ENDIAN), value);
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldRoundTripUtf8StringWithoutLength(final MutableDirectBuffer buffer) {
        final String value = "Hello£ World £";

        final int encodedLength = buffer.putStringWithoutLengthUtf8(INDEX, value);

        assertEquals(buffer.getStringWithoutLengthUtf8(INDEX, encodedLength), value);
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldGetAsciiToAppendable(final MutableDirectBuffer buffer) {
        final String value = "Hello World";

        buffer.putStringAscii(INDEX, value);

        final Appendable appendable = new StringBuilder();
        final int encodedLength = buffer.getStringAscii(INDEX, appendable);

        assertEquals(encodedLength, value.length());
        assertEquals(appendable.toString(), value);
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldGetAsciiWithByteOrderToAppendable(final MutableDirectBuffer buffer) {
        final String value = "Hello World";

        buffer.putStringAscii(INDEX, value, ByteOrder.BIG_ENDIAN);

        final Appendable appendable = new StringBuilder();
        final int encodedLength = buffer.getStringAscii(INDEX, appendable, ByteOrder.BIG_ENDIAN);

        assertEquals(encodedLength, value.length());
        assertEquals(appendable.toString(), value);
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldGetAsciiToAppendableForLength(final MutableDirectBuffer buffer) {
        final String value = "Hello World";

        buffer.putStringAscii(INDEX, value);

        final int length = 5;
        final Appendable appendable = new StringBuilder();
        final int encodedLength = buffer.getStringAscii(INDEX, length, appendable);

        assertEquals(encodedLength, length);
        assertEquals(appendable.toString(), value.substring(0, length));
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldAppendWithInvalidChar(final MutableDirectBuffer buffer) {
        final String value = "Hello World";

        buffer.putStringAscii(INDEX, value);
        buffer.putByte(INDEX + SIZE_OF_INT + 5, (byte) 163);

        final Appendable appendable = new StringBuilder();
        final int encodedLength = buffer.getStringAscii(INDEX, appendable);

        assertEquals(encodedLength, value.length());
        assertEquals(appendable.toString(), "Hello?World");
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldAppendWithInvalidCharWithoutLength(final MutableDirectBuffer buffer) {
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
