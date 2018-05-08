package jit.wxs.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

/**
 * @author jitwxs
 * @since 2018/5/8 22:14
 */
public class TestTopic {
    @Test
    public void testTopicProducer() throws Exception {
        //1、创建一个连接工厂对象，指定服务IP和端口
        // 这里的端口不是8161，而是ActiveMQ服务端口，默认为61616
        String brokerURL = "tcp://192.168.30.188:61616";
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
        //2、使用工厂对象创建Collection对象
        Connection connection = connectionFactory.createConnection();
        //3、开启连接，调用Collection.start()
        connection.start();
        //4、创建Session对象
        // 参数1：是否开启事务，如果为true，参数2无效
        // 参数2：应答模式，自动应答/手动应答，自动应答即可
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、使用Session对象创建Destination对象（queue或topic）
        Topic topic = session.createTopic("test-topic");
        //6、使用Session对象创建一个Producer对象
        MessageProducer producer = session.createProducer(topic);
        //7、创建一个Message对象
        TextMessage message = session.createTextMessage("It just a test topic...");
        //8、发送消息
        producer.send(message);
        //9、关闭资源
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testTopicConsumer() throws Exception {
        //1、创建一个连接工厂对象，指定服务IP和端口
        // 这里的端口不是8161，而是ActiveMQ服务端口，默认为61616
        String brokerURL = "tcp://192.168.30.188:61616";
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
        //2、使用工厂对象创建Collection对象
        Connection connection = connectionFactory.createConnection();
        //3、开启连接，调用Collection.start()
        connection.start();
        //4、创建Session对象
        // 参数1：是否开启事务，如果为true，参数2无效
        // 参数2：应答模式，自动应答/手动应答，自动应答即可
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、使用Session对象创建Destination对象（queue或topic）
        Topic topic = session.createTopic("test-topic");
        //6、使用Session对象创建一个Consumer对象
        MessageConsumer consumer = session.createConsumer(topic);
        //7、接收消息
        consumer.setMessageListener(message -> {
            try {
                TextMessage msg = (TextMessage) message;
                System.out.println("接收到消息：" + msg.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
        //阻塞程序，避免结束
        System.in.read();
        //8、关闭资源
        consumer.close();
        session.close();
        connection.close();
    }
}
