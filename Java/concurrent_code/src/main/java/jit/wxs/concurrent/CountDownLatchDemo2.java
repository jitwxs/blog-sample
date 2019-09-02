package jit.wxs.concurrent;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 描述：CountdownLatch 使用实例
 *
 * @author jitwxs
 * @date 2019年08月11日 20:52
 */
public class CountDownLatchDemo2 {
    private ExecutorService executor = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        List<Integer> dataList = IntStream.range(1, 100).boxed().collect(Collectors.toList());
        int size = 10;

        new CountDownLatchDemo2().func(dataList, size);
    }

    /**
     * @param dataList 待处理数据列表
     * @param segment  单个线程处理数据量
     */
    private void func(List<Integer> dataList, int segment) {
        int threadCount = dataList.size() % segment == 0 ? dataList.size() / segment : dataList.size() / segment + 1;
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            List<Integer> subList;
            if (i == threadCount - 1) {
                subList = dataList.subList(i * segment, dataList.size());
            } else {
                subList = dataList.subList(i * segment, (i + 1) * segment);
            }
            executor.execute(new TaskThread(subList, latch));
        }

        try {
            latch.await();
            System.out.println("CountDownLatch complete!");
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class TaskThread implements Runnable {
        private List<Integer> list;
        private CountDownLatch latch;

        TaskThread(List<Integer> list, CountDownLatch latch) {
            this.list = list;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                System.out.println(String.format("Thread-%s: %s", Thread.currentThread().getName(), list));
            } finally {
                latch.countDown();
            }
        }
    }
}
