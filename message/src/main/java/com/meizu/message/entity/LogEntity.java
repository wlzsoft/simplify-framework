package com.meizu.message.entity;

import com.meizu.message.enums.Terminal;
import com.meizu.message.enums.Type;

/**
 * 业务日志实体
 * @author wanghb
 * @Company:meizu
 * @version V1.0
 * @Date 2015年12月14日
 * @Copyright:Copyright(c)2015
 */
public class LogEntity implements java.io.Serializable {

	private static final long serialVersionUID = -9213847583807623555L;
	
	private Type type;// 日志类型
	private String operationModule;// 模块名
	private String remark;// 描述 节点信息
	private Terminal terminalName;// 渠道名
	private Object beans;// 日志记录实体bean
	private Integer count;// 响应数据条数;
	
	private String ip;
	private String level;// 日志级别
	private String method;// 方法
	private String line;// 行
	private String createTime;// 时间
	private String consumeTime;//方法执行耗时
	private Integer userId;// 当前用户Id
	private String userName;// 当前用户名称
	private String sessionId;
	private String url;

	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

	public Object getBeans() {
		return beans;
	}
	public void setBeans(Object beans) {
		this.beans = beans;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getOperationModule() {
		return operationModule;
	}

	public void setOperationModule(String operationModule) {
		this.operationModule = operationModule;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Terminal getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(Terminal terminalName) {
		this.terminalName = terminalName;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getConsumeTime() {
		return consumeTime;
	}
	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
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
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
