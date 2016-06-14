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
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class Model {
	
	/**
	 * 过滤器模式的参数值
	 */
	private String[] params = new String[] {}; 
	
	/**
	 * 是否为script方式加载  0：jsonp自加载格式
	 *                     1：jsonp自加载格式，除了数据传输，还有js页面加载触发事件，可用于表单提交到iframe的无刷新表单提交
	 *                     2: jsonp的wiget组件方式，常用于头部自动加载(也可用模板引擎后台生成达到公用)和用于广告系统的弹出(百度的广告推荐块)
	 */ 
	private Integer script = 0;
	
	private String callback = "callback";

	
	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
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