package lcd.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import lcd.test.bean.User;

public class Test {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("bean.xml");// ��ȡbean.xml�е�����
		User u = ctx.getBean("user", User.class);// ����bean�����ö���
		//ctx.start();
		u.info();
	}
}