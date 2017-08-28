package com.dubbo.demo.facade.impl;

import com.dubbo.demo.facade.IDubboDemoFacade;
import org.springframework.stereotype.Service;

/**
 * Created by liugen.xu on 15/9/1.
 */
@Service("dubboDemoFacade")
public class DubboDemoFacade implements IDubboDemoFacade {

    public String showMsg(String msg) {
        StringBuilder sb = new StringBuilder("The msg is:");
        sb.append(msg);
        System.out.println(sb.toString());
        return sb.toString();
    }
}
