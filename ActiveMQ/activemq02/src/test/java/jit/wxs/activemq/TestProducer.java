package jit.wxs.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;

/**
 * @author jitwxs
 * @since 2018/5/8 22:27
 */
public class TestProducer {

    @Test
    public void testProducer() {
        // 1、加载Spring容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext-activemq.xml");
        // 2、获取jmsTemplate对象
        JmsTemplate jmsTemplate = ac.getBean(JmsTemplate.class);
        // 3、获取Destination对象（以Queue模式为例，如果要测试Topic更换下面注释即可）
        Destination destination = (Destination)ac.getBean("queueDestination");
        //Destination destination = (Destination)ac.getBean("topicDestination");
        // 4、发送消息
        jmsTemplate.send(destination, session -> session.createTextMessage("这是Spring与ActiveMQ整合的测试消息"));
    }
}
