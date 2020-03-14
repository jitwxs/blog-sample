package jit.wxs.disruptor.demo.quickstart2;

import com.lmax.disruptor.EventFactory;

/**
 * @author jitwxs
 * @date 2020年03月14日 18:02
 */
public class TradeEventFactory implements EventFactory<TradeEvent> {
    @Override
    public TradeEvent newInstance() {
        return new TradeEvent();
    }
}
