package com.lcd.activemq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * 普通消息
 * @author lcd
 *
 */
public class TestApp {

	static ApplicationContext ctx = new ClassPathXmlApplicationContext(
			"applicationContext.xml");

	public static void main(String[] args) throws InterruptedException {

		Sender sender = (Sender) ctx.getBean("producer");
		int i = 10;
		while (i<10) {
			Thread.sleep(1000);
			sender.sender("消息:"+i);
			i++;
		}
	}

}
