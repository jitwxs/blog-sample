package jit.wxs.disruptor.demo.quickstart;

import com.lmax.disruptor.EventFactory;

/**
 * 事件生成工厂
 * @author jitwxs
 * @date 2019年11月28日 23:52
 */
class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
