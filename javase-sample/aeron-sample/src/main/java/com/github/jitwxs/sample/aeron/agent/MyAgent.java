package com.github.jitwxs.sample.aeron.agent;

import org.agrona.concurrent.Agent;

class MyAgent implements Agent {
    private final String roleName;
    private int count;

    public MyAgent(String roleName, int initialCount) {
        this.roleName = roleName;
        this.count = initialCount;
    }

    @Override
    public int doWork() throws Exception {
        count--;

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