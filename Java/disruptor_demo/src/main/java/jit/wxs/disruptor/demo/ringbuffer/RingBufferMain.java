package jit.wxs.disruptor.demo.ringbuffer;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.YieldingWaitStrategy;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.*;

/**
 * 纯 RingBuffer 实现单生产者、单消费者
 * @author jitwxs
 * @date 2020年01月09日 23:59
 */
class RingBufferMain {
    private static int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws InterruptedException {
        UserEventFactory factory = new UserEventFactory();

        // 获取 RingBuffer
        RingBuffer<UserEvent> ringBuffer = RingBuffer.createSingleProducer(factory, BUFFER_SIZE, new YieldingWaitStrategy());
        // 创建 sequence 栅栏，用于协调 RingBuffer 生产者和消费者
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        // 创建Event消费处理器
        BatchEventProcessor<UserEvent> processor = new BatchEventProcessor<>(ringBuffer, sequenceBarrier, new UserEventHandler());
        // 将Event消费处理器的sequence引用注入到ringBuffer中
        ringBuffer.addGatingSequences(processor.getSequence());
        // 启动Event消费处理器
        new Thread(processor).start();

        // 启动单生产者，生产100条Event
        UserEventTranslator translator = new UserEventTranslator(ringBuffer);
        for (int i = 0; i < 100; i++) {
            translator.publish(RandomStringUtils.randomAlphabetic(5), RandomUtils.nextDouble(0, 100), UserEvent.SexEnum.getByCode(RandomUtils.nextInt(0, 2)));
        }

        // 等待消费者消费完毕
        TimeUnit.SECONDS.sleep(2);

        // 通知事件处理器，可以结束，准备销毁资源
        processor.halt();
    }
}
