package com.github.jitwxs.sample.aeron.agrona.collections;

import org.agrona.BitUtil;
import org.agrona.collections.BiInt2ObjectMap;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @see BiInt2ObjectMap
 */
public class BiInt2ObjectMapTest {
    private final BiInt2ObjectMap<String> map = new BiInt2ObjectMap<>();

    @Test
    public void shouldInitialiseUnderlyingImplementation() {
        final int initialCapacity = 10;
        final float loadFactor = 0.6f;
        final BiInt2ObjectMap<String> map = new BiInt2ObjectMap<>(initialCapacity, loadFactor);

        assertEquals(map.capacity(), BitUtil.findNextPositivePowerOfTwo(initialCapacity));
        assertEquals(map.loadFactor(), loadFactor);
    }

    @Test
    public void shouldReportEmpty() {
        assertEquals(map.isEmpty(), true);
    }

    @Test
    public void shouldPutItem() {
        final String testValue = "Test";
        final int keyPartA = 3;
        final int keyPartB = 7;

        assertNull(map.put(keyPartA, keyPartB, testValue));
        assertEquals(map.size(), 1);
    }

    @Test
    public void shouldPutAndGetItem() {
        final String testValue = "Test";
        final int keyPartA = 3;
        final int keyPartB = 7;

        assertNull(map.put(keyPartA, keyPartB, testValue));
        assertEquals(map.get(keyPartA, keyPartB), testValue);
    }

    @Test
    public void shouldReturnNullWhenNotFoundItem() {
        final int keyPartA = 3;
        final int keyPartB = 7;

        assertNull(map.get(keyPartA, keyPartB));
    }

    @Test
    public void shouldRemoveItem() {
        final String testValue = "Test";
        final int keyPartA = 3;
        final int keyPartB = 7;

        map.put(keyPartA, keyPartB, testValue);
        assertEquals(map.remove(keyPartA, keyPartB), testValue);
        assertNull(map.get(keyPartA, keyPartB));
    }

    @Test
    public void shouldIterateValues() {
        final Set<String> expectedSet = new HashSet<>();
        final int count = 7;

        for (int i = 0; i < count; i++) {
            final String value = String.valueOf(i);
            expectedSet.add(value);
            map.put(i, i + 97, value);
        }

        final Set<String> actualSet = new HashSet<>();

        map.forEach(actualSet::add);

        assertEquals(actualSet, expectedSet);
    }

    @Test
    public void shouldIterateEntries() {
        final Set<EntryCapture<String>> expectedSet = new HashSet<>();
        final int count = 7;

        for (int i = 0; i < count; i++) {
            final String value = String.valueOf(i);
            expectedSet.add(new EntryCapture<>(i, i + 97, value));
            map.put(i, i + 97, value);
        }

        final Set<EntryCapture<String>> actualSet = new HashSet<>();

        map.forEach((keyPartA, keyPartB, value) -> actualSet.add(new EntryCapture<>(keyPartA, keyPartB, value)));

        assertEquals(actualSet, expectedSet);
    }

    @Test
    public void shouldToString() {
        final int count = 7;

        for (int i = 0; i < count; i++) {
            final String value = String.valueOf(i);
            map.put(i, i + 97, value);
        }

        assertEquals(map.toString(), "{0_97=0, 1_98=1, 4_101=4, 3_100=3, 5_102=5, 6_103=6, 2_99=2}");
    }

    @Test
    public void shouldPutAndGetKeysOfNegativeValue() {
        map.put(721632679, 333118496, "a");
        assertEquals(map.get(721632679, 333118496), "a");

        map.put(721632719, -659033725, "b");
        assertEquals(map.get(721632719, -659033725), "b");

        map.put(721632767, -235401032, "c");
        assertEquals(map.get(721632767, -235401032), "c");

        map.put(721632839, 1791470537, "d");
        assertEquals(map.get(721632839, 1791470537), "d");

        map.put(721633069, -939458690, "e");
        assertEquals(map.get(721633069, -939458690), "e");

        map.put(721633127, 1620485039, "f");
        assertEquals(map.get(721633127, 1620485039), "f");

        map.put(721633163, -1503337805, "g");
        assertEquals(map.get(721633163, -1503337805), "g");

        map.put(721633229, -2073657736, "h");
        assertEquals(map.get(721633229, -2073657736), "h");

        map.put(721633255, -1278969172, "i");
        assertEquals(map.get(721633255, -1278969172), "i");

        map.put(721633257, -1230662585, "j");
        assertEquals(map.get(721633257, -1230662585), "j");

        map.put(721633319, -532637417, "k");
        assertEquals(map.get(721633319, -532637417), "k");
    }

    public static class EntryCapture<V> {
        public final int keyPartA;
        public final int keyPartB;
        public final V value;

        public EntryCapture(final int keyPartA, final int keyPartB, final V value) {
            this.keyPartA = keyPartA;
            this.keyPartB = keyPartB;
            this.value = value;
        }

        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof EntryCapture)) {
                return false;
            }

            final EntryCapture<?> that = (EntryCapture<?>) o;

            return keyPartA == that.keyPartA && keyPartB == that.keyPartB && value.equals(that.value);

        }

        public int hashCode() {
            int result = keyPartA;
            result = 31 * result + keyPartB;
            result = 31 * result + value.hashCode();

            return result;
        }
    }
}
