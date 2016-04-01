package com.meizu.message.entity;

import java.io.Serializable;

/**
 * log4j日志实体
 * 
 * @author wanghb
 * @Company:meizu
 * @version V1.0
 * @Date 2015年12月14日
 * @Copyright:Copyright(c)2015
 */

public class Log4JEntity implements Serializable {

	private static final long serialVersionUID = 2776696944828270988L;

	private String ip;// 节点IP
	private String level;// 日志级别
	private Object message;// 日志信息
	private String className;// 类
	private String method;// 方法
	private String line;// 行
	private String fileName;
	private String createTime;// 时间
	private String LoggerName;// 日志打印类
	private Integer userId;// 当前用户Id
	private String userName;// 当前用户名称
	private String sessionId;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getLoggerName() {
		return LoggerName;
	}

	public void setLoggerName(String loggerName) {
		LoggerName = loggerName;
	}

}
