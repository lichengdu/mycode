package com.lcd.util.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.lcd.util.exception.MgException;

/**
 * service层传入JSON格式字符串校验与转换成Map集合工具类
 * @author 黄永丰
 * @createtime 2016年9月22日
 * @version 1.0
 */
public class CheckJSONDataUtil
{

	/**
	 * 传入参数JSON格式校验与转换
	 * @author HuangYongFeng
	 * @createtime 2015年7月16日
	 * @param data 传入JSON格式实字符串
	 * @return Map<String,Object> 返回JSON转换成Map数据
	 */
	public static Map<String, Object> checkJSONData(String data)
	{
		if(data == null || "".equals(data.trim()))
			throw new MgException(-1, "传入的参数data不能为空.");
		if("null".equals(data.trim()))
			throw new MgException(-1, "null字符串不是JSON格式.");
		Map<String, Object> dataMap = new HashMap<String,Object>();
		try
		{
			dataMap = JSONObject.parseObject(data.trim());
		}
		catch(Exception e)
		{
			throw new MgException(-1, "解析参数出错,请检查传入参数JSON格式是否正确.");
		}
		return dataMap;
	}
	
}
