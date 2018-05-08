package jit.wxs.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author jitwxs
 * @since 2018/5/8 23:23
 */
@Component
public class TestQueueConsumer {

    // 值为要接收的destination的名称
    @JmsListener(destination = "test-queue")
    public void receiveQueue(String text) {
        System.out.println("接收到消息:" + text);
        System.out.println("接收时间：" + new Date());
    }
}
