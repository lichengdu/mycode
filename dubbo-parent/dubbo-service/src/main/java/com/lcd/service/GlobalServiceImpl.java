package com.lcd.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lcd.api.GlobalService;
import com.lcd.util.service.ProviderService;


public class GlobalServiceImpl implements GlobalService{

	
	@Autowired
	private ProviderService providerService;
	public String doPost(Map<String, String> map) {
		//统一入口调用provider进行分发接口
		return providerService.doPost(map);
	}

}
