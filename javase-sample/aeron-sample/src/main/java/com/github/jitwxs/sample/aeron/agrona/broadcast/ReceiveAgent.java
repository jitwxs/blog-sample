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

import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.AtomicBuffer;
import org.agrona.concurrent.MessageHandler;
import org.agrona.concurrent.broadcast.BroadcastReceiver;
import org.agrona.concurrent.broadcast.CopyBroadcastReceiver;

import java.util.logging.Logger;

public class ReceiveAgent implements Agent, MessageHandler {
    private static final Logger logger = Logger.getLogger(ReceiveAgent.class.getName());

    private final String name;
    private final CopyBroadcastReceiver copyBroadcastReceiver;

    public ReceiveAgent(final AtomicBuffer atomicBuffer, final String name) {
        this.name = name;
        this.copyBroadcastReceiver = new CopyBroadcastReceiver(new BroadcastReceiver(atomicBuffer));
    }

    @Override
    public int doWork() {
        copyBroadcastReceiver.receive(this);
        return 0;
    }

    @Override
    public String roleName() {
        return name;
    }

    @Override
    public void onMessage(int msgTypeId, MutableDirectBuffer buffer, int index, int length) {
        logger.info("Received " + buffer.getInt(index));
    }
}
