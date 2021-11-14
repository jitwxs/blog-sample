package com.github.jitwxs.sample.aeron.agrona;

import org.agrona.BitUtil;
import org.agrona.DeadlineTimerWheel;
import org.agrona.collections.Long2LongHashMap;
import org.agrona.collections.MutableLong;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @see org.agrona.DeadlineTimerWheel
 */
public class DeadlineTimerWheelTest {
    private static final TimeUnit TIME_UNIT = TimeUnit.NANOSECONDS;
    private static final int RESOLUTION = BitUtil.findNextPositivePowerOfTwo((int) TimeUnit.MILLISECONDS.toNanos(1));

    @Test
    public void shouldExceptionOnNonPowerOfTwoTicksPerWheel() {
        assertThrows(IllegalArgumentException.class, () -> new DeadlineTimerWheel(TIME_UNIT, 0, 16, 10));
    }

    @Test
    public void shouldExceptionOnNonPowerOfTwoResolution() {
        assertThrows(IllegalArgumentException.class, () -> new DeadlineTimerWheel(TIME_UNIT, 0, 17, 8));
    }

    @Test
    public void shouldDefaultConfigure() {
        final int startTime = 7;
        final int tickResolution = 16;
        final int ticksPerWheel = 8;
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, startTime, tickResolution, ticksPerWheel);

