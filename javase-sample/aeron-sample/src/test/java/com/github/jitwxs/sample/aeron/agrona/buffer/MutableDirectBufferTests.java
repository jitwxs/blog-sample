package com.github.jitwxs.sample.aeron.agrona.buffer;

import org.agrona.AsciiNumberFormatException;
import org.agrona.ExpandableArrayBuffer;
import org.agrona.ExpandableDirectByteBuffer;
import org.agrona.MutableDirectBuffer;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class MutableDirectBufferTests {
    private static final int ROUND_TRIP_ITERATIONS = 10_000_000;

    private final Class<? extends MutableDirectBuffer> bufferClass;

    public MutableDirectBufferTests(Class<? extends MutableDirectBuffer> bufferClass) {
        this.bufferClass = bufferClass;
    }

    @Parameterized.Parameters
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
    private MutableDirectBuffer newBuffer(int capacity) {
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
    @Test
    public void shouldPutNaturalFromEnd() {
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
            final MutableDirectBuffer buffer = newBuffer(8 * 1024);

            final int value = pair.getLeft(), length = pair.getRight();

            final int start = buffer.putNaturalIntAsciiFromEnd(value, length);

            assertEquals(0, start);
            assertEquals(String.valueOf(value), buffer.getStringWithoutLengthAscii(0, length));
        }
    }

    @Test
    public void putIntAsciiRoundTrip() {
        final int index = 4;
        final MutableDirectBuffer buffer = newBuffer(64);

        for (int i = 0; i < ROUND_TRIP_ITERATIONS; i++) {
            final int value = ThreadLocalRandom.current().nextInt();
            final int length = buffer.putIntAscii(index, value);
            final int parsedValue = buffer.parseIntAscii(index, length);
            assertEquals(value, parsedValue);
        }
    }

    @Test
    public void putLongAsciiRoundTrip() {
        final int index = 16;
        final MutableDirectBuffer buffer = newBuffer(64);

        for (int i = 0; i < ROUND_TRIP_ITERATIONS; i++) {
            final long value = ThreadLocalRandom.current().nextLong();
            final int length = buffer.putLongAscii(index, value);
            final long parsedValue = buffer.parseLongAscii(index, length);
            assertEquals(value, parsedValue);
        }
    }

    @Test
    public void putNaturalIntAsciiRoundTrip() {
        final int index = 8;
        final MutableDirectBuffer buffer = newBuffer(64);

        for (int i = 0; i < ROUND_TRIP_ITERATIONS; i++) {
            final int value = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
            final int length = buffer.putNaturalIntAscii(index, value);
            final int parsedValue = buffer.parseNaturalIntAscii(index, length);
            assertEquals(value, parsedValue);
        }
    }

    @Test
    public void putNaturalLongAsciiRoundTrip() {
        final int index = 12;
        final MutableDirectBuffer buffer = newBuffer(64);

        for (int i = 0; i < ROUND_TRIP_ITERATIONS; i++) {
            final long value = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
            final int length = buffer.putNaturalLongAscii(index, value);
            final long parsedValue = buffer.parseNaturalLongAscii(index, length);
            assertEquals(value, parsedValue);
        }
    }

    @Test
    public void setMemory() {
        for (int length : Arrays.asList(11, 64, 1011)) {
            final int index = 2;
            final byte value = (byte) 11;
            final MutableDirectBuffer buffer = newBuffer(2 * index + length);

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

    @Test
    public void putLongAsciiShouldHandleEightDigitNumber() {
        final int index = 0;
        final MutableDirectBuffer buffer = newBuffer(16);

        final int length = buffer.putLongAscii(index, 87654321);
        assertEquals(8, length);

        assertEquals("87654321", buffer.getStringWithoutLengthAscii(index, length));
    }

    @Test
    public void putLongAsciiShouldEncodeBoundaryValues() {
        for (long value : Arrays.asList(Long.MIN_VALUE, 0L, Long.MAX_VALUE)) {
            final String encodedValue = Long.toString(value);
            final int index = 4;
            final MutableDirectBuffer buffer = newBuffer(32);

            final int length = buffer.putLongAscii(index, value);

            assertEquals(encodedValue.length(), length);
            assertEquals(encodedValue, buffer.getStringWithoutLengthAscii(index, length));
            assertEquals(value, buffer.parseLongAscii(index, length));
        }
    }

    @Test
    public void putIntAsciiShouldEncodeBoundaryValues() {
        for (int value : Arrays.asList(Integer.MIN_VALUE, 0, Integer.MAX_VALUE)) {
            final String encodedValue = Integer.toString(value);
            final int index = 3;
            final MutableDirectBuffer buffer = newBuffer(32);

            final int length = buffer.putIntAscii(index, value);

            assertEquals(encodedValue.length(), length);
            assertEquals(encodedValue, buffer.getStringWithoutLengthAscii(index, length));
            assertEquals(value, buffer.parseIntAscii(index, length));
        }
    }

    @Test
    public void parseIntAsciiThrowsAsciiNumberFormatExceptionIfValueContainsInvalidCharacters() {
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
            final MutableDirectBuffer buffer = newBuffer(16);
            final int length = buffer.putStringWithoutLengthAscii(index, value);

            assertThrows(AsciiNumberFormatException.class, () -> buffer.parseIntAscii(index, length));
        }
    }

    @Test
    public void parseLongAsciiThrowsAsciiNumberFormatExceptionIfValueContainsInvalidCharacters() {
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
            final MutableDirectBuffer buffer = newBuffer(32);
            final int length = buffer.putStringWithoutLengthAscii(index, value);

            assertThrows("error parsing long: " + value, AsciiNumberFormatException.class, () -> buffer.parseLongAscii(index, length));
        }
    }

    @Test
    public void parseNaturalIntAsciiThrowsAsciiNumberFormatExceptionIfValueContainsInvalidCharacters() {
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
            final MutableDirectBuffer buffer = newBuffer(16);
            final int length = buffer.putStringWithoutLengthAscii(index, value);

            assertThrows(AsciiNumberFormatException.class, () -> buffer.parseNaturalIntAscii(index, length));
        }
    }

    @Test
    public void parseNaturalLongAsciiThrowsAsciiNumberFormatExceptionIfValueContainsInvalidCharacters() {
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
            final MutableDirectBuffer buffer = newBuffer(32);
            final int length = buffer.putStringWithoutLengthAscii(index, value);

            assertThrows("error parsing long: " + value, AsciiNumberFormatException.class, () -> buffer.parseNaturalLongAscii(index, length));
        }
    }
}
