package jit.wxs.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 描述：CountdownLatch 基本使用
 *
 * @author jitwxs
 * @date 2019年08月11日 20:52
 */
public class CountDownLatchDemo {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        executor.execute(new TaskThread("你", latch));
        executor.execute(new TaskThread("弟弟", latch));

        try {
            // 麻麻等待你们
            latch.await();
            // 开始炒菜
            System.out.println("麻麻开始炒菜了...");

            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class TaskThread implements Runnable {
        private String name;
        private CountDownLatch latch;

        TaskThread(String name, CountDownLatch latch) {
            this.name = name;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                System.out.println(name + "开始干活了...");
                TimeUnit.SECONDS.sleep(2); // 模拟干活
                System.out.println(name + "活干了...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 通知麻麻
                latch.countDown();
            }
        }
    }
}
