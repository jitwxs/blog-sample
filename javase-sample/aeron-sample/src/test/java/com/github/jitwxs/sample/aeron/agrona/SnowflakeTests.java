package com.github.jitwxs.sample.aeron.agrona;

import lombok.var;
import org.agrona.concurrent.CachedEpochClock;
import org.agrona.concurrent.EpochClock;
import org.agrona.concurrent.SnowflakeIdGenerator;
import org.agrona.concurrent.SystemEpochClock;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.agrona.concurrent.SnowflakeIdGenerator.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @see SnowflakeIdGenerator
 */
public class SnowflakeTests {
    static Method extractTimestampMethod, extractNodeIdMethod, extractSequenceMethod;

    static {
        try {
            extractTimestampMethod = SnowflakeIdGenerator.class.getDeclaredMethod("extractTimestamp", long.class);
            extractTimestampMethod.setAccessible(true);

            extractNodeIdMethod = SnowflakeIdGenerator.class.getDeclaredMethod("extractNodeId", long.class);
            extractNodeIdMethod.setAccessible(true);

            extractSequenceMethod = SnowflakeIdGenerator.class.getDeclaredMethod("extractSequence", long.class);
            extractSequenceMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldThrowExceptionIfNodeIdBitsIsNegative() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SnowflakeIdGenerator(-3, SEQUENCE_BITS_DEFAULT, 0, 0, SystemEpochClock.INSTANCE));
        assertEquals("must be >= 0: nodeIdBits=-3", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfSequenceBitsIsNegative() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SnowflakeIdGenerator(NODE_ID_BITS_DEFAULT, -1, 0, 0, SystemEpochClock.INSTANCE));
        assertEquals("must be >= 0: sequenceBits=-1", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfTimestampOffsetIsNegative() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new SnowflakeIdGenerator(
                        NODE_ID_BITS_DEFAULT, SEQUENCE_BITS_DEFAULT, 0, -6, SystemEpochClock.INSTANCE));
        assertEquals("must be >= 0: timestampOffsetMs=-6", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfTimestampOffsetIsGreaterThanCurrentTime() {
        final EpochClock clock = () -> 42;

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new SnowflakeIdGenerator(NODE_ID_BITS_DEFAULT, SEQUENCE_BITS_DEFAULT, 0, 256, clock));
        assertEquals("timestampOffsetMs=256 > nowMs=42", exception.getMessage());
    }

    @Test
    public void shouldGenerateId() {
        final var snowflake = new SnowflakeIdGenerator(1L);
        final var nextId = snowflake.nextId();
        assertNotEquals(0L, nextId);
    }

    @Test
    public void shouldInitialiseGenerator() {
        final long nodeId = 7;
        final long timestampOffset = 19;
        final EpochClock clock = new SystemEpochClock();

        final SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(
                NODE_ID_BITS_DEFAULT, SEQUENCE_BITS_DEFAULT, nodeId, timestampOffset, clock);

        assertEquals(nodeId, idGenerator.nodeId());
        assertEquals(timestampOffset, idGenerator.timestampOffsetMs());
    }

    @Test
    public void shouldGetFirstId() throws InvocationTargetException, IllegalAccessException {
        final long nodeId = 7;
        final long timestampOffset = 0;
        final CachedEpochClock clock = new CachedEpochClock();

        final SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(
                NODE_ID_BITS_DEFAULT, SEQUENCE_BITS_DEFAULT, nodeId, timestampOffset, clock);
        clock.advance(1);

        final long id = idGenerator.nextId();

        assertEquals(nodeId, extractNodeIdMethod.invoke(idGenerator, id));
        assertEquals(0L, extractSequenceMethod.invoke(idGenerator, id));
    }

    @Test
    public void shouldIncrementSequence() throws InvocationTargetException, IllegalAccessException {
        final long nodeId = 7;
        final long timestampOffset = 0;
        final CachedEpochClock clock = new CachedEpochClock();

        final SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(
                NODE_ID_BITS_DEFAULT, SEQUENCE_BITS_DEFAULT, nodeId, timestampOffset, clock);
        clock.advance(3);

        final long idOne = idGenerator.nextId();

        assertEquals(clock.time(), extractTimestampMethod.invoke(idGenerator, idOne));
        assertEquals(nodeId, extractNodeIdMethod.invoke(idGenerator, idOne));
        assertEquals(0L, extractSequenceMethod.invoke(idGenerator, idOne));

        final long idTwo = idGenerator.nextId();

        assertEquals(clock.time(), extractTimestampMethod.invoke(idGenerator, idTwo));
        assertEquals(nodeId, extractNodeIdMethod.invoke(idGenerator, idTwo));
        assertEquals(1L, extractSequenceMethod.invoke(idGenerator, idTwo));
    }

    @Test
    public void shouldAdvanceTimestamp() throws InvocationTargetException, IllegalAccessException {
        final long nodeId = 7;
        final long timestampOffset = 0;
        final CachedEpochClock clock = new CachedEpochClock();

        final SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(
                NODE_ID_BITS_DEFAULT, SEQUENCE_BITS_DEFAULT, nodeId, timestampOffset, clock);
        clock.advance(3);

        final long idOne = idGenerator.nextId();

        assertEquals(clock.time(), extractTimestampMethod.invoke(idGenerator, idOne));
        assertEquals(nodeId, extractNodeIdMethod.invoke(idGenerator, idOne));
        assertEquals(0L, extractSequenceMethod.invoke(idGenerator, idOne));

        clock.advance(3);
        final long idTwo = idGenerator.nextId();

        assertEquals(clock.time(), extractTimestampMethod.invoke(idGenerator, idTwo));
        assertEquals(nodeId, extractNodeIdMethod.invoke(idGenerator, idTwo));
        assertEquals(0L, extractSequenceMethod.invoke(idGenerator, idTwo));
    }

    @Test
    public void shouldDetectClockGoingBackwards() {
        final long nodeId = 7;
        final long timestampOffset = 0;
        final CachedEpochClock clock = new CachedEpochClock();

        final SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(
                NODE_ID_BITS_DEFAULT, SEQUENCE_BITS_DEFAULT, nodeId, timestampOffset, clock);
        clock.update(7);

        idGenerator.nextId();

        clock.update(3);
        assertThrows(IllegalStateException.class, idGenerator::nextId);
    }

    @Test
    public void allowsExtractionOfTimeNodeSequence() {
        final var clock = new CachedEpochClock();
        final var timestampOffset = 1609459200000L; //January 1, 2021 12:00:00 AM
        clock.update(SystemEpochClock.INSTANCE.time()); //cached epoch clock is used to control time in the test.
        final var snowflake = new SnowflakeIdGenerator(NODE_ID_BITS_DEFAULT, SEQUENCE_BITS_DEFAULT, 1L, timestampOffset, clock);

        //extract the time, node and sequence
        final var nextId = snowflake.nextId();
        assertEquals(clock.time() - timestampOffset, nextId >>> MAX_NODE_ID_AND_SEQUENCE_BITS);
        assertEquals(1L, (nextId >>> SEQUENCE_BITS_DEFAULT) & snowflake.maxNodeId());
        assertEquals(0L, nextId & snowflake.maxSequence());

        final var nextNextId = snowflake.nextId();
        assertEquals(clock.time() - timestampOffset, nextNextId >>> MAX_NODE_ID_AND_SEQUENCE_BITS);
        assertEquals(1L, (nextNextId >>> SEQUENCE_BITS_DEFAULT) & snowflake.maxNodeId());
        assertEquals(1L, nextNextId & snowflake.maxSequence());
    }
}