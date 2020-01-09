package jit.wxs.helloworld;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

/**
 * 生产者
 * @author jitwxs
 * @date 2019年11月29日 0:41
 */
class LongEventProducerTranslator {
    private final RingBuffer<LongEvent> ringBuffer;

    private static final EventTranslatorOneArg<LongEvent, Long> TRANSLATOR = new EventTranslatorOneArg<LongEvent, Long>() {
        @Override
        public void translateTo(LongEvent event, long sequence, Long score) {
            // 模拟业务操作，数据加工
            System.out.println(String.format("DataEventTranslator seq = %d, score = %d", sequence, score));
            event.setValue(score);
        }
    };

    public LongEventProducerTranslator(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void publish(Long score) {
        //向 ringBuffer 生产数据
        ringBuffer.publishEvent(TRANSLATOR, score);
    }
}
