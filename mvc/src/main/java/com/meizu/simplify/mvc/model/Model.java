package com.meizu.simplify.mvc.model;

/**
 * <p><b>Title:</b><i>表单处理模型</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:24:30</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:24:30</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public abstract class Model {
	
	/**
	 * 过滤器模式的参数值
	 */
	private String[] params = new String[] {}; 
	
	private String cmd = "view";
	
	/**
	 * 是否为Script方式加载  0：jsonp格式
	 *                     1：jsonp自加载格式，除了数据传输，还有js页面加载触发事件
	 */ 
	private Integer script = 0;
	
	private String jsonp = "callback";

	public String getJsonp() {
		return jsonp;
	}

	public void setJsonp(String jsonp) {
		this.jsonp = jsonp;
	}

	
	public String getCmd() {
		return cmd;
	}
	
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	public String[] getParams() {
		return params;
	}
	
	public void setParams(String[] params) {
		this.params = params;
	}
	
	public Integer getScript() {
		return script;
	}

	public void setScript(Integer script) {
		this.script = script;
	}

}