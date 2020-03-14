package jit.wxs.disruptor.demo.ringbuffer2;

import com.lmax.disruptor.WorkHandler;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 对于单消费者，既可以实现EventHandler也可以实现WorkHandler
 * 但是对于多消费者，只能实现WorkHandler
 * @author jitwxs
 * @date 2020年03月14日 20:15
 */
public class Consumer implements WorkHandler<OrderEvent> {
    @Getter
    private String consumerName;

    public static AtomicInteger COUNT = new AtomicInteger(0);

    public Consumer(String consumerName) {
        this.consumerName = consumerName;
    }

    @Override
    public void onEvent(OrderEvent event) throws Exception {
        System.out.println(String.format("%s Receive Event: %s", this.consumerName, event.toString()));
        COUNT.incrementAndGet();
    }
}
