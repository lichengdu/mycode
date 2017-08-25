package com.lcd.activemq;

public class Receiver {

	public void process(String message) throws Exception {

		//Thread.sleep(1000);
		System.out.println(message);
	}

}
