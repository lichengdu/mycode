package com.lcd.provider;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lcd.util.log.ILogUtil;
import com.lcd.util.util.GlobalContainer;
public class Provider {
	public static void main(String[] args) throws Exception {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });
		GlobalContainer.setApplicationContext(context);
		context.start();
		ILogUtil.info("启动dubbo服务器成功!");
		System.in.read(); // 为保证服务一直开着，利用输入流的阻塞来模拟

	}
}
