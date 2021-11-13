/*
 * Copyright 2019-2021 Shaun Laurens.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jitwxs.sample.aeron.agrona.broadcast;

import lombok.extern.slf4j.Slf4j;
import org.agrona.ExpandableArrayBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.AtomicBuffer;
import org.agrona.concurrent.ShutdownSignalBarrier;
import org.agrona.concurrent.broadcast.BroadcastTransmitter;

@Slf4j
public class SendAgent implements Agent {
    private final ShutdownSignalBarrier barrier;
    private final int sendCount;
    private final BroadcastTransmitter transmitter;
    private int lastSend;
    private boolean completed;

    private final MutableDirectBuffer msgBuffer = new ExpandableArrayBuffer();

    public SendAgent(final AtomicBuffer buffer, ShutdownSignalBarrier barrier, int sendCount) {
        this.barrier = barrier;
        this.sendCount = sendCount;
        this.transmitter = new BroadcastTransmitter(buffer);
        this.lastSend = 0;
        this.completed = false;
    }

    @Override
    public int doWork() {
        if (completed) {
            return 0;
        }

        if (lastSend == sendCount) {
            log.info("completed send: {}", lastSend);
            barrier.signal();
            completed = true;
            return 0;
        }

        msgBuffer.putInt(0, lastSend);
        transmitter.transmit(1, msgBuffer, 0, Integer.BYTES);

        lastSend++;

        return 0;
    }

    @Override
    public String roleName() {
        return "sender";
    }
}