        assertEquals(wheel.timeUnit(), TIME_UNIT);
        assertEquals(wheel.tickResolution(), tickResolution);
        assertEquals(wheel.ticksPerWheel(), ticksPerWheel);
        assertEquals(wheel.startTime(), startTime);
    }

    @Test
    public void shouldBeAbleToScheduleTimerOnEdgeOfTick() {
        long controlTimestamp = 0;
        final MutableLong firedTimestamp = new MutableLong(-1);
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 1024);

        final long deadline = 5 * wheel.tickResolution();
        final long id = wheel.scheduleTimer(deadline);
        assertEquals(wheel.deadline(id), deadline);

        do {
            wheel.poll(
                    controlTimestamp,
                    (timeUnit, now, timerId) ->
                    {
                        assertEquals(id, timerId);
                        firedTimestamp.value = now;
                        return true;
                    },
                    Integer.MAX_VALUE);

            controlTimestamp += wheel.tickResolution();
        }
        while (-1 == firedTimestamp.value);

        // this is the first tick after the timer, so it should be on this edge
        assertEquals(6 * wheel.tickResolution(), firedTimestamp.value);
    }

    @Test
    public void shouldHandleNonZeroStartTime() {
        long controlTimestamp = 100L * RESOLUTION;
        final MutableLong firedTimestamp = new MutableLong(-1);
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 1024);

        final long id = wheel.scheduleTimer(controlTimestamp + (5 * wheel.tickResolution()));

        do {
            wheel.poll(
                    controlTimestamp,
                    (timeUnit, now, timerId) ->
                    {
                        assertEquals(id, timerId);
                        firedTimestamp.value = now;
                        return true;
                    },
                    Integer.MAX_VALUE);

            controlTimestamp += wheel.tickResolution();
        }
        while (-1 == firedTimestamp.value);

        // this is the first tick after the timer, so it should be on this edge
        assertEquals(106 * wheel.tickResolution(), firedTimestamp.value);
    }

    @Test
    public void shouldHandleNanoTimeUnitTimers() {
        long controlTimestamp = 0;
        final MutableLong firedTimestamp = new MutableLong(-1);
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 1024);

        final long id = wheel.scheduleTimer(controlTimestamp + (5 * wheel.tickResolution()) + 1);

        do {
            wheel.poll(
                    controlTimestamp,
                    (timeUnit, now, timerId) ->
                    {
                        assertEquals(id, timerId);
                        firedTimestamp.value = now;
                        return true;
                    },
                    Integer.MAX_VALUE);

            controlTimestamp += wheel.tickResolution();
        }
        while (-1 == firedTimestamp.value);

        // this is the first tick after the timer, so it should be on this edge
        assertEquals(6 * wheel.tickResolution(), firedTimestamp.value);
    }

    @Test
    public void shouldHandleMultipleRounds() {
        long controlTimestamp = 0;
        final MutableLong firedTimestamp = new MutableLong(-1);
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 16);

        final long id = wheel.scheduleTimer(controlTimestamp + (63 * wheel.tickResolution()));

        do {
            wheel.poll(
                    controlTimestamp,
                    (timeUnit, now, timerId) ->
                    {
                        assertEquals(id, timerId);
                        firedTimestamp.value = now;
                        return true;
                    },
                    Integer.MAX_VALUE);

            controlTimestamp += wheel.tickResolution();
        }
        while (-1 == firedTimestamp.value);

        // this is the first tick after the timer, so it should be on this edge
        assertEquals(64 * wheel.tickResolution(), firedTimestamp.value);
    }

    @Test
    public void shouldBeAbleToCancelTimer() {
        long controlTimestamp = 0;
        final MutableLong firedTimestamp = new MutableLong(-1);
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 256);

        final long id = wheel.scheduleTimer(controlTimestamp + (63 * wheel.tickResolution()));

        do {
            wheel.poll(
                    controlTimestamp,
                    (timeUnit, now, timerId) ->
                    {
                        assertEquals(id, timerId);
                        firedTimestamp.value = now;
                        return true;
                    },
                    Integer.MAX_VALUE);

            controlTimestamp += wheel.tickResolution();
        }
        while (-1 == firedTimestamp.value && controlTimestamp < (16 * wheel.tickResolution()));

        assertTrue(wheel.cancelTimer(id));
        assertFalse(wheel.cancelTimer(id));

        do {
            wheel.poll(
                    controlTimestamp,
                    (timeUnit, now, timerId) ->
                    {
                        firedTimestamp.value = now;
                        return true;
                    },
                    Integer.MAX_VALUE);

            controlTimestamp += wheel.tickResolution();
        }
        while (-1 == firedTimestamp.value && controlTimestamp < (128 * wheel.tickResolution()));

        assertEquals(-1L, firedTimestamp.value);
    }

    @Test
    public void shouldHandleExpiringTimersInPreviousTicks() {
        long controlTimestamp = 0;
        final MutableLong firedTimestamp = new MutableLong(-1);
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 256);

        final long id = wheel.scheduleTimer(controlTimestamp + (15 * wheel.tickResolution()));

        final long pollStartTimeNs = 32 * wheel.tickResolution();
        controlTimestamp += pollStartTimeNs;

        do {
            wheel.poll(
                    controlTimestamp,
                    (timeUnit, now, timerId) ->
                    {
                        assertEquals(id, timerId);
                        firedTimestamp.value = now;
                        return true;
                    },
                    Integer.MAX_VALUE);

            if (wheel.currentTickTime() > pollStartTimeNs) {
                controlTimestamp += wheel.tickResolution();
            }
        }
        while (-1 == firedTimestamp.value && controlTimestamp < (128 * wheel.tickResolution()));

        assertEquals(pollStartTimeNs, firedTimestamp.value);
    }

    @Test
    public void shouldHandleMultipleTimersInDifferentTicks() {
        long controlTimestamp = 0;
        final MutableLong firedTimestamp1 = new MutableLong(-1);
        final MutableLong firedTimestamp2 = new MutableLong(-1);
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 256);

        final long id1 = wheel.scheduleTimer(controlTimestamp + (15 * wheel.tickResolution()));
        final long id2 = wheel.scheduleTimer(controlTimestamp + (23 * wheel.tickResolution()));

        do {
            wheel.poll(
                    controlTimestamp,
                    (timeUnit, now, timerId) ->
                    {
                        if (timerId == id1) {
                            firedTimestamp1.value = now;
                        } else if (timerId == id2) {
                            firedTimestamp2.value = now;
                        }

                        return true;
                    },
                    Integer.MAX_VALUE);

            controlTimestamp += wheel.tickResolution();
        }
        while (-1 == firedTimestamp1.value || -1 == firedTimestamp2.value);

        assertEquals(16 * wheel.tickResolution(), firedTimestamp1.value);
        assertEquals(24 * wheel.tickResolution(), firedTimestamp2.value);
    }

    @Test
    public void shouldHandleMultipleTimersInSameTickSameRound() {
        long controlTimestamp = 0;
        final MutableLong firedTimestamp1 = new MutableLong(-1);
        final MutableLong firedTimestamp2 = new MutableLong(-1);
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 8);

        final long id1 = wheel.scheduleTimer(controlTimestamp + (15 * wheel.tickResolution()));
        final long id2 = wheel.scheduleTimer(controlTimestamp + (15 * wheel.tickResolution()));

        do {
            wheel.poll(
                    controlTimestamp,
                    (timeUnit, now, timerId) ->
                    {
                        if (timerId == id1) {
                            firedTimestamp1.value = now;
                        } else if (timerId == id2) {
                            firedTimestamp2.value = now;
                        }

                        return true;
                    },
                    Integer.MAX_VALUE);

            controlTimestamp += wheel.tickResolution();
        }
        while (-1 == firedTimestamp1.value || -1 == firedTimestamp2.value);

        assertEquals(16 * wheel.tickResolution(), firedTimestamp1.value);
        assertEquals(16 * wheel.tickResolution(), firedTimestamp2.value);
    }

    @Test
    public void shouldHandleMultipleTimersInSameTickDifferentRound() {
        long controlTimestamp = 0;
        final MutableLong firedTimestamp1 = new MutableLong(-1);
        final MutableLong firedTimestamp2 = new MutableLong(-1);
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 8);

        final long id1 = wheel.scheduleTimer(controlTimestamp + (15 * wheel.tickResolution()));
        final long id2 = wheel.scheduleTimer(controlTimestamp + (23 * wheel.tickResolution()));

        do {
            wheel.poll(
                    controlTimestamp,
                    (timeUnit, now, timerId) ->
                    {
                        if (timerId == id1) {
                            firedTimestamp1.value = now;
                        } else if (timerId == id2) {
                            firedTimestamp2.value = now;
                        }

                        return true;
                    },
                    Integer.MAX_VALUE);

            controlTimestamp += wheel.tickResolution();
        }
        while (-1 == firedTimestamp1.value || -1 == firedTimestamp2.value);

        assertEquals(16 * wheel.tickResolution(), firedTimestamp1.value);
        assertEquals(24 * wheel.tickResolution(), firedTimestamp2.value);
    }

    @Test
    public void shouldLimitExpiringTimers() {
        long controlTimestamp = 0;
        final MutableLong firedTimestamp1 = new MutableLong(-1);
        final MutableLong firedTimestamp2 = new MutableLong(-1);
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 8);

        final long id1 = wheel.scheduleTimer(controlTimestamp + (15 * wheel.tickResolution()));
        final long id2 = wheel.scheduleTimer(controlTimestamp + (15 * wheel.tickResolution()));

        int numExpired = 0;

        do {
            numExpired += wheel.poll(
                    controlTimestamp,
                    (timeUnit, now, timerId) ->
                    {
                        assertEquals(id1, timerId);
                        firedTimestamp1.value = now;
                        return true;
                    },
                    1);

            controlTimestamp += wheel.tickResolution();
        }
        while (-1 == firedTimestamp1.value && -1 == firedTimestamp2.value);

        assertEquals(1, numExpired);

        do {
            numExpired += wheel.poll(
                    controlTimestamp,
                    (timeUnit, now, timerId) ->
                    {
                        assertEquals(id2, timerId);
                        firedTimestamp2.value = now;
                        return true;
                    },
                    1);

            controlTimestamp += wheel.tickResolution();
        }
        while (-1 == firedTimestamp1.value && -1 == firedTimestamp2.value);

        assertEquals(2, numExpired);

        assertEquals(16 * wheel.tickResolution(), firedTimestamp1.value);
        assertEquals(17 * wheel.tickResolution(), firedTimestamp2.value);
    }

    @Test
    public void shouldHandleFalseReturnToExpireTimerAgain() {
        long controlTimestamp = 0;
        final MutableLong firedTimestamp1 = new MutableLong(-1);
        final MutableLong firedTimestamp2 = new MutableLong(-1);
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 8);

        final long id1 = wheel.scheduleTimer(controlTimestamp + (15 * wheel.tickResolution()));
        final long id2 = wheel.scheduleTimer(controlTimestamp + (15 * wheel.tickResolution()));

        int numExpired = 0;

        do {
            numExpired += wheel.poll(
                    controlTimestamp,
                    (timeUnit, now, timerId) ->
                    {
                        if (timerId == id1) {
                            if (-1 == firedTimestamp1.value) {
                                firedTimestamp1.value = now;
                                return false;
                            }

                            firedTimestamp1.value = now;
                        } else if (timerId == id2) {
                            firedTimestamp2.value = now;
                        }

                        return true;
                    },
                    Integer.MAX_VALUE);

            controlTimestamp += wheel.tickResolution();
        }
        while (-1 == firedTimestamp1.value || -1 == firedTimestamp2.value);

        assertEquals(17 * wheel.tickResolution(), firedTimestamp1.value);
        assertEquals(17 * wheel.tickResolution(), firedTimestamp2.value);
        assertEquals(2, numExpired);
    }

    @Test
    public void shouldCopeWithExceptionFromHandler() {
        long controlTimestamp = 0;
        final MutableLong firedTimestamp1 = new MutableLong(-1);
        final MutableLong firedTimestamp2 = new MutableLong(-1);
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 8);

        final long id1 = wheel.scheduleTimer(controlTimestamp + (15 * wheel.tickResolution()));
        final long id2 = wheel.scheduleTimer(controlTimestamp + (15 * wheel.tickResolution()));

        int numExpired = 0;
        Exception e = null;
        do {
            try {
                numExpired += wheel.poll(
                        controlTimestamp,
                        (timeUnit, now, timerId) ->
                        {
                            if (timerId == id1) {
                                firedTimestamp1.value = now;
                                throw new IllegalStateException();
                            } else if (timerId == id2) {
                                firedTimestamp2.value = now;
                            }

                            return true;
                        },
                        Integer.MAX_VALUE);

                controlTimestamp += wheel.tickResolution();
            } catch (final Exception ex) {
                e = ex;
            }
        }
        while (-1 == firedTimestamp1.value || -1 == firedTimestamp2.value);

        assertEquals(16 * wheel.tickResolution(), firedTimestamp1.value);
        assertEquals(16 * wheel.tickResolution(), firedTimestamp2.value);
        assertEquals(1, numExpired);
        assertEquals(0L, wheel.timerCount());
        assertNotNull(e);
    }

    @Test
    public void shouldBeAbleToIterateOverTimers() {
        final long controlTimestamp = 0;
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 8);
        final long deadline1 = controlTimestamp + (15 * wheel.tickResolution());
        final long deadline2 = controlTimestamp + ((15 + 7) * wheel.tickResolution());

        final long id1 = wheel.scheduleTimer(deadline1);
        final long id2 = wheel.scheduleTimer(deadline2);

        final Long2LongHashMap timerIdByDeadlineMap = new Long2LongHashMap(Long.MIN_VALUE);

        wheel.forEach(timerIdByDeadlineMap::put);

        assertEquals(2, timerIdByDeadlineMap.size());
        assertEquals(id1, timerIdByDeadlineMap.get(deadline1));
        assertEquals(id2, timerIdByDeadlineMap.get(deadline2));
    }

    @Test
    public void shouldClearOutScheduledTimers() {
        final long controlTimestamp = 0;
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 8);
        final long deadline1 = controlTimestamp + (15 * wheel.tickResolution());
        final long deadline2 = controlTimestamp + ((15 + 7) * wheel.tickResolution());

        final long id1 = wheel.scheduleTimer(deadline1);
        final long id2 = wheel.scheduleTimer(deadline2);

        wheel.clear();

        assertEquals(0L, wheel.timerCount());
        assertEquals(DeadlineTimerWheel.NULL_DEADLINE, wheel.deadline(id1));
        assertEquals(DeadlineTimerWheel.NULL_DEADLINE, wheel.deadline(id2));
    }

    @Test
    public void shouldNotAllowResetWhenTimersActive() {
        final long controlTimestamp = 0;
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 8);

        wheel.scheduleTimer(controlTimestamp + 100);
        assertThrows(IllegalStateException.class, () -> wheel.resetStartTime(controlTimestamp + 1));
    }

    @Test
    public void shouldAdvanceWheelToLaterTime() {
        final long startTime = 0;
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, startTime, RESOLUTION, 8);

        wheel.scheduleTimer(startTime + 100000);

        final long currentTickTime = wheel.currentTickTime();
        wheel.currentTickTime(currentTickTime * 5);

        assertEquals(currentTickTime * 6, wheel.currentTickTime());
    }

    @Test
    public void shouldScheduleDeadlineInThePast() {
        long controlTimestamp = 100L * RESOLUTION;
        final MutableLong firedTimestamp = new MutableLong(-1);
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(TIME_UNIT, controlTimestamp, RESOLUTION, 1024);

        final long deadline = controlTimestamp - 3;
        final long id = wheel.scheduleTimer(deadline);

        do {
            wheel.poll(
                    controlTimestamp,
                    (timeUnit, now, timerId) ->
                    {
                        assertEquals(id, timerId);
                        firedTimestamp.value = now;
                        return true;
                    },
                    Integer.MAX_VALUE);

            controlTimestamp += wheel.tickResolution();
        }
        while (-1 == firedTimestamp.value);

        assertTrue(firedTimestamp.value > deadline);
    }

    @Test
    public void shouldExpandTickAllocation() {
        final int tickAllocation = 4;
        final int ticksPerWheel = 8;
        final DeadlineTimerWheel wheel = new DeadlineTimerWheel(
                TIME_UNIT, 0, RESOLUTION, ticksPerWheel, tickAllocation);

        final int timerCount = tickAllocation + 1;
        final long[] timerIds = new long[timerCount];

        for (int i = 0; i < timerCount; i++) {
            timerIds[i] = wheel.scheduleTimer(i + 1L);
        }

        for (int i = 0; i < timerCount; i++) {
            assertEquals(i + 1L, wheel.deadline(timerIds[i]));
        }

        final Long2LongHashMap deadlineByTimerId = new Long2LongHashMap(Long.MIN_VALUE);
        final int expiredCount = wheel.poll(
                timerCount + 1L,
                (timeUnit, now, timerId) ->
                {
                    deadlineByTimerId.put(timerId, now);
                    return true;
                },
                timerCount);

        assertEquals(timerCount, expiredCount);
        assertEquals(timerCount, deadlineByTimerId.size());
    }
}
