package jit.wxs.disruptor.demo.quickstart2;

import lombok.Data;

/**
 * 事件对象
 * @author jitwxs
 * @date 2020年03月14日 18:01
 */
@Data
public class TradeEvent {
    private long t1;

    private long t2;

    private long t3;

    private long t4;

    private long t5;

    private long t6;

    @Override
    public String toString() {
        return String.format("t1: %d, t2: %d, t3: %d, t4: %d, t5: %d, t6: %d", t1, t2, t3, t4, t5, t6);
    }
}
