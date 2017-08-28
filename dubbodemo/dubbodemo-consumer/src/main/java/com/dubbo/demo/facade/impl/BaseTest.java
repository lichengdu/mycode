package com.dubbo.demo.facade.impl;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试Demo的时候，引入spring的配置文件
 * Created by liugen.xu on 15/9/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:consumer-root.xml"})
public class BaseTest {

}
