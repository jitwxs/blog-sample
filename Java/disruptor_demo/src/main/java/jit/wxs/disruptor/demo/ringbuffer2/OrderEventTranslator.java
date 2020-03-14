package jit.wxs.disruptor.demo.ringbuffer2;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;

/**
 * @author jitwxs
 * @date 2020年03月14日 21:06
 */
public class OrderEventTranslator {
    private RingBuffer<OrderEvent> ringBuffer;
    private String producerName;

    private EventTranslatorTwoArg<OrderEvent, String, Double> translator = new EventTranslatorTwoArg<OrderEvent, String, Double>() {
        @Override
        public void translateTo(OrderEvent event, long sequence, String id, Double price) {
            event.setId(id);
            event.setPrice(price);

            System.out.println(String.format("%s Send Event: %s", producerName, event));
        }
    };

    public OrderEventTranslator(RingBuffer<OrderEvent> ringBuffer, String producerName) {
        this.ringBuffer = ringBuffer;
        this.producerName = producerName;
    }

    public void publish(String id, double price) {
        this.ringBuffer.publishEvent(translator, id, price);
    }
}
