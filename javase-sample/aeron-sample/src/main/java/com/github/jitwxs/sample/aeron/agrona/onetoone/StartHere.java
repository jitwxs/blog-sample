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

import com.github.jitwxs.sample.aeron.agrona.agent.MyErrorHandle;
import lombok.extern.slf4j.Slf4j;
import org.agrona.concurrent.*;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;
import org.agrona.concurrent.ringbuffer.RingBufferDescriptor;

import java.nio.ByteBuffer;

@Slf4j
public class StartHere {
    public static void main(String[] args) {
        final int sendCount = 18_000_000;
        final int bufferLength = 16384 + RingBufferDescriptor.TRAILER_LENGTH;

        // 构造 ringBuffer
        final UnsafeBuffer unsafeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(bufferLength));
        final OneToOneRingBuffer ringBuffer = new OneToOneRingBuffer(unsafeBuffer);

        // 空闲策略
        final IdleStrategy idleStrategySend = new BusySpinIdleStrategy();
        final IdleStrategy idleStrategyReceive = new BusySpinIdleStrategy();

        // 屏障控制
        final ShutdownSignalBarrier barrier = new ShutdownSignalBarrier();

        // 构造 Agent
        final SendAgent sendAgent = new SendAgent(ringBuffer, sendCount);
        final ReceiveAgent receiveAgent = new ReceiveAgent(ringBuffer, barrier, sendCount);

        // 构造 AgentRunner
        final AgentRunner sendAgentRunner = new AgentRunner(idleStrategySend, new MyErrorHandle(), null, sendAgent);
        final AgentRunner receiveAgentRunner = new AgentRunner(idleStrategyReceive, new MyErrorHandle(), null, receiveAgent);

        // 启动 Agent
        log.info("starting agent...");
        AgentRunner.startOnThread(sendAgentRunner);
        AgentRunner.startOnThread(receiveAgentRunner);

        //等待最后一条消息被消费完毕
        barrier.await();

        // 关闭资源
        receiveAgentRunner.close();
        sendAgentRunner.close();
    }
}
