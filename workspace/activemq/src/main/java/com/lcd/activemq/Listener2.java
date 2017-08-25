package com.lcd.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class Listener2 implements MessageListener {

	public void onMessage(Message message) {
		try {
            System.out.println("订阅者二收到的消息："+((TextMessage)message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
	}

}
