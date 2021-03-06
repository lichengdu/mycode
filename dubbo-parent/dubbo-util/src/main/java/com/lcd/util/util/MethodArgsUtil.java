package com.lcd.util.util;

import java.util.Map;

import com.alibaba.dubbo.common.json.JSON;
import com.lcd.util.entity.MethodArgs;
import com.lcd.util.exception.MgException;



public class MethodArgsUtil
{
	/**
	 * 检查方法名称、服务名称、参数数据，并返回对象
	 * @author 黄永丰
	 * @createtime 2016年1月2日
	 * @param postdata 传入数据
	 * @return 方法名称、服务名称、参数数据对象
	 */
	public static MethodArgs checkPostMethodData(Map<String,String> postdata) throws Exception
	{
		MethodArgs methodArgs = new MethodArgs();
		try
		{
			//1、获取传入参数
			String method = postdata.containsKey("method") ? postdata.get("method") : "";
			//2、判断method
			checkInterfaceName(method);
			//3、验证接口,如果是config服务就不用session,全是base64加密,其它的获取session,并解密数据
			String methodData=null;
			if(postdata.containsKey("data")){
				methodData=JSON.json(postdata.get("data"));
			}
			
			//4、对应参数返回
			String className = getClassName(method);//获取对应的类型和方法名称
			String methodName = getMethodName(method);
			methodArgs.setMethodData(methodData);
			methodArgs.setMethodName(methodName);
			methodArgs.setClassName(className);
			methodArgs.setInterfaceName(method);
			return methodArgs;
		}
		catch (MgException ex)
		{
			throw new MgException(ex.getRet(), ex.getMessage());
		}
		catch (Exception e)
		{
			if (e.getClass().equals(MgException.class)) throw e;
			else throw new MgException(-1, "检查方法名称、服务名称、参数数据失败."+e.getMessage(),e);
		}
	}
	

	/**
	 * 判断调用接口类的方法名称
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param interfaceName 接口名称
	 * @return 返回调用接口类的方法名称
	 */
	public static void checkInterfaceName(String interfaceName)
	{
		if (interfaceName == null || interfaceName.length() == 0)
			throw new MgException(-1, "method参数不能为空.");
		String[] methodToken = interfaceName.split("\\.");
		if (methodToken.length < 4)
			throw new MgException(-1, "传入method方法长度必须大于4节.");
	}
	
	/**
	 * 获取调用接口的服务名称
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param interfaceName 接口名称
	 * @return 返回用接口的服务名称
	 */
	public static String getServiceName(String interfaceName)
	{
		String serviceName = "";
		String[] methodToken = interfaceName.split("\\.");
		serviceName = methodToken[1].toLowerCase();
		return serviceName;
	}
	
	/**
	 * 获取调用接口类的方法名称
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param interfaceName 接口名称
	 * @return 返回调用接口类的方法名称
	 */
	public static String getMethodName(String interfaceName)
	{
		String methodName = "";
		String[] methodToken = interfaceName.split("\\.");
		int count = 0;
		for (int i = methodToken.length - 1; i >= 3; i--)
		{
			if (count == 0)
				methodName += methodToken[i].toLowerCase();
			else
				methodName += methodToken[i].substring(0, 1).toUpperCase() + methodToken[i].substring(1);
			count++;
		}
		return methodName;
	}

	/**
	 * 获取调用接口的类名称
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param interfaceName 接口名称
	 * @return 返回用接口的类名称
	 */
	public static String getClassName(String interfaceName)
	{
		String className = "";
		String[] methodToken = interfaceName.split("\\.");
		className = methodToken[2].toLowerCase() + "ServiceImpl";
		return className;
	}

	public static void main(String[] args)
	{
		System.out.println(getClassName("maplemart.openapi.test.get"));
		System.out.println(getMethodName("maplemart.openapi.test.get"));
		System.out.println(getClassName("maplemart.openapi.test.all.get"));
		System.out.println(getMethodName("maplemart.openapi.test.all.get"));
	}

}
