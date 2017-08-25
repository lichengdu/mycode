package lcd.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import lcd.test.bean.User;

public class Test {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("bean.xml");// 读取bean.xml中的内容
		User u = ctx.getBean("user", User.class);// 创建bean的引用对象
		//ctx.start();
		u.info();
	}
}
