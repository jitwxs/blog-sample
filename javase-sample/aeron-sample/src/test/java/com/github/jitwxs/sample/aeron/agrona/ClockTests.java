package com.github.jitwxs.sample.aeron.agrona;

import org.agrona.concurrent.*;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @see EpochClock
 */
public class ClockTests {
    /**
     * 测试 {@link SystemEpochClock} API
     */
    @Test
    public void systemEpochClock() {
        EpochClock clock = SystemEpochClock.INSTANCE;
        long time = clock.time();
        assertNotEquals(0L, time);
    }

    /**
     * 测试 {@link CachedEpochClock} API
     */
    @Test
    public void cachedEpochClock() {
        CachedEpochClock clock = new CachedEpochClock();
        clock.update(1L);

        assertEquals(1L, clock.time());

        clock.update(2L);
        assertEquals(2L, clock.time());

        clock.advance(98L);
        assertEquals(100L, clock.time());
    }

    /**
     * 测试 {@link SystemEpochMicroClock} API
     */
    @Test
    public void systemEpochMicroClock() {
        EpochMicroClock clock = new SystemEpochMicroClock();
        long time = clock.microTime();
        assertNotEquals(0L, time);
    }

    /**
     * 测试 {@link SystemEpochNanoClock} API
     */
    @Test
    public void systemEpochNanoClock() {
        EpochNanoClock clock = new SystemEpochNanoClock();
        long time = clock.nanoTime();
        assertNotEquals(0L, time);
    }

    /**
     * 测试 {@link OffsetEpochNanoClock} API
     */
    @Test
    public void offsetEpochNanoClock() {
        EpochNanoClock clock = new OffsetEpochNanoClock();
        long time = clock.nanoTime();
        assertNotEquals(0L, time);
    }

    @Test
    public void rollovers() {
        System.out.println(new Date(Integer.MAX_VALUE * 1000L)); //epoch seconds: Mon Jan 18 22:14:07 EST 2038
        System.out.println(new Date(Long.MAX_VALUE)); //epoch millis: Sun Aug 17 02:12:55 EST 292278994
    }
}
