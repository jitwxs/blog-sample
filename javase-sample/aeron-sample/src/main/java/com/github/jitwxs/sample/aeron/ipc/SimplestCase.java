package com.github.jitwxs.sample.aeron.ipc;

import io.aeron.Aeron;
import io.aeron.Publication;
import io.aeron.Subscription;
import io.aeron.driver.MediaDriver;
import io.aeron.logbuffer.FragmentHandler;
import lombok.extern.slf4j.Slf4j;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

@Slf4j
public class SimplestCase {
    public static void main(String[] args) {
        final String channel = "aeron:ipc";
        final String message = "my message";

        final IdleStrategy idle = new SleepingIdleStrategy();
        final UnsafeBuffer unsafeBuffer = new UnsafeBuffer(ByteBuffer.allocate(256));

        try (MediaDriver driver = MediaDriver.launch();
             Aeron aeron = Aeron.connect();
             Subscription sub = aeron.addSubscription(channel, 10);
             Publication pub = aeron.addPublication(channel, 10)) {
            while (!pub.isConnected()) {
                idle.idle();
            }

            unsafeBuffer.putStringAscii(0, message);
            log.info("sending:{}", message);
            while (pub.offer(unsafeBuffer) < 0) {
                idle.idle();
            }

            FragmentHandler handler = (buffer, offset, length, header) -> log.info("received:{}", buffer.getStringAscii(offset));

            while (sub.poll(handler, 1) <= 0) {
                idle.idle();
            }
        }
    }
}