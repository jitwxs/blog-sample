package jit.wxs.disruptor.demo.ringbuffer2;

import com.lmax.disruptor.*;
import org.apache.commons.lang3.RandomUtils;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 纯 RingBuffer 实现多生产者、多消费者
 * @author jitwxs
 * @date 2020年03月14日 20:19
 */
public class RingBuffer2Main {
    private static int BUFFER_SIZE = 1024, PRODUCER_SIZE = 10, CONSUMER_SIZE = 10;

    public static void main(String[] args) throws InterruptedException {
        OrderEventFactory factory = new OrderEventFactory();

        // 创建多生产者的RingBuffer
        RingBuffer<OrderEvent> ringBuffer = RingBuffer.createMultiProducer(factory, BUFFER_SIZE, new YieldingWaitStrategy());
        // 创建sequence栅栏，用于协调RingBuffer生产者和消费者
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        // 初始化消费者列表
        Consumer[] consumers = buildConsumers();

        /*
         * 创建 workPool
         * @params ringBuffer 处理数据的环形队列
         * @params sequenceBarrier sequence栅栏，用于协调RingBuffer生产者和消费者
         * @params exceptionHandler 出现异常时的处理策略，此处直接使用lmax提供的策略
         * @params workHandlers 消费者列表，需要实现WorkHandler接口
         */
        WorkerPool<OrderEvent> workerPool = new WorkerPool<>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), consumers);
        /*
         * 根据官方的 Disruptor 流程图（图片链接给了两个，随便某一个都可以看），我们知道：
         * - 对于生产者来说，不关心生产者的数量，sequence 都共用一个， 这是因为不同的生产者生产的Event都会放入到 ringBuffer 中，
         *   它们共用同一个 sequence 即可，彼此之间通过 CAS 操作保证线程安全即可。
         * - 但是对于消费者来说，每个消费者都会有一个 sequence，。这是因为每个消费者的消费进度是不一样的，各自的 sequence 标识各自的消费进度。
         *   消费者要通过 sequenceBarrier 作为和 ringBuffer 的一个中间代理， 来操作 ringBuffer 上的 sequence。
         * - 生产者判断能否继续生产的判断，需要取 Min(consumerList's sequence)，即根据当前 ringBuffer 的 sequence，和所有消费者的 sequence 的 min 做比较。
         * 因此我们需要将消费者们的 sequence 交给 ringBuffer， 对于多消费者来说，消费者们都被 WorkerPool 管理了，那么将 workerPool.getWorkerSequences() 交给 ringBuffer 就可以了。
         *
         * https://github.com/LMAX-Exchange/disruptor/wiki/images/Models.png
         * https://www.jitwxs.cn/images/posts/20191216221318838.png
         */
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

        // 启动workPool
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        workerPool.start(executor);

        CountDownLatch mainLatch = new CountDownLatch(1);
        // 创建生产者列表，生产数据
        for(int i = 0; i < PRODUCER_SIZE; i++) {
            OrderEventTranslator translator = new OrderEventTranslator(ringBuffer, String.format("Producer-%d", i + 1));
            new Thread(() -> {
                try {
                    mainLatch.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 每个生产者生产100条数据
                for(int j = 0; j < 100; j++) {
                    translator.publish(UUID.randomUUID().toString(), RandomUtils.nextDouble(1, 100));
                }
            }).start();
        }

        // 等待生产者的线程都创建完毕
        TimeUnit.SECONDS.sleep(2);
        mainLatch.countDown();

        // 预留时间让消费者消费
        TimeUnit.SECONDS.sleep(4);
        System.out.println("Total consumer count :" + Consumer.COUNT.get());

        // 回收资源
        workerPool.halt();
        executor.shutdown();
    }

    private static Consumer[] buildConsumers() {
        Consumer[] consumers = new Consumer[CONSUMER_SIZE];
        for(int i = 0; i < CONSUMER_SIZE; i++) {
            consumers[i] = new Consumer(String.format("Consumer-%d", i + 1));
        }
        return consumers;
    }
}
