package com.github.jitwxs.sample.aeron.agrona.agent;

import org.agrona.concurrent.Agent;
import org.agrona.concurrent.AgentTerminationException;

public class MyAgentSupportStop implements Agent {
    private final String roleName;
    private int count;

    public MyAgentSupportStop(String roleName, int initialCount) {
        this.roleName = roleName;
        this.count = initialCount;
    }

    @Override
    public int doWork() throws Exception {
        count--;

        if (count < 0) {
            throw new AgentTerminationException("count already less zero, stop execute");
        }

        System.out.printf("[%s][%s] doWork: %s%n", roleName(), Thread.currentThread().getPriority(), count);

        if (count % 2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String roleName() {
        return this.roleName;
    }

    public int getCount() {
        return this.count;
    }
}