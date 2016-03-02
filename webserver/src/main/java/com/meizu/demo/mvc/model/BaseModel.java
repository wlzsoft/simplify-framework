package com.meizu.demo.mvc.model;

import com.meizu.simplify.mvc.directives.Model;

/**
 * 表单基础模型
 * 
 */
public class BaseModel extends Model {
	private String cmd = "view";
	private String jsonp = "callback";

	private Integer start = 0;
	private Integer pageCount = 10;
	private Integer repCount = 3; // 默认显示回复数
	private Integer order = 0;
	private Integer script = 0; // 是否为Script方式加载

	public final String getCmd() {
		return cmd;
	}

	public final void setCmd(final String cmd) {
		this.cmd = cmd;
	}

	public String getJsonp() {
		return jsonp;
	}

	public void setJsonp(String jsonp) {
		this.jsonp = jsonp;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getScript() {
		return script;
	}

	public void setScript(Integer script) {
		this.script = script;
	}

	public Integer getRepCount() {
		return repCount;
	}

	public void setRepCount(Integer repCount) {
		this.repCount = repCount;
	}

}
