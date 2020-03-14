package jit.wxs.disruptor.demo.ringbuffer2;

import com.lmax.disruptor.EventFactory;

/**
 * @author jitwxs
 * @date 2020年03月14日 20:19
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
