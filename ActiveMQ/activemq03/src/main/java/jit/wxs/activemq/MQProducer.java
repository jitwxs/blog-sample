package jit.wxs.activemq;

import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.TextMessage;

/**
 * ActiveMQ服务提供
 * @author jitwxs
 * @since 2018/5/8 16:56
 */
@Component
public class MQProducer {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    /**
     * 即时发送
     * @param message 内容，一般为JSON
     * @author jitwxs
     * @since 2018/5/8 21:08
     */
    public void send(Destination destination, String message){
        jmsMessagingTemplate.convertAndSend(destination,message);
    }

    /**
     * 延时发送
     * @param message 内容，一般为JSON
     * @param time 时间（单位：ms）
     * @author jitwxs
     * @since 2018/5/8 21:08
     */
    public void delaySend(Destination destination,String message, Long time) {
        // 得到jmsTemplate
        JmsTemplate jmsTemplate = jmsMessagingTemplate.getJmsTemplate();
        // 发送消息
        jmsTemplate.send(destination, session -> {
            TextMessage textMessage = session.createTextMessage(message);
            // 设置延时时间【关键】
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, time);
            return textMessage;
        });
    }
}
