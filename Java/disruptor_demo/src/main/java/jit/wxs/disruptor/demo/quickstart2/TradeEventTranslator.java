package jit.wxs.disruptor.demo.quickstart2;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;

/**
 * @author jitwxs
 * @date 2020年03月14日 18:03
 */
public class TradeEventTranslator {
    private RingBuffer<TradeEvent> ringBuffer;

    public TradeEventTranslator(RingBuffer<TradeEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public static final EventTranslator<TradeEvent> TRANSLATOR = new EventTranslator<TradeEvent>() {
        @Override
        public void translateTo(TradeEvent event, long sequence) {
            // 模拟业务操作，数据加工
            System.out.println(String.format("TradeEventTranslator Send seq: %d, event: %s", sequence, event.toString()));
        }
    };

    /**
     * 将event放入ringBuffer
     */
    public void publish() {
        ringBuffer.publishEvent(TRANSLATOR);
    }
}
