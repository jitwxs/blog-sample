package jit.wxs.single1;

import com.lmax.disruptor.EventHandler;

/**
 * @author jitwxs
 * @date 2020年01月09日 23:59
 */
public class UserEventHandler implements EventHandler<UserEvent> {
    @Override
    public void onEvent(UserEvent event, long sequence, boolean endOfBatch) throws Exception {
        // 从 ringBuffer 中消费数据
        System.out.println("UserEventHandler Receive: " + event.toString());
    }
}
