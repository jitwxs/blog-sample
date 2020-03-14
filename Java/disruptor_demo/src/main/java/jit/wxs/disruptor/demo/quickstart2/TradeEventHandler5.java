package jit.wxs.disruptor.demo.quickstart2;

import com.lmax.disruptor.EventHandler;

/**
 * Event消费者5
 * @author jitwxs
 * @date 2020年03月14日 18:08
 */
public class TradeEventHandler5 implements EventHandler<TradeEvent> {
    @Override
    public void onEvent(TradeEvent event, long sequence, boolean endOfBatch) throws Exception {
        // 消费者5
        event.setT5(System.nanoTime());
        Thread.sleep(1000);
        System.out.println("Handler5: seq: " + sequence + ", event: " + event.toString());
    }
}
