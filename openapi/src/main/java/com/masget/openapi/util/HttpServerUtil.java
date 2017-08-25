package com.masget.openapi.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpServerUtil {
	 
	public static boolean isEmpty(String s)
	  {
	    return (s == null) || ("".equals(s.trim()));
	  }
	
	public static String getServiceName(String method){
		String result="";
		
		String[] strs=method.split("\\.");
		if(strs.length>2){
			result=strs[1];
			result=result+"Service";
		}
		
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public  static Object invokeMethodGernaral(Object owner,String methodName,Object[]args) 
    {
      //a.先获取对象所属的类
      Class ownerClass = owner.getClass();
       Method method=null;
       Object result=null;
        //b.获取需要调用的方法
       for(Method m:ownerClass.getDeclaredMethods())
       {
       	if(m.getName().equalsIgnoreCase(methodName))
       	{
       		method=m;
       		break;
       	}
       }
       try {
       //c.调用该方法
        result=method.invoke(owner, args);//调用方法
      } catch (IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
       return result;
    }
}
