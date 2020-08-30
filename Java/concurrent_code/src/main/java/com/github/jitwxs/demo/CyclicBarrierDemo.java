package com.github.jitwxs.demo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述：CyclicBarrier 基本使用
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(6);
        for (int i = 1; i <= 18; i++) {
            executorService.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "开始等待其他线程");
                try {
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread().getName() + "开始执行业务逻辑，耗时0.5秒");
                    // 工作线程开始处理，这里用Thread.sleep()来模拟业务处理
                    Thread.sleep(500);
                    System.out.println(Thread.currentThread().getName() + "业务逻辑执行完毕");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
    }
}
