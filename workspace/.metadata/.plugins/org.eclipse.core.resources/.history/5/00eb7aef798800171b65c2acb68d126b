package com.lcd.util.exception;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.BadSqlGrammarException;

import com.lcd.util.log.ILog;




public class MgException extends RuntimeException implements ILog
{

	private static final long serialVersionUID = 1L;
	private int ret;
	private String message;
	private String mYlogMessage;
	
	/**
	 * 统一接口抛出异常
	 * @param e Exception
	 * @param sessionid 
	 * @throws Exception
	 */
	public MgException(Exception e) throws Exception
	{
		if (e.getClass().equals(MgException.class)) throw e;
		else if (e.getClass().equals(BadSqlGrammarException.class))
		{
			BadSqlGrammarException bsge = (BadSqlGrammarException) e;
			SQLException ex = bsge.getSQLException();
			String state = ex.getSQLState();
			throw new MgException(-1, "SQL执行错误,SQL错误码为[" + state + "]",e);
		}
		else throw new MgException(-1, "程序处理错误,请稍后重试.",e);
	}
	
	/**
	 * 统一接口抛出异常
	 * @param e Exception
	 * @param sessionid 
	 * @throws Exception
	 */
	public MgException(int ret,Exception e) throws Exception
	{
		if (e.getClass().equals(MgException.class)) throw e;
		else if (e.getClass().equals(BadSqlGrammarException.class))
		{
			BadSqlGrammarException bsge = (BadSqlGrammarException) e;
			SQLException ex = bsge.getSQLException();
			String state = ex.getSQLState();
			throw new MgException(ret, "SQL执行错误,SQL错误码为[" + state + "]",e);
		}
		else throw new MgException(ret, "程序处理错误.",e);
	}

	/**
	 * 参数检查统一异常
	 * @author 黄永丰
	 * @createtime 2016年5月18日
	 * @param ret 错误码
	 * @param message 错误信息
	 */
	public MgException(int ret, String message)
	{
		// 记录错误日志
		String projectName = getProjectName();
		Map<String, String> map = getClassNameAndMethodNameAndlineNumber();
		String logMessage = writeLog(message,projectName, map.get("className"), map.get("methodName")
									, null, map.get("lineNumber"), message, null, null);
		log.error(logMessage);
		// 返回错误信息
		this.ret = ret;
		this.message = message;
		this.mYlogMessage = logMessage;
	}

	/**
	 * 参数检查统一异常
	 * @author 黄永丰
	 * @createtime 2016年5月18日
	 * @param ret 错误码
	 * @param message 返回用户查询错误信息
	 * @param logMessage 记录日志的错误信息
	 */
	public MgException(int ret, String message,String logMessage)
	{
		// 记录错误日志
		String projectName = getProjectName();
		Map<String, String> map = getClassNameAndMethodNameAndlineNumber();
		logMessage = writeLog(message,projectName, map.get("className"), map.get("methodName")
									, null, map.get("lineNumber"), logMessage, null, null);
		log.error(logMessage);
		// 返回错误信息
		this.ret = ret;
		this.message = message;
		this.mYlogMessage = logMessage;
	}

	/**
	 * 业务处理统一异常
	 * @author 黄永丰
	 * @createtime 2016年5月18日
	 * @param ret 错误码
	 * @param message 错误信息
	 * @param sessionid sessionid
	 * @param e 异常对象e
	 */
//	public MgException(int ret, String message,Throwable e)
//	{
//		super(message, e);
//		// 返回错误信息
//		String userName = null;
//		String ip = null;
//		if (sessionid != null && !sessionid.equals(""))
//		{
//			tf_session session = SessionManager.getInstance().getSession(sessionid);
//			if (session != null)
//			{
//				userName = session.getUsername();
//				ip = session.getIp();
//			}
//		}
//		// 记录第一次错误
//		String projectName = getProjectName();
//		StackTraceElement[] elems = e.getStackTrace();
//		StackTraceElement elem = elems[0];
//		String logMessage = writeLog(message,projectName,elem.getClassName(),elem.getMethodName()
//							,e.getClass().getName(),elem.getLineNumber()+"",e.getMessage(),userName,ip);
//		log.error(logMessage);
//		// 返回错误信息
//		this.ret = ret;
//		this.message = message;
//		this.logMessage = logMessage;
//	}

