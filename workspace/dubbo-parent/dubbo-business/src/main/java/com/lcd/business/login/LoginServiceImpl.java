package com.lcd.business.login;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.lcd.util.entity.RetStruct;
import com.lcd.util.util.CheckJSONDataUtil;

@Service
public class LoginServiceImpl {
	/**
	 * @interfaceName lcd.global.login.get
	 * @param data
	 * @param sessionid
	 * @return
	 * @throws Exception
	 */
	public String get(String data, String sessionid) throws Exception{
		Map<String, Object> dataMap = CheckJSONDataUtil.checkJSONData(data);
		//你的业务逻辑
		return new RetStruct(dataMap.get("name")+"，你请求后台接口成功了!sessionid:"+sessionid).toString();
	}

}
