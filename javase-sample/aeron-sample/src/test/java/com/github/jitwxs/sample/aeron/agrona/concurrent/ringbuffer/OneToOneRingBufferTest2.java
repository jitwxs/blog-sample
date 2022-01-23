package com.github.jitwxs.sample.aeron.agrona.concurrent.ringbuffer;

import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.ControlledMessageHandler;
import org.agrona.concurrent.MessageHandler;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;
import org.agrona.concurrent.ringbuffer.RingBufferDescriptor;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class OneToOneRingBufferTest2 {
    @Test
    public void testSendAndReceive() {
        final int bufferLength = 4096 + RingBufferDescriptor.TRAILER_LENGTH;
        final UnsafeBuffer internalBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(bufferLength));
        final OneToOneRingBuffer ringBuffer = new OneToOneRingBuffer(internalBuffer);
        final MessageCapture capture = new MessageCapture();

        final String testString = "0123456789";

        final UnsafeBuffer toSend = new UnsafeBuffer(ByteBuffer.allocateDirect(testString.length()));
        toSend.putStringWithoutLengthAscii(0, testString);

        for (int i = 0; i < 10000; i++) {
            for (int k = 0; k < 20; k++) {
                final boolean success = ringBuffer.write(1, toSend, 0, testString.length());
                if (!success) {
                    System.err.println("Failed to write!");
                }
            }
            ringBuffer.read(capture, 40);
        }

        assertEquals(1, capture.receivedStrings.size());
        assertTrue(capture.receivedStrings.contains(testString));
        assertEquals(200000, capture.count);
        assertNotEquals(0, ringBuffer.consumerPosition());
        assertNotEquals(0, ringBuffer.producerPosition());
    }

    static class MessageCapture implements MessageHandler {
        private final HashSet<String> receivedStrings = new HashSet<>();
        private int count = 0;

        @Override
        public void onMessage(int msgTypeId, MutableDirectBuffer buffer, int index, int length) {
            receivedStrings.add(buffer.getStringWithoutLengthAscii(index, length));
            count++;
        }
    }

    static class ControlledMessageCapture implements ControlledMessageHandler {
        private HashSet<String> receivedStrings = new HashSet<>();
        private int count = 0;

        @Override
        public ControlledMessageHandler.Action onMessage(int msgTypeId, MutableDirectBuffer buffer, int index, int length) {
            receivedStrings.add(buffer.getStringWithoutLengthAscii(index, length));
            count++;
            return Action.COMMIT;
        }
    }
}
