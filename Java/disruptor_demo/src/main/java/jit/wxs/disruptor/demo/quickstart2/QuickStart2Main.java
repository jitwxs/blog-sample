package jit.wxs.disruptor.demo.quickstart2;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

/**
 * Disruptor 入门程序，单生产者单消费者，消费者多边形消费
 * @author jitwxs
 * @date 2020年03月14日 18:01
 */
public class QuickStart2Main {
    private static int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        TradeEventFactory factory = new TradeEventFactory();

        Disruptor<TradeEvent> disruptor = new Disruptor<>(factory, BUFFER_SIZE, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new YieldingWaitStrategy());

//        pipeline(disruptor);
//        parallel(disruptor);
//        rhombus(disruptor);
//        hexagon(disruptor);

        // 生产数据
        RingBuffer<TradeEvent> ringBuffer = disruptor.start();
        TradeEventTranslator translator = new TradeEventTranslator(ringBuffer);
        for(int i = 0; i < 3; i++) {
            translator.publish();
        }

        // 回收资源
        disruptor.shutdown();
    }

    /**
     * 串行消费，可以观察到，t1、t2、t3升序
     *
     * 流程图：Producer -> Handler1 -> Handler2 -> Handler3 -> Close
     */
    private static void pipeline(Disruptor<TradeEvent> disruptor) {
        disruptor
                .handleEventsWith(new TradeEventHandler1())
                .handleEventsWith(new TradeEventHandler2())
                .handleEventsWith(new TradeEventHandler3());
    }

    /**
     * 并行消费，可以观察到，t1、t2、t3非升序
     *
     *                    |-> Handler1 ->|
     * 流程图：Producer -> |-> Handler2 ->| -> Close
     *                    |-> Handler3 ->|
     */
    private static void parallel(Disruptor<TradeEvent> disruptor) {
        disruptor.handleEventsWith(new TradeEventHandler1(), new TradeEventHandler2(), new TradeEventHandler3());
    }

    /**
     * 菱形消费，可以观察到，t3永远大于t1、t2，但t1、t2之间的顺序非升序
     *
     *                    |-> Handler1 ->|
     * 流程图：Producer -> |              | -> Handler3 -> Close
     *                    |-> Handler2 ->|
     */
    private static void rhombus(Disruptor<TradeEvent> disruptor) {
        disruptor
                .handleEventsWith(new TradeEventHandler1(), new TradeEventHandler2())
                .handleEventsWith(new TradeEventHandler3());
    }

    /**
     * 六边形消费，可以观察到，t1 总是最小，t2 总是小于 t3，t4 总是小于 t5，t6 总是最大
     *
     *                                |-> Handler2 -> Handler3 ->|
     * 流程图：Producer -> Handler1 -> |                          | -> Handler6 -> Close
     *                                |-> Handler4 -> Handler5 ->|
     */
    private static void hexagon(Disruptor<TradeEvent> disruptor) {
        TradeEventHandler1 h1 = new TradeEventHandler1();
        TradeEventHandler2 h2 = new TradeEventHandler2();
        TradeEventHandler3 h3 = new TradeEventHandler3();
        TradeEventHandler4 h4 = new TradeEventHandler4();
        TradeEventHandler5 h5 = new TradeEventHandler5();
        TradeEventHandler6 h6 = new TradeEventHandler6();

        disruptor.handleEventsWith(h1);
        disruptor.after(h1).handleEventsWith(h2, h4);
        disruptor.after(h2).handleEventsWith(h3);
        disruptor.after(h4).handleEventsWith(h5);
        disruptor.after(h3, h5).handleEventsWith(h6);
    }
}
