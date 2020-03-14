package jit.wxs.disruptor.demo.quickstart;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * 生产者——新版推荐写法
 * @author jitwxs
 * @date 2019年11月29日 0:41
 */
class LongEventTranslator {
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventTranslator(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * 此处使用一个参数的Translator类，你可以根据自己的需求，选择没有参数、一个参数、两个参数、三个参数、可变参数的Translator类
     */
    private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR = new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
        @Override
        public void translateTo(LongEvent event, long sequence, ByteBuffer data) {
            long score = data.getLong(0);
            // 模拟业务操作，数据加工
            System.out.println(String.format("LongEventProducerTranslator seq = %d, score = %d", sequence, score));
            event.setValue(score);
        }
    };

    /**
     * 将event放入ringBuffer
     */
    public void publish(ByteBuffer data) {
        //向 ringBuffer 生产数据
        ringBuffer.publishEvent(TRANSLATOR, data);
    }
}
