package jit.wxs.disruptor.demo.quickstart2;

import com.lmax.disruptor.EventHandler;

/**
 * Event消费者4
 * @author jitwxs
 * @date 2020年03月14日 18:08
 */
public class TradeEventHandler4 implements EventHandler<TradeEvent> {
    @Override
    public void onEvent(TradeEvent event, long sequence, boolean endOfBatch) throws Exception {
        // 消费者4
        event.setT4(System.nanoTime());
        Thread.sleep(1000);
        System.out.println("Handler4: seq: " + sequence + ", event: " + event.toString());
    }
}
