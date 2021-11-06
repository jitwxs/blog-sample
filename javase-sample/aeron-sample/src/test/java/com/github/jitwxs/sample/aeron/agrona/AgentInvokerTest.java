package com.github.jitwxs.sample.aeron.agrona;

import com.github.jitwxs.sample.aeron.agrona.agent.MyAgent;
import com.github.jitwxs.sample.aeron.agrona.agent.MyErrorHandle;
import org.agrona.concurrent.AgentInvoker;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

public class AgentInvokerTest {
    @Test
    public void testAgentInvoker() {
        final int initialCount = RandomUtils.nextInt();

        final MyErrorHandle errorHandle = new MyErrorHandle();
        final MyAgent agent = new MyAgent("my-agent", initialCount);

        final AgentInvoker agentInvoker = new AgentInvoker(errorHandle, null, agent);

        Assert.assertFalse(agentInvoker.isStarted());
        Assert.assertFalse(agentInvoker.isRunning());

        // 调用 invoke 前，必须先调用 start
        agentInvoker.invoke();
        Assert.assertEquals(initialCount, agent.getCount());

        agentInvoker.start();
        Assert.assertTrue(agentInvoker.isStarted());
        Assert.assertTrue(agentInvoker.isRunning());

        // 调用一次
        agentInvoker.invoke();
        Assert.assertEquals(initialCount - 1, agent.getCount());

        // 再调用一次
        agentInvoker.invoke();
        Assert.assertEquals(initialCount - 2, agent.getCount());

        // 调用 close 终止
        agentInvoker.close();
        Assert.assertTrue(agentInvoker.isClosed());
        Assert.assertFalse(agentInvoker.isRunning());

        // close 以后，再调用，没用了
        agentInvoker.invoke();
        Assert.assertEquals(initialCount - 2, agent.getCount());
    }
}
