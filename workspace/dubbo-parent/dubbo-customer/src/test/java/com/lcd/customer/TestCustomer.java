package com.lcd.customer;


import java.util.HashMap;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lcd.api.GlobalService;

public class TestCustomer {
	public static void main(String[] args) throws Exception {

		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "servlet-context.xml" });
		context.start();

		GlobalService globalService = (GlobalService) context.getBean("testService");
		/*String hello = testService.say("lichengdu");
		String dubbo = testService.testSayDubbo();
		System.out.println(hello);
		System.out.println(dubbo);*/
		globalService.doPost(new HashMap<String,String>());
		System.in.read();
	}

}
