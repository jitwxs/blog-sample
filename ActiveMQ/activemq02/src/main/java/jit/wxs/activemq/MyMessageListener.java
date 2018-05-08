package jit.wxs.activemq;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author jitwxs
 * @since 2018/5/8 22:30
 */
public class MyMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage)message;
            System.out.println("收到消息：" + textMessage.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
