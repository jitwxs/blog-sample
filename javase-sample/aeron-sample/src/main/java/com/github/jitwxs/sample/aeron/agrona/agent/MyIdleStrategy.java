package com.github.jitwxs.sample.aeron.agrona.agent;

import org.agrona.concurrent.IdleStrategy;

/**
 * 自定义空闲策略
 */
public class MyIdleStrategy implements IdleStrategy {
    @Override
    public void idle(int i) {
        if (i <= 0) {
            idle();
        }
    }

    @Override
    public void idle() {
        try {
            System.out.println("MyIdleStrategy trigger idle...");
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reset() {
    }
}