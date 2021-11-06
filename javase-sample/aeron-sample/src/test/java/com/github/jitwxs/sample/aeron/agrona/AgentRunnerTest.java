package com.github.jitwxs.sample.aeron.agrona;

import com.github.jitwxs.sample.aeron.agrona.agent.MyAgent;
import com.github.jitwxs.sample.aeron.agrona.agent.MyAgentSupportStop;
import com.github.jitwxs.sample.aeron.agrona.agent.MyErrorHandle;
import com.github.jitwxs.sample.aeron.agrona.agent.MyIdleStrategy;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.CompositeAgent;
import org.agrona.concurrent.IdleStrategy;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.util.concurrent.ThreadFactory;

public class AgentRunnerTest {
    /**
     * 每两次 {@link Agent#doWork()} 之间触发一次 {@link IdleStrategy}，执行 1s 后终止主程序
     */
    @Test
    public void testAgentRunner() throws InterruptedException {
        final IdleStrategy idleStrategy = new MyIdleStrategy();
        final MyErrorHandle errorHandle = new MyErrorHandle();
        final MyAgent agent = new MyAgent("my-agent", 10);

        AgentRunner agentRunner = new AgentRunner(idleStrategy, errorHandle, null, agent);

        AgentRunner.startOnThread(agentRunner);

        Thread.sleep(1000);

        agentRunner.close();
    }

    /**
     * 使用自定义的 {@link ThreadFactory} 创建线程，观察打印日志中的 {@link Thread#getPriority()} 为自定义值
     */
    @Test
    public void testAgentRunnerWithThreadFactory() throws InterruptedException {
        final IdleStrategy idleStrategy = new MyIdleStrategy();
        final MyErrorHandle errorHandle = new MyErrorHandle();
        final MyAgent agent = new MyAgent("my-agent", 10);

        AgentRunner agentRunner = new AgentRunner(idleStrategy, errorHandle, null, agent);

        ThreadFactory myThreadFactory = new ThreadFactoryBuilder()
                .setPriority(10).build();

        AgentRunner.startOnThread(agentRunner, myThreadFactory);

        Thread.sleep(1000);

        agentRunner.close();
    }

    /**
     * 将多个 {@link Agent} 封装成 {@link CompositeAgent}，然后使用同一个线程执行
     */
    @Test
    public void testAgentRunnerWithCompositeAgent() throws InterruptedException {
        final IdleStrategy idleStrategy = new MyIdleStrategy();
        final MyErrorHandle errorHandle = new MyErrorHandle();

        final CompositeAgent agent = new CompositeAgent(
                new MyAgent("my-agent-0", RandomUtils.nextInt(1, 100)),
                new MyAgent("my-agent-1", RandomUtils.nextInt(1, 100)),
                new MyAgent("my-agent-2", RandomUtils.nextInt(1, 100))
        );

        AgentRunner agentRunner = new AgentRunner(idleStrategy, errorHandle, null, agent);

        AgentRunner.startOnThread(agentRunner);

        Thread.sleep(1000);

        agentRunner.close();
    }

    /**
     * 两次 {@link Agent#doWork()} 之间触发一次 {@link IdleStrategy}，当初始值小于 0 后，终止执行
     */
    @Test
    public void testAgentRunnerWithStop() throws InterruptedException {
        final IdleStrategy idleStrategy = new MyIdleStrategy();
        final MyErrorHandle errorHandle = new MyErrorHandle();

        final MyAgentSupportStop agent = new MyAgentSupportStop("my-agent", 10);

        AgentRunner agentRunner = new AgentRunner(idleStrategy, errorHandle, null, agent);

        AgentRunner.startOnThread(agentRunner);

        while (!agentRunner.isClosed()) {
            Thread.sleep(10);
        }
    }
}
