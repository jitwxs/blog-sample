package com.github.jitwxs.sample.aeron.agrona.buffer;

import org.agrona.AsciiNumberFormatException;
import org.agrona.ExpandableArrayBuffer;
import org.agrona.ExpandableDirectByteBuffer;
import org.agrona.MutableDirectBuffer;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MutableDirectBufferTests {
    private static final int ROUND_TRIP_ITERATIONS = 10_000_000;

    public static Collection<Class<? extends MutableDirectBuffer>> buffers() {
        return Arrays.asList(
                ExpandableArrayBuffer.class,
                ExpandableDirectByteBuffer.class
        );
    }

    /**
     * Allocate new buffer with the specified capacity.
     *
     * @param capacity to allocate.
     * @return new buffer.
     */
    private MutableDirectBuffer newBuffer(int capacity, Class<? extends MutableDirectBuffer> bufferClass) {
        try {
            final Constructor<? extends MutableDirectBuffer> constructor = bufferClass.getDeclaredConstructor(int.class);
            return constructor.newInstance(capacity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试 {@link MutableDirectBuffer#putNaturalIntAsciiFromEnd(int, int)}
     */
    @ParameterizedTest
    @MethodSource("buffers")
    public void shouldPutNaturalFromEnd(Class<? extends MutableDirectBuffer> bufferClass) {
        for (Pair<Integer, Integer> pair : Arrays.asList(
                Pair.of(1, 1),
                Pair.of(10, 2),
                Pair.of(100, 3),
                Pair.of(1000, 4),
                Pair.of(12, 2),
                Pair.of(123, 3),
                Pair.of(2345, 4),
                Pair.of(9, 1),
                Pair.of(99, 2),
                Pair.of(999, 3),
                Pair.of(9999, 4))) {
            final MutableDirectBuffer buffer = newBuffer(8 * 1024, bufferClass);

            final int value = pair.getLeft(), length = pair.getRight();

            final int start = buffer.putNaturalIntAsciiFromEnd(value, length);

            assertEquals(0, start);
            assertEquals(String.valueOf(value), buffer.getStringWithoutLengthAscii(0, length));
        }
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void putIntAsciiRoundTrip(Class<? extends MutableDirectBuffer> bufferClass) {
        final int index = 4;
        final MutableDirectBuffer buffer = newBuffer(64, bufferClass);

        for (int i = 0; i < ROUND_TRIP_ITERATIONS; i++) {
            final int value = ThreadLocalRandom.current().nextInt();
            final int length = buffer.putIntAscii(index, value);
            final int parsedValue = buffer.parseIntAscii(index, length);
            assertEquals(value, parsedValue);
        }
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void putLongAsciiRoundTrip(Class<? extends MutableDirectBuffer> bufferClass) {
        final int index = 16;
        final MutableDirectBuffer buffer = newBuffer(64, bufferClass);

        for (int i = 0; i < ROUND_TRIP_ITERATIONS; i++) {
            final long value = ThreadLocalRandom.current().nextLong();
            final int length = buffer.putLongAscii(index, value);
            final long parsedValue = buffer.parseLongAscii(index, length);
            assertEquals(value, parsedValue);
        }
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void putNaturalIntAsciiRoundTrip(Class<? extends MutableDirectBuffer> bufferClass) {
        final int index = 8;
        final MutableDirectBuffer buffer = newBuffer(64, bufferClass);

        for (int i = 0; i < ROUND_TRIP_ITERATIONS; i++) {
            final int value = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
            final int length = buffer.putNaturalIntAscii(index, value);
            final int parsedValue = buffer.parseNaturalIntAscii(index, length);
            assertEquals(value, parsedValue);
        }
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void putNaturalLongAsciiRoundTrip(Class<? extends MutableDirectBuffer> bufferClass) {
        final int index = 12;
        final MutableDirectBuffer buffer = newBuffer(64, bufferClass);

        for (int i = 0; i < ROUND_TRIP_ITERATIONS; i++) {
            final long value = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
            final int length = buffer.putNaturalLongAscii(index, value);
            final long parsedValue = buffer.parseNaturalLongAscii(index, length);
            assertEquals(value, parsedValue);
        }
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void setMemory(Class<? extends MutableDirectBuffer> bufferClass) {
        for (int length : Arrays.asList(11, 64, 1011)) {
            final int index = 2;
            final byte value = (byte) 11;
            final MutableDirectBuffer buffer = newBuffer(2 * index + length, bufferClass);

            buffer.setMemory(index, length, value);

            assertEquals(0, buffer.getByte(0));
            assertEquals(0, buffer.getByte(1));
            assertEquals(0, buffer.getByte(index + length));
            assertEquals(0, buffer.getByte(index + length + 1));
            for (int i = 0; i < length; i++) {
                assertEquals(value, buffer.getByte(index + i));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void putLongAsciiShouldHandleEightDigitNumber(Class<? extends MutableDirectBuffer> bufferClass) {
        final int index = 0;
        final MutableDirectBuffer buffer = newBuffer(16, bufferClass);

        final int length = buffer.putLongAscii(index, 87654321);
        assertEquals(8, length);

        assertEquals("87654321", buffer.getStringWithoutLengthAscii(index, length));
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void putLongAsciiShouldEncodeBoundaryValues(Class<? extends MutableDirectBuffer> bufferClass) {
        for (long value : Arrays.asList(Long.MIN_VALUE, 0L, Long.MAX_VALUE)) {
            final String encodedValue = Long.toString(value);
            final int index = 4;
            final MutableDirectBuffer buffer = newBuffer(32, bufferClass);

            final int length = buffer.putLongAscii(index, value);

            assertEquals(encodedValue.length(), length);
            assertEquals(encodedValue, buffer.getStringWithoutLengthAscii(index, length));
            assertEquals(value, buffer.parseLongAscii(index, length));
        }
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void putIntAsciiShouldEncodeBoundaryValues(Class<? extends MutableDirectBuffer> bufferClass) {
        for (int value : Arrays.asList(Integer.MIN_VALUE, 0, Integer.MAX_VALUE)) {
            final String encodedValue = Integer.toString(value);
            final int index = 3;
            final MutableDirectBuffer buffer = newBuffer(32, bufferClass);

            final int length = buffer.putIntAscii(index, value);

            assertEquals(encodedValue.length(), length);
            assertEquals(encodedValue, buffer.getStringWithoutLengthAscii(index, length));
            assertEquals(value, buffer.parseIntAscii(index, length));
        }
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void parseIntAsciiThrowsAsciiNumberFormatExceptionIfValueContainsInvalidCharacters(Class<? extends MutableDirectBuffer> bufferClass) {
        for (String value : Arrays.asList(
                "23.5",
                "+1",
                "a14349",
                "0xFF",
                "999v",
                "-",
                "+",
                "1234%67890")) {
            final int index = 2;
            final MutableDirectBuffer buffer = newBuffer(16, bufferClass);
            final int length = buffer.putStringWithoutLengthAscii(index, value);

            assertThrows(AsciiNumberFormatException.class, () -> buffer.parseIntAscii(index, length));
        }
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void parseLongAsciiThrowsAsciiNumberFormatExceptionIfValueContainsInvalidCharacters(Class<? extends MutableDirectBuffer> bufferClass) {
        for (Pair<String, Integer> pair : Arrays.asList(
                Pair.of("23.5", 2),
                Pair.of("+1", 0),
                Pair.of("a14349", 0),
                Pair.of("0xFF", 1),
                Pair.of("999v", 3),
                Pair.of("-", 0),
                Pair.of("+", 0),
                Pair.of("123456789^123456789", 9)
        )) {
            final String value = pair.getLeft();
            final int baseIndex = pair.getRight();

            final int index = 7;
            final MutableDirectBuffer buffer = newBuffer(32, bufferClass);
            final int length = buffer.putStringWithoutLengthAscii(index, value);

            assertThrows(AsciiNumberFormatException.class, () -> buffer.parseLongAscii(index, length), "error parsing long: " + value);
        }
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void parseNaturalIntAsciiThrowsAsciiNumberFormatExceptionIfValueContainsInvalidCharacters(Class<? extends MutableDirectBuffer> bufferClass) {
        for (String value : Arrays.asList(
                "23.5",
                "+1",
                "a14349",
                "0xFF",
                "999v",
                "-",
                "+",
                "1234%67890")) {
            final int index = 1;
            final MutableDirectBuffer buffer = newBuffer(16, bufferClass);
            final int length = buffer.putStringWithoutLengthAscii(index, value);

            assertThrows(AsciiNumberFormatException.class, () -> buffer.parseNaturalIntAscii(index, length));
        }
    }

    @ParameterizedTest
    @MethodSource("buffers")
    public void parseNaturalLongAsciiThrowsAsciiNumberFormatExceptionIfValueContainsInvalidCharacters(Class<? extends MutableDirectBuffer> bufferClass) {
        for (Pair<String, Integer> pair : Arrays.asList(
                Pair.of("23.5", 2),
                Pair.of("+1", 0),
                Pair.of("a14349", 0),
                Pair.of("0xFF", 1),
                Pair.of("999v", 3),
                Pair.of("-", 0),
                Pair.of("+", 0),
                Pair.of("123456789^123456789", 9)
        )) {
            final String value = pair.getLeft();
            final int baseIndex = pair.getRight();

            final int index = 8;
            final MutableDirectBuffer buffer = newBuffer(32, bufferClass);
            final int length = buffer.putStringWithoutLengthAscii(index, value);

            assertThrows(AsciiNumberFormatException.class, () -> buffer.parseNaturalLongAscii(index, length), "error parsing long: " + value);
        }
    }
}