	/**
	 * 业务处理统一异常
	 * @author 黄永丰
	 * @createtime 2016年5月18日
	 * @param ret 错误码
	 * @param message 错误信息
	 * @param e 异常对象e
	 */
	public MgException(int ret, String message, Throwable e)
	{
		// 记录第一次错误
		String projectName = getProjectName();
		StackTraceElement[] elems = e.getStackTrace();
		StackTraceElement elem = elems[0];
		String logMessage = writeLog(message,projectName,elem.getClassName(),elem.getMethodName()
							,e.getClass().getName(),elem.getLineNumber()+"",e.getMessage(),null,null);
		log.error(logMessage);
		//记录所有错误
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("\n");
		sbuf.append("===================================Exception Detail Begin=====================================");
		sbuf.append("\n");
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		for (StackTraceElement ste : elems) 
		{
			sbuf.append("在["+date+"]时间,");
			sbuf.append("类[" + ste.getClassName() + "]调用["+ste.getMethodName()+"]方法时,");
			sbuf.append("发生了["+e.getClass().getName()+"]异常,");
			sbuf.append("异常出现在第[" + ste.getLineNumber()+ "]行代码.");
			sbuf.append("\n");
		}
		sbuf.append("===================================Exception Detail End=====================================");
		sbuf.append("\n");
		log.error(sbuf.toString());
		// 返回错误信息
		this.ret = ret;
		this.message = message;
		this.mYlogMessage = logMessage;
	}

	/***
	 * 记录错误 日志 
	 * @author 黄永丰
	 * @createtime 2016年5月18日
	 * @param message 业务抛出错误信息
	 * @param projectName 工程名称
	 * @param className 包名.类名
	 * @param methodName 方法名
	 * @param exceptionName 异常名称
	 * @param lineNumber 错误代码在那行数
	 * @param exceptionMessage 异常信息
	 * @param userName 操作用户名
	 * @param ip 操作用户ip
	 * @return 错误 日志
	 */
	private String writeLog(String message,String projectName,String className,String methodName
			,String exceptionName,String lineNumber,String exceptionMessage,String userName,String ip)
	{
		// 记录错误日志
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("===================================Exception Begin===================================");
		sb.append("\n");
		sb.append("[" + message + "]异常.");
		sb.append("\n");
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		sb.append("在[" + date + "]时间,");
		sb.append("\n");
		if (userName != null && !"".equals(userName))
		{
			sb.append("用户[" + userName + "],");
			sb.append("\n");
		}
		if (ip != null && !"".equals(ip))
		{
			sb.append("IP[" + ip + "],");
			sb.append("\n");
		}
		if (projectName != null)
		{
			sb.append("工程名称为[" + projectName + "],");
			sb.append("\n");
		}
		sb.append("类[" + className + "],");
		sb.append("\n");
		sb.append("调用[" + methodName + "]方法时,");
		sb.append("\n");
		if (exceptionName != null && !"".equals(exceptionName))
		{
			sb.append("发生了[" + exceptionName + "]异常,");
			sb.append("\n");
		}
		sb.append("异常出现在第[" + lineNumber + "]行代码.");
		sb.append("\n");
		if (exceptionMessage != null && !"".equals(exceptionMessage))
		{
			sb.append("异常信息为[" + exceptionMessage + "]");
			sb.append("\n");
		}
		sb.append("===================================Exception End=====================================");
		sb.append("\n");
		return sb.toString();
	}
	
	/**
	 * 获取当前工程名称
	 * @author 黄永丰
	 * @createtime 2016年5月18日
	 * @return 工程名称
	 */
	private String getProjectName()
	{
		// 获取当前项目的路径
		String tempStr = System.getProperty("user.dir");
		// 获取项目名
		String projectName = null;
		if (tempStr != null)
		{
			if ("\\".equals(File.separator))
			{
				projectName = tempStr.substring(tempStr.lastIndexOf(File.separator) + 1, tempStr.length());
			}
			else
			{
				tempStr = tempStr.substring(0,tempStr.lastIndexOf(File.separator));
				projectName = tempStr.substring(tempStr.lastIndexOf(File.separator) + 1, tempStr.length());
			}
		}
		return projectName;
	}

	/**
	 * 获取抛出异常的类名与方法名与在那行代码抛出
	 * @author 黄永丰
	 * @createtime 2016年5月18日
	 * @return {"className":包名.类名,"methodName":方法名,"lineNumber":那行代码抛出}
	 */
	private Map<String, String> getClassNameAndMethodNameAndlineNumber()
	{
		String className = Thread.currentThread().getStackTrace()[3].getClassName();
		String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
		int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
		Map<String, String> map = new HashMap<String, String>();
		map.put("className", className);
		map.put("methodName", methodName);
		map.put("lineNumber", lineNumber + "");
		return map;
	}

	public int getRet()
	{
		return ret;
	}
	public void setRet(int ret)
	{
		this.ret = ret;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getmYlogMessage()
	{
		return mYlogMessage;
	}

	public void setmYlogMessage(String mYlogMessage)
	{
		this.mYlogMessage = mYlogMessage;
	}


}
