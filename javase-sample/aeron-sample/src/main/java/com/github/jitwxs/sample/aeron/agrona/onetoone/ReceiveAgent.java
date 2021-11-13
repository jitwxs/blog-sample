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

package com.github.jitwxs.sample.aeron.agrona.onetoone;

import lombok.extern.slf4j.Slf4j;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.ShutdownSignalBarrier;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

@Slf4j
public class ReceiveAgent implements Agent {
    private final ShutdownSignalBarrier barrier;
    private final OneToOneRingBuffer ringBuffer;
    private final int sendCount;

    public ReceiveAgent(final OneToOneRingBuffer ringBuffer, ShutdownSignalBarrier barrier, int sendCount) {
        this.ringBuffer = ringBuffer;
        this.barrier = barrier;
        this.sendCount = sendCount;
    }

    @Override
    public int doWork() throws Exception {
        ringBuffer.read(this::handler);
        return 0;
    }

    private void handler(int messageType, DirectBuffer buffer, int offset, int length) {
        final int lastValue = buffer.getInt(offset);

        if (lastValue == sendCount) {
            log.info("received: {}", lastValue);
            // 达到消息上限时，通知 barrier
            barrier.signal();
        }
    }

    @Override
    public String roleName() {
        return "receiver";
    }
}
