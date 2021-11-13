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

package com.github.jitwxs.sample.aeron.agrona.manytoone;

import com.github.jitwxs.sample.aeron.agrona.agent.MyErrorHandle;
import lombok.extern.slf4j.Slf4j;
import org.agrona.concurrent.*;
import org.agrona.concurrent.ringbuffer.ManyToOneRingBuffer;
import org.agrona.concurrent.ringbuffer.RingBufferDescriptor;

import java.nio.ByteBuffer;

@Slf4j
public class StartHere {
    public static void main(String[] args) {
        final int sendCount = 100;
        final int bufferLength = 16384 + RingBufferDescriptor.TRAILER_LENGTH;

        // 构造 ringBuffer
        final UnsafeBuffer unsafeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(bufferLength));
        final ManyToOneRingBuffer ringBuffer = new ManyToOneRingBuffer(unsafeBuffer);

        // 空闲策略
        final IdleStrategy idleStrategySend1 = new BusySpinIdleStrategy();
        final IdleStrategy idleStrategySend2 = new BusySpinIdleStrategy();
        final IdleStrategy idleStrategyReceive = new BusySpinIdleStrategy();

        // 屏障控制
        final ShutdownSignalBarrier barrier = new ShutdownSignalBarrier();

        // 构造 Agent
        final SendAgent1 sendAgent1 = new SendAgent1(ringBuffer, sendCount);
        final SendAgent2 sendAgent2 = new SendAgent2(ringBuffer, sendCount);
        final ReceiveAgent receiveAgent = new ReceiveAgent(ringBuffer, barrier, sendCount);

        // 构造 AgentRunner
        final AgentRunner sendAgentRunner1 = new AgentRunner(idleStrategySend1, new MyErrorHandle(), null, sendAgent1);
        final AgentRunner sendAgentRunner2 = new AgentRunner(idleStrategySend2, new MyErrorHandle(), null, sendAgent2);
        final AgentRunner receiveAgentRunner = new AgentRunner(idleStrategyReceive, new MyErrorHandle(), null, receiveAgent);

        // 启动 Agent
        log.info("starting agent...");
        AgentRunner.startOnThread(sendAgentRunner1);
        AgentRunner.startOnThread(sendAgentRunner2);
        AgentRunner.startOnThread(receiveAgentRunner);

        // 等待最后一条消息被消费完毕
        barrier.await();

        // 关闭资源
        receiveAgentRunner.close();
        sendAgentRunner1.close();
        sendAgentRunner2.close();
    }
}
