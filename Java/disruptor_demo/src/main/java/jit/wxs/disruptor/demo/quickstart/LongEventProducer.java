package jit.wxs.disruptor.demo.quickstart;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * 生产者——老版本写法
 * @author jitwxs
 * @date 2019年11月29日 0:41
 */
class LongEventProducer {
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void publish(ByteBuffer data) {
        long sequence = ringBuffer.next();
        try {
            LongEvent event = ringBuffer.get(sequence);

            // 模拟业务操作，数据加工
            long score = data.getLong(0);
            System.out.println(String.format("LongEventProducer seq = %d, score = %d", sequence, score));
            event.setValue(score);
        } finally {
            ringBuffer.publish(sequence);
        }

    }
}
