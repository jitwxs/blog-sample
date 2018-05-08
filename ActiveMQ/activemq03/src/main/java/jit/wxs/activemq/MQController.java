package jit.wxs.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import java.util.Date;

/**
 * @author jitwxs
 * @since 2018/5/8 23:17
 */
@RestController
public class MQController {
    @Autowired
    private MQProducer producer;

    @GetMapping("/queue/{info}")
    public void sendQueue(@PathVariable String info) {
        // 创建一个Queue的Destination，参数为Destination的名称
        Destination destination = new ActiveMQQueue("test-queue");
        // 发送消息。参数1：Destination；参数2：消息内容
        producer.send(destination,info);
    }

    @GetMapping("/delay/{time}/queue/{info}")
    public void sendDelayQueue(@PathVariable Long time, @PathVariable String info) {
        // 创建一个Queue的Destination，参数为Destination的名称
        Destination destination = new ActiveMQQueue("test-queue");
        // 发送消息。参数1：Destination；参数2：消息内容；参数3：延时（ms）
        producer.delaySend(destination,info,time * 1000);
        System.out.println("发送时间：" + new Date());
    }
}