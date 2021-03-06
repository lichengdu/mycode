package com.lcd.util.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lcd.util.entity.MethodArgs;
import com.lcd.util.entity.RetStruct;
import com.lcd.util.exception.MgException;
import com.lcd.util.log.ILog;
import com.lcd.util.log.ILogUtil;
import com.lcd.util.util.DataValidatorUtil;
import com.lcd.util.util.GlobalContainer;
import com.lcd.util.util.MethodArgsUtil;
import com.lcd.util.util.MethodReflectUtil;

@Service
public class ProviderService implements ILog
{
	private DataValidatorUtil dataValidatorUtil;
	
	public ProviderService() 
	{
		dataValidatorUtil =  new DataValidatorUtil();
	}

	/**
	 * 通过doPost调用 base服务的接口
	 * @interfaceName
	 * @author 黄永丰
	 * @createtime 2016年10月8日
	 * @param postdata  客户端传入的map集合数据
	 * @return 调用接口后返回的数据
	 */
	public String doPost(Map<String, String> postdata)
	{
		String result = null;
		try
		{
			// 1、获取方法名称、服务名称、参数 数据的对象
			MethodArgs methodargs = MethodArgsUtil.checkPostMethodData(postdata);
			// 2、获取传入调用接口方法参数的数据
			String validData = methodargs.getMethodData();
			// 3、获取传入调用接口的类名称
			String className = methodargs.getClassName();
			// 4、获取传入调用接口方法名称
			String methodName = methodargs.getMethodName();
			// 5、获取调用类对象
			Object classObject = GlobalContainer.getApplicationContext().getBean(className);
			// 6、数据校验
			String paramsData = dataValidatorUtil.getValidData(classObject,methodName,validData);
			// 7、获取传入调用接口名称
			String interfaceName = methodargs.getInterfaceName();
			// 8、打印调用接口数据
			ILogUtil.requestData(paramsData, interfaceName);
			// 9、调用接口方法
			String sessionid = postdata.containsKey("session") ? postdata.get("session") : "";
			result = String.valueOf(MethodReflectUtil.invokeMethod(classObject,methodName,new Object[]{paramsData,sessionid/**调用方法参数多少个这里数组就是多少个长度数组*/}));
			// 10、打印输出接口数据
//			ILogUtil.responeData(result, interfaceName);
			return result;
		}
		catch (MgException ex)
		{
			return new RetStruct(ex.getRet(),ex.getMessage(),ex.getmYlogMessage()).toString();
		}
		catch (Exception e)
		{
			if (e.getClass().equals(MgException.class)) return e.getMessage();
			else return new RetStruct(-1,"调用doPost方法处理异常."+e.getMessage()).toString();
		}
	}
	
	/**
	 * 通过其它子系统过来的
	 */
	public String doPostForOtherSystem(String method, String data)
	{
		Map<String, String> postdata = new HashMap<String, String>();
		postdata.put("method", method);
		postdata.put("data", data);
		String result = null;
		try
		{
			// 1、获取方法名称、服务名称、参数 数据的对象
			MethodArgs methodargs = MethodArgsUtil.checkPostMethodData(postdata);
			// 2、获取传入调用接口方法参数的数据
			String validData = methodargs.getMethodData();
			// 3、获取传入调用接口的类名称
			String className = methodargs.getClassName();
			// 4、获取传入调用接口方法名称
			String methodName = methodargs.getMethodName();
			// 5、获取调用类对象
			Object classObject = GlobalContainer.getApplicationContext().getBean(className);
			// 6、数据校验
			String paramsData = dataValidatorUtil.getValidData(classObject,methodName,validData);
			// 7、获取传入调用接口名称
			String interfaceName = methodargs.getInterfaceName();
			// 8、打印调用接口数据
			ILogUtil.requestData(paramsData, interfaceName);
			// 9、调用接口方法
			String sessionid = postdata.containsKey("session") ? postdata.get("session") : "";
			result = String.valueOf(MethodReflectUtil.invokeMethod(classObject,methodName,new Object[]{paramsData,sessionid/**调用方法参数多少个这里数组就是多少个长度数组*/}));
			return result;
		}
		catch (Exception e)
		{
			if (e.getClass().equals(MgException.class)) 
				return new RetStruct(-1,e.getMessage()).toString();
			else
				return new RetStruct(-1,"调用doPostForOtherSystem方法处理异常."+e.getMessage()).toString();
		}
	}
	
	
}
