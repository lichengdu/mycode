package com.dubbo.demo.facade.impl;

import com.dubbo.demo.facade.IDubboDemoFacade;
import org.junit.Test;

import javax.annotation.Resource;
import javax.inject.Named;

/**
 * Created by xuliugen on 15/12/22.
 */
@Named
public class DubboConsumerDemo extends BaseTest {

    @Resource
    private IDubboDemoFacade dubboDemoFacade;

    @Test
    public void test() {
        System.out.println(dubboDemoFacade.showMsg("Hello Dubbox"));

        try {
            //使线程休眠一段时间，以便于在dubbo-admin上显示消费的情况
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
