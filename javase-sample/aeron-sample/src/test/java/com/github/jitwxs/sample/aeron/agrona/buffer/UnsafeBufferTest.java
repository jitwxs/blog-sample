package com.github.jitwxs.sample.aeron.agrona.buffer;

import org.agrona.concurrent.UnsafeBuffer;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class UnsafeBufferTest {
    @Test
    public void unsafeLongExtras() {
        //allocate a buffer to store the long
        final UnsafeBuffer unsafeBuffer = new UnsafeBuffer(ByteBuffer.allocate(Long.BYTES));

        //place 41 at index 0
        unsafeBuffer.putLong(0, 41);
        //add 1 to the long at index 0 and return the old value
        long originalValue = unsafeBuffer.getAndAddLong(0, 1);
        //read the value of the long at index 0
        long plus1 = unsafeBuffer.getLong(0);
        assertEquals(41, originalValue);
        assertEquals(42, plus1);

        //read current value while writing a new value
        long oldValue = unsafeBuffer.getAndSetLong(0, 43);
        //read the value of the long at index 0
        long newValue = unsafeBuffer.getLong(0);
        assertEquals(42, oldValue);
        assertEquals(43, newValue);

        //check the value was what was expected, returning true/false if it was. Then update the value a new value
        boolean wasExpected = unsafeBuffer.compareAndSetLong(0, 43, 44);
        //read the value of the long at index 0
        long updatedValue = unsafeBuffer.getLong(0);

        assertTrue(wasExpected);
        assertEquals(44, updatedValue);

        //check the value was what was expected, returning true/false if it was. Then update the value a new value
        boolean notAsExpected = unsafeBuffer.compareAndSetLong(0, 502, 688);
        //read the value of the long at index 0
        long ignoredUpdate = unsafeBuffer.getLong(0);

        assertFalse(notAsExpected);
        assertNotEquals(688, ignoredUpdate);
    }
}