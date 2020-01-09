package jit.wxs.single2;

import com.lmax.disruptor.WorkHandler;

/**
 * @author jitwxs
 * @date 2020年01月09日 23:59
 */
public class UserEventHandler implements WorkHandler<UserEvent> {

    @Override
    public void onEvent(UserEvent event) throws Exception {
        // 从 ringBuffer 中消费数据
        System.out.println("UserEventHandler Receive: " + event.toString());
    }
}
