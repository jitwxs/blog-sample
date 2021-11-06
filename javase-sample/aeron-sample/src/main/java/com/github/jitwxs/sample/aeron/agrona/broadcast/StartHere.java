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

import org.agrona.concurrent.*;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import static org.agrona.concurrent.broadcast.BroadcastBufferDescriptor.TRAILER_LENGTH;

public class StartHere {
    private static final Logger logger = Logger.getLogger(StartHere.class.getName());

    public static void main(String[] args) {
        final int sendCount = 1000;
        final int bufferLength = 65536 + TRAILER_LENGTH;
        final UnsafeBuffer unsafeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(bufferLength));
        //swap for BusySpinIdleStrategy to see the unable to keep up with broadcast errors
        final IdleStrategy idleStrategySend = new SleepingMillisIdleStrategy();
        final IdleStrategy idleStrategyReceive2 = new BusySpinIdleStrategy();
        final IdleStrategy idleStrategyReceive1 = new BusySpinIdleStrategy();
        final ShutdownSignalBarrier barrier = new ShutdownSignalBarrier();

        //construct the agents
        final ReceiveAgent receiveAgent1 = new ReceiveAgent(unsafeBuffer, "receiveAgent1");
        final ReceiveAgent receiveAgent2 = new ReceiveAgent(unsafeBuffer, "receiveAgent2");
        final SendAgent sendAgent = new SendAgent(unsafeBuffer, barrier, sendCount);

        //construct agent runners
        final AgentRunner sendAgentRunner = new AgentRunner(idleStrategySend,
                Throwable::printStackTrace, null, sendAgent);
        final AgentRunner receiveAgentRunner1 = new AgentRunner(idleStrategyReceive1,
                Throwable::printStackTrace, null, receiveAgent1);
        final AgentRunner receiveAgentRunner2 = new AgentRunner(idleStrategyReceive2,
                Throwable::printStackTrace, null, receiveAgent2);

        logger.info("starting");

        //start the runners
        AgentRunner.startOnThread(sendAgentRunner);
        AgentRunner.startOnThread(receiveAgentRunner1);
        AgentRunner.startOnThread(receiveAgentRunner2);

        //wait for the final item to be received before closing
        barrier.await();

        //close the resources
        receiveAgentRunner1.close();
        sendAgentRunner.close();
        receiveAgentRunner2.close();
    }
}
