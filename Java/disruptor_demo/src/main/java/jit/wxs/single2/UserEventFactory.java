package jit.wxs.single2;

import com.lmax.disruptor.EventFactory;

/**
 * 事件生成工厂
 * @author jitwxs
 * @date 2019年11月28日 23:52
 */
class UserEventFactory implements EventFactory<UserEvent> {
    @Override
    public UserEvent newInstance() {
        return new UserEvent();
    }
}
