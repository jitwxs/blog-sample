package com.github.jitwxs.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述：ReentrantLock 基本使用
 * @author jitwxs
 * @date 2019年08月11日 21:21
 */
public class ReentrantLockDemo {
    private Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(demo::func);
        executorService.execute(demo::func);
    }

    public void func() {
        lock.lock();
        try {
            for (int i = 0; i < 10; i++) {
                System.out.print(i + " ");
            }
        } finally {
            lock.unlock();
        }
    }
}
