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

import org.agrona.concurrent.Agent;
import org.agrona.concurrent.AtomicBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

public class SendAgent implements Agent {
    private final int sendCount;
    private final OneToOneRingBuffer ringBuffer;
    private int currentCountItem = 1;

    public SendAgent(final OneToOneRingBuffer ringBuffer, int sendCount) {
        this.ringBuffer = ringBuffer;
        this.sendCount = sendCount;
    }

    @Override
    public int doWork() {
        // 当达到发送上限时，不再发送
        if (currentCountItem > sendCount) {
            return 0;
        }

        // 通过 tryClaim 方式写入数据
        int claimIndex = ringBuffer.tryClaim(1, Integer.BYTES);
        if (claimIndex > 0) {
            currentCountItem += 1;
            final AtomicBuffer buffer = ringBuffer.buffer();
            buffer.putInt(claimIndex, currentCountItem);
            ringBuffer.commit(claimIndex);
        }
        return 0;
    }

    @Override
    public String roleName() {
        return "sender";
    }
}
