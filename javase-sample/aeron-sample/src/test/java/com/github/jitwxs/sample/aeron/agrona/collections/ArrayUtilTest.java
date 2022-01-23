package com.github.jitwxs.sample.aeron.agrona.collections;

import org.agrona.collections.ArrayUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @see ArrayUtil
 */
public class ArrayUtilTest {
    // Reference Equality
    private static final Integer ONE = 1;
    private static final Integer TWO = 2;
    private static final Integer THREE = 3;

    private final Integer[] values = {ONE, TWO};

    @Test
    public void shouldNotRemoveMissingElement() {
        final Integer[] result = ArrayUtil.remove(values, THREE);

        assertArrayEquals(values, result);
    }

    @Test
    public void shouldRemovePresentElementAtEnd() {
        final Integer[] result = ArrayUtil.remove(values, TWO);

        assertArrayEquals(new Integer[]{ONE}, result);
    }

    @Test
    public void shouldRemovePresentElementAtStart() {
        final Integer[] result = ArrayUtil.remove(values, ONE);

        assertArrayEquals(new Integer[]{TWO}, result);
    }

    @Test
    public void shouldRemoveByIndex() {
        final Integer[] result = ArrayUtil.remove(values, 0);

        assertArrayEquals(new Integer[]{TWO}, result);
    }
}
