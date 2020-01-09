package jit.wxs.helloworld;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

/**
 * @author jitwxs
 * @date 2019年11月28日 23:49
 */
class Main {
    public static void main(String[] args) {
        LongEventFactory factory = new LongEventFactory();
        int ringBufferSize = 1024;

        /*
         * 详细见：https://www.jitwxs.cn/4ae6dc03.html
         * BlockingWaitStrategy：最低效的策略，但是对cpu的消耗是最小的，在各种不同部署环境中能提供更加一致的性能表现。
         * SleepingWaitStrategy：性能和 BlockingWaitStrategy差不多少，cpu消耗也类似，但是其对生产者线程的影响最小，适合用于异步处理数据的场景。
         * YieldingWaitStrategy：性能是最好的，适用于低延迟的场景。在要求极高性能且事件处理线程数小于cpu处理核数时推荐使用此策略。
         * BusySpinWaitStrategy：低延迟，但是对cpu资源的占用较多。
         * PhasedBackoffWaitStrategy：上边几种策略的综合体，延迟大，但是占用cpu资源较少。
         */
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, ringBufferSize, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new YieldingWaitStrategy());
        // 指定消费者
        disruptor.handleEventsWith(new LongEventHandler());
        disruptor.start();

        // 生产者生产数据
        LongEventProducerTranslator producer = new LongEventProducerTranslator(disruptor.getRingBuffer());
        for(long score = 0; score < 100; score++) {
            producer.publish(score);
        }

        disruptor.shutdown();
    }
}
