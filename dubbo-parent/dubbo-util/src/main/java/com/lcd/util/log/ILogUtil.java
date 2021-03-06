package com.lcd.util.log;

import com.alibaba.fastjson.JSONObject;

/**
 * 用来打印日志 信息工具类
 * @author 黄永丰
 * @createtime 2016年5月18日
 * @version 1.0
 */
public class ILogUtil implements ILog
{

	
	/**
	 * 记录日志信息
	 * @author 黄永丰
	 * @createtime 2016年5月18日
	 * @param logMessage 传入参数数据
	 */
	public static void info(String logMessage)
	{
		log.info(logMessage);
	}
	
	/**
	 * 记录日志信息
	 * @author 黄永丰
	 * @createtime 2016年5月18日
	 * @param logMessage 传入参数数据
	 */
	public static void error(String logMessage)
	{
		log.error(logMessage);
	}
	
	/**
	 * 记录调用 接口传入参数数据
	 * @author 黄永丰
	 * @createtime 2016年5月18日
	 * @param data 传入参数数据
	 * @param interfaceName 接口名称
	 */
	public static void requestData(String data,String interfaceName)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("***************************"+interfaceName+" request data Begin***************************");
		sb.append("\n");
		sb.append(data);
		sb.append("\n");
		sb.append("***************************"+interfaceName+" request data End*****************************");
		sb.append("\n");
		log.info(sb.toString());
	}
	
	/**
	 * 记录调用 接口传入参数数据
	 * @author 黄永丰
	 * @createtime 2016年5月18日
	 * @param data 传入参数数据
	 * @param interfaceName 接口名称
	 */
	public static void requestEncryptData(String data,String interfaceName)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("***************************"+interfaceName+" request encrypt data Begin***************************");
		sb.append("\n");
		sb.append(data);
		sb.append("\n");
		sb.append("***************************"+interfaceName+" request encrypt data End*****************************");
		sb.append("\n");
		log.info(sb.toString());
	}
	
	/**
	 * 记录异常错误信息
	 * @author 黄永丰
	 * @createtime 2016年5月18日
	 * @param errorName 错误名称
	 * @param errorMessage 错误信息
	 */
	public static void exceptionError(String errorName,String errorMessage)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("==================================="+errorName+"Exception Begin===================================");
		sb.append("\n");
		sb.append(errorMessage);
		sb.append("\n");
		sb.append("==================================="+errorName+"Exception End=====================================");
		sb.append("\n");
		log.error(sb.toString());
	}
	
	public static void error(String message, Throwable e){
		log.error(message, e);
	}
	
	/**
	 * 记录调用 接口输出参数数据
	 * @author 黄永丰
	 * @createtime 2016年5月18日
	 * @param data 传入参数数据
	 * @param interfaceName 接口名称
	 */
	public static void responeData(String data,String interfaceName)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("***************************"+interfaceName+" respone data Begin***************************");
		sb.append("\n");
		sb.append(data);
		sb.append("\n");
		sb.append("***************************"+interfaceName+" respone data End*****************************");
		sb.append("\n");
		log.info(sb.toString());
	}
	
	/**
	 * 记录调用 接口输出参数数据
	 * @author 黄永丰
	 * @createtime 2016年5月18日
	 * @param data 传入参数数据
	 */
	public static void responeData(String data)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("***************************respone data Begin***************************");
		sb.append("\n");
		JSONObject resultObj = JSONObject.parseObject(data);
		if (resultObj.get("mYlogMessage") != null)
		{
			resultObj.remove("mYlogMessage");
			data = resultObj.toString();
		}
		sb.append(data);
		sb.append("\n");
		sb.append("***************************respone data End*****************************");
		sb.append("\n");
		log.info(sb.toString());
	}
	
	
}
