package jit.wxs.single1;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.YieldingWaitStrategy;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.*;

/**
 * BatchEventProcessor 实现
 * @author jitwxs
 * @date 2020年01月09日 23:59
 */
class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executors = Executors.newFixedThreadPool(4);
        UserEventFactory factory = new UserEventFactory();
        int ringBufferSize = 1024;

        // 获取 RingBuffer
        RingBuffer<UserEvent> ringBuffer = RingBuffer.createSingleProducer(factory, ringBufferSize, new YieldingWaitStrategy());
        // 创建 sequence 栅栏，用于协调 RingBuffer 生产者和消费者
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        // 创建事件处理器
        BatchEventProcessor<UserEvent> processor = new BatchEventProcessor<>(ringBuffer, sequenceBarrier, new UserEventHandler());
        // 将事件处理器的 sequence 引用注入到 ringBuffer 中
        ringBuffer.addGatingSequences(processor.getSequence());

        // 线程池中处理事件处理器
        executors.execute(processor);

        Future<Void> future = executors.submit(() -> {
            for (int i = 0; i < 100; i++) {
                long sequence = ringBuffer.next();

                // 模拟生产业务处理
                handleUserEvent(ringBuffer.get(sequence));

                ringBuffer.publish(sequence);
            }
            return null;
        });
        // 等待生产结果
        future.get();

        // 等待消费者消费完毕
        TimeUnit.MILLISECONDS.sleep(100);

        // 通知事件处理器，可以结束，准备销毁资源
        processor.halt();
        // 准备销毁线程池
        executors.shutdown();
    }

    static UserEvent handleUserEvent(UserEvent event) {
        event.setScore(RandomUtils.nextDouble(0, 100));
        event.setSexEnum(UserEvent.SexEnum.getByCode(RandomUtils.nextInt(0,2)));
        event.setUsername(RandomStringUtils.randomAlphabetic(5));

        return event;
    }
}
