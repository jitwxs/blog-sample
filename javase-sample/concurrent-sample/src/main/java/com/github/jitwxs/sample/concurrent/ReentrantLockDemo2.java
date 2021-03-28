package com.github.jitwxs.sample.concurrent;

/**
 * 描述：ReentrantLock 公平锁与非公平锁
 * @author jitwxs
 * @date 2019年08月11日 21:24
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo2 {
    private static CountDownLatch latch;

    public static void main(String[] args) {
        Lock fairLock = new MyReentrantLock(true);
        Lock unFairLock = new MyReentrantLock(false);

        testLock(unFairLock);
    }

    private static void testLock(Lock lock) {
        latch = new CountDownLatch(1);
        for (int i = 0; i < 5; i++) {
            Thread thread = new Worker(lock, latch);
            thread.setName("Thread-" + i);
            thread.start();
        }
        latch.countDown();
    }
}

class Worker extends Thread {
    private Lock lock;
    private CountDownLatch latch;

    public Worker(Lock lock, CountDownLatch latch) {
        this.lock = lock;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 2; i++) {
            lock.lock();
            try {
                System.out.println("Lock by [" + getName() + "], Waiting by " + ((MyReentrantLock) lock).getQueuedThreads());
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}

class MyReentrantLock extends ReentrantLock {
    MyReentrantLock(boolean fair) {
        super(fair);
    }

    @Override
    public Collection<Thread> getQueuedThreads() {
        List<Thread> arrayList = new ArrayList<>(super.getQueuedThreads());
        Collections.reverse(arrayList);
        return arrayList;
    }
}
