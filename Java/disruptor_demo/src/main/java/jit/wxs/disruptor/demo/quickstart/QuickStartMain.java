package jit.wxs.disruptor.demo.quickstart;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;


/**
 *  Disruptor 入门程序，单生产者单消费者
 * @author jitwxs
 * @date 2019年11月28日 23:49
 */
class QuickStartMain {
    private static int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        LongEventFactory factory = new LongEventFactory();

        /*
         * 1. 实例化Disruptor对象
         * @params eventFactory event生产工厂
         * @params ringBufferSize 环形队列的容量，2的整数倍
         * @params threadFactory 执行线程池
         * @params producerType 生产者类型，单生产者 or 多生产者
         * @params waitStrategy 当消费者和生产者速率不匹配时，消费者的等待策略，详细见：https://www.jitwxs.cn/4ae6dc03.html
         */
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, BUFFER_SIZE, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new YieldingWaitStrategy());

        // 2. 添加消费者
        disruptor.handleEventsWith(new LongEventHandler());

        // 3. 启动Disruptor
        RingBuffer<LongEvent> ringBuffer = disruptor.start();

        // 4. 生产者生产数据，有两种写法，推荐使用Translator方式
        LongEventTranslator newProducer = new LongEventTranslator(ringBuffer);
//        LongEventProducer oldProducer = new LongEventProducer(ringBuffer);

        // 初始化long长度的ByteBuffer，用于数据传输
        ByteBuffer buffer = ByteBuffer.allocate(8);
        for(long score = 0; score < 100; score++) {
            buffer.putLong(0, score);
            newProducer.publish(buffer);
//            oldProducer.publish(buffer);
        }

        // 5. 停止Disruptor
        disruptor.shutdown();
    }
}
