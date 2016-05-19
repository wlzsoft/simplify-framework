package com.meizu.simplify.entity.log;

import com.meizu.simplify.enums.log.Terminal;
import com.meizu.simplify.enums.log.Type;

/**
 * <p><b>Title:</b><i>业务日志实体</i></p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2014年12月30日 下午5:29:22</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2014年12月30日 下午5:29:22</p>
 * @author <a href="mailto:wanghaibin@meizu.com" >wanghaibin</a>
 * @version Version 3.0
 */
public class LogEntity implements java.io.Serializable {

	private static final long serialVersionUID = -9213847583807623555L;
	
	private String busiType;//业务类型：接口、业务
	private Type type;//操作类型
	private String operationModule;// 操作模块
	private String action;//动作：接口拉取、提交等
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
	private String fuserName;// '用户登录账号'
	private String fnickName;// '昵称'
	private String sessionId;
	private String url;
	
	
	public String getBusiType() {
		return busiType;
	}
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
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
	public String getFuserName() {
		return fuserName;
	}
	public void setFuserName(String fuserName) {
		this.fuserName = fuserName;
	}
	public String getFnickName() {
		return fnickName;
	}
	public void setFnickName(String fnickName) {
		this.fnickName = fnickName;
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
