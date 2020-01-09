package jit.wxs.helloworld;

import com.lmax.disruptor.EventHandler;

/**
 * 消费者
 * @author jitwxs
 * @date 2019年11月28日 23:46
 */
class LongEventHandler implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        // 从 ringBuffer 中消费数据
        System.out.println("LongEventHandler Receive: " + event.getValue());
    }
}
