package com.lcd.util.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RetStruct implements Serializable
{
	private static final long serialVersionUID = -1668080042864766979L;
	
	private Map<String, Object> retMap;

	public RetStruct()
	{
		retMap = new HashMap<String, Object>();
		retMap.put("ret", 0);
		retMap.put("message", "");
	}

	public RetStruct(String message)
	{
		retMap = new HashMap<String, Object>();
		retMap.put("ret", 0);
		retMap.put("message", message);
	}

	public RetStruct(String message, String key, Object value)
	{
		this(message);
		retMap.put(key, value);
	}

	public RetStruct(String message, Map<String, Object> map)
	{
		retMap = map;
		retMap.put("ret", 0);
		retMap.put("message", message);
	}

	public RetStruct(int ret, String message)
	{
		retMap = new HashMap<String, Object>();
		retMap.put("ret", ret);
		retMap.put("message", message);
	}
	
	public RetStruct(int ret, String message,String mYlogMessage)
	{
		retMap = new HashMap<String, Object>();
		retMap.put("ret", ret);
		retMap.put("message", message);
		retMap.put("mYlogMessage", mYlogMessage);
	}

	public RetStruct setMessage(String message)
	{
		retMap.put("message", message);
		return this;
	}

	public RetStruct put(String key, Object value)
	{
		retMap.put(key, value);
		return this;
	}

	public RetStruct put(Map<String, Object> map)
	{
		retMap.putAll(map);
		return this;
	}

	@Override
	public String toString()
	{
//		String result = JsonUtil.toJSON(JsonUtil.dateFormat, retMap);
//		String result = JsonUtil.toJSON(retMap);
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String result = gson.toJson(retMap);
		return result;
	}
}
