package com.github.jitwxs.sample.aeron.agrona.concurrent;

import org.agrona.concurrent.ManyToOneConcurrentLinkedQueue;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @see ManyToOneConcurrentLinkedQueue
 */
public class ManyToOneConcurrentLinkedQueueTest {
    private final Queue<Integer> queue = new ManyToOneConcurrentLinkedQueue<>();

    @Test
    public void shouldBeEmpty() {
        assertTrue(queue.isEmpty());
        assertEquals(queue.size(), 0);
    }

    @Test
    public void shouldNotBeEmpty() {
        queue.offer(1);

        assertFalse(queue.isEmpty());
        assertEquals(queue.size(), 1);
    }

    @Test
    public void shouldFailToPoll() {
        assertNull(queue.poll());
    }

    @Test
    public void shouldOfferItem() {
        assertTrue(queue.offer(7));
        assertEquals(queue.size(), 1);
    }

    @Test
    public void shouldExchangeItem() {
        final Integer testItem = 1;
        queue.offer(testItem);

        assertEquals(queue.poll(), testItem);
    }

    @Test
    public void shouldExchangeInFifoOrder() {
        final int numItems = 7;

        for (int i = 0; i < numItems; i++) {
            queue.offer(i);
        }

        assertEquals(queue.size(), numItems);

        for (Integer i = 0; i < numItems; i++) {
            assertEquals(queue.poll(), i);
        }

        assertEquals(queue.size(), 0);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void shouldExchangeInFifoOrderInterleaved() {
        final int numItems = 7;

        for (Integer i = 0; i < numItems; i++) {
            queue.offer(i);
            assertEquals(queue.poll(), i);
        }

        assertEquals(queue.size(), 0);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void shouldToString() {
        assertEquals(queue.toString(), "{}");

        for (int i = 0; i < 5; i++) {
            queue.offer(i);
        }

        assertEquals(queue.toString(), "{0, 1, 2, 3, 4}");
    }
}
