package com.lcd.customer;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lcd.api.GlobalService;


@Controller
public class TestController {
	
	
	@Autowired
	private GlobalService globalService;
	
	@RequestMapping("/testdoPost")
	@ResponseBody
	public String sayDubbo(){
		String str = globalService.doPost(new HashMap<String,String>());
		return str;
	}

}
