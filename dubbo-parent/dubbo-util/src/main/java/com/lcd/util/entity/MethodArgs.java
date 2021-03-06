package com.lcd.util.entity;

import java.io.Serializable;

public class MethodArgs implements Serializable
{
	private static final long serialVersionUID = 4007599739356701149L;
	
	/**类名称*/
	private String className;
	/**类的方法名称*/
	private String methodName;
	/**类的方法的传入参数*/
	private String methodData;
	/**接口名称*/
	private String interfaceName;
	
	public String getInterfaceName() {
		return interfaceName;
	}
	
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public String getMethodName()
	{
		return methodName;
	}

	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}

	public String getMethodData()
	{
		return methodData;
	}

	public void setMethodData(String methodData)
	{
		this.methodData = methodData;
	}

	@Override
	public String toString()
	{
		return "MethodArgs [className=" + className + ", methodName=" + methodName + ", methodData=" + methodData + "]";
	}

}
