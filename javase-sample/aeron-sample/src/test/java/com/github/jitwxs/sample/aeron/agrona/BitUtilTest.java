package com.github.jitwxs.sample.aeron.agrona;

import org.agrona.BitUtil;
import org.junit.Test;

import static java.lang.Integer.MIN_VALUE;
import static org.junit.Assert.assertEquals;

/**
 * @see org.agrona.BitUtil
 */
public class BitUtilTest {
    @Test
    public void testFindNextPositivePowerOfTwo() {
        assertEquals(BitUtil.findNextPositivePowerOfTwo(MIN_VALUE), MIN_VALUE);
        assertEquals(BitUtil.findNextPositivePowerOfTwo(MIN_VALUE + 1), 1);
        assertEquals(BitUtil.findNextPositivePowerOfTwo(-1), 1);
        assertEquals(BitUtil.findNextPositivePowerOfTwo(0), 1);
        assertEquals(BitUtil.findNextPositivePowerOfTwo(1), 1);
        assertEquals(BitUtil.findNextPositivePowerOfTwo(2), 2);
        assertEquals(BitUtil.findNextPositivePowerOfTwo(3), 4);
        assertEquals(BitUtil.findNextPositivePowerOfTwo(4), 4);
        assertEquals(BitUtil.findNextPositivePowerOfTwo(31), 32);
        assertEquals(BitUtil.findNextPositivePowerOfTwo(32), 32);
        assertEquals(BitUtil.findNextPositivePowerOfTwo(1 << 30), 1 << 30);
        assertEquals(BitUtil.findNextPositivePowerOfTwo((1 << 30) + 1), MIN_VALUE);
    }
}
