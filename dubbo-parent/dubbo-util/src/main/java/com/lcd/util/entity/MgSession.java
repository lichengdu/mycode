package com.lcd.util.entity;

import java.io.Serializable;

public class MgSession implements Serializable
{
	private static final long serialVersionUID = 809453367826505769L;
	
	/**sessionid*/
	public String sessionid;
	/**密钥*/
	public String secretkey;
	/**公司id*/
	public long companyid;
	/**公司名称*/
	private String companyname;
	/**公司类型id*/
	private int companytypeid;
	/**公司类型名称*/
	private String companytypename;
	/**站点id*/
	private long stationid;
	/**站点名称*/
	private String stationname;
	/**站点类型id*/
	private int stationtypeid;
	/**站点类型名称*/
	private String stationtypename;
	/**员工id*/
	private long staffid;
	/**员工名称*/
	private String staffname;
	/**最后登录时间*/
	public String lastusetime;
	/**登录时间*/
	private String logintime;
	/**登录ip*/
	private String ip;
	/**登录端口*/
	private int port;
	/**登录设备类型*/
	private int devicetype;
	/**session有校时间*/
	private int persisted;
	/**员工类型id*/
	private int roletypeid;
	/**员工类型名称*/
	private String roletypename;
	/**本地openapi*/
	private String openApiUrl;
	/**核心openapi*/
	private String platformOpenApiUrl;
	/**登录名称*/
	private String loginname;
	/**是否清分公司标识*/
	private int clarifyflag;
	/**导出文件临时存储路径*/
	private String tmpFileDir;
	/**导出文件临时请求路径*/
	private String tmpFileUrl;
	
	public String getTmpFileUrl() {
		return tmpFileUrl;
	}
	public void setTmpFileUrl(String tmpFileUrl) {
		this.tmpFileUrl = tmpFileUrl;
	}
	public String getTmpFileDir() {
		return tmpFileDir;
	}
	public void setTmpFileDir(String tmpFileDir) {
		this.tmpFileDir = tmpFileDir;
	}
	public int getClarifyflag() {
		return clarifyflag;
	}
	public void setClarifyflag(int clarifyflag) {
		this.clarifyflag = clarifyflag;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getOpenApiUrl() {
		return openApiUrl;
	}
	public void setOpenApiUrl(String openApiUrl) {
		this.openApiUrl = openApiUrl;
	}
	public String getPlatformOpenApiUrl() {
		return platformOpenApiUrl;
	}
	public void setPlatformOpenApiUrl(String platformOpenApiUrl) {
		this.platformOpenApiUrl = platformOpenApiUrl;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public String getSecretkey() {
		return secretkey;
	}
	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}
	public long getCompanyid() {
		return companyid;
	}
	public void setCompanyid(long companyid) {
		this.companyid = companyid;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public int getCompanytypeid() {
		return companytypeid;
	}
	public void setCompanytypeid(int companytypeid) {
		this.companytypeid = companytypeid;
	}
	public String getCompanytypename() {
		return companytypename;
	}
	public void setCompanytypename(String companytypename) {
		this.companytypename = companytypename;
	}
	public long getStationid() {
		return stationid;
	}
	public void setStationid(long stationid) {
		this.stationid = stationid;
	}
	public String getStationname() {
		return stationname;
	}
	public void setStationname(String stationname) {
		this.stationname = stationname;
	}
	public int getStationtypeid() {
		return stationtypeid;
	}
	public void setStationtypeid(int stationtypeid) {
		this.stationtypeid = stationtypeid;
	}
	public String getStationtypename() {
		return stationtypename;
	}
	public void setStationtypename(String stationtypename) {
		this.stationtypename = stationtypename;
	}
	public long getStaffid() {
		return staffid;
	}
	public void setStaffid(long staffid) {
		this.staffid = staffid;
	}
	public String getStaffname() {
		return staffname;
	}
	public void setStaffname(String staffname) {
		this.staffname = staffname;
	}
	public String getLastusetime() {
		return lastusetime;
	}
	public void setLastusetime(String lastusetime) {
		this.lastusetime = lastusetime;
	}
	public String getLogintime() {
		return logintime;
	}
	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getDevicetype() {
		return devicetype;
	}
	public void setDevicetype(int devicetype) {
		this.devicetype = devicetype;
	}
	public int getPersisted() {
		return persisted;
	}
	public void setPersisted(int persisted) {
		this.persisted = persisted;
	}
	public int getRoletypeid() {
		return roletypeid;
	}
	public void setRoletypeid(int roletypeid) {
		this.roletypeid = roletypeid;
	}
	public String getRoletypename() {
		return roletypename;
	}
	public void setRoletypename(String roletypename) {
		this.roletypename = roletypename;
	}
	
	
	

	
}
