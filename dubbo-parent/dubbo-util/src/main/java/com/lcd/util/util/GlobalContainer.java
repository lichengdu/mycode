package com.lcd.util.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class GlobalContainer
{

	public static ClassPathXmlApplicationContext applicationContext = null;

	public static ClassPathXmlApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	public static void setApplicationContext(ClassPathXmlApplicationContext applicationContext)
	{
		GlobalContainer.applicationContext = applicationContext;
	}
}