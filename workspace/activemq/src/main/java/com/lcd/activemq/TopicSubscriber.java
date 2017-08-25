package com.lcd.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;  
  
  
public class TopicSubscriber {   
private static final String USERNAME=ActiveMQConnection.DEFAULT_USER; // 默认的连接用户名
private static final String PASSWORD=ActiveMQConnection.DEFAULT_PASSWORD; // 默认的连接密码
private static final String BROKEURL=ActiveMQConnection.DEFAULT_BROKER_URL; // 默认的连接地址

public static void main(String[] args) {
    ConnectionFactory connectionFactory; // 连接工厂
    Connection connection = null; // 连接
    Session session; // 会话 接受或者发送消息的线程
    Topic topic; // 消息的目的地
    MessageConsumer messageConsumer; // 消息的消费者

    // 实例化连接工厂
    connectionFactory=new ActiveMQConnectionFactory(TopicSubscriber.USERNAME, TopicSubscriber.PASSWORD, TopicSubscriber.BROKEURL);

    try {
        connection=connectionFactory.createConnection();  // 通过连接工厂获取连接
        
        connection.setClientID("1"); //持久订阅需要设置这个订阅id
        connection.start(); // 启动连接
        session=connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE); // 创建Session
        // destination=session.createQueue("FirstQueue1");  // 创建连接的消息队列
        topic =session.createTopic("myTopic");
        //messageConsumer=session.createConsumer(destination); // 创建消息消费者
        
        //这里的121是Subscription Name可以和id不同
        messageConsumer = session.createDurableSubscriber(topic,"我的名字是1"); //持久订阅
        messageConsumer.setMessageListener(new Listener()); // 注册消息监听
    } catch (JMSException e) {
        e.printStackTrace();
    } 
}

  
}  