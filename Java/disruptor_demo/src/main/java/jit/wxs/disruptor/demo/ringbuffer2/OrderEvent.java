package jit.wxs.disruptor.demo.ringbuffer2;

import lombok.Data;

/**
 * @author jitwxs
 * @date 2020年03月14日 20:14
 */
@Data
public class OrderEvent {
    private String id;

    private double price;

    @Override
    public String toString() {
        return String.format("OrderEvent, id: %s, price: %f", id, price);
    }
}
