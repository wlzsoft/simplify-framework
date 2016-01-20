package com.meizu.mvc.directives;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表单处理模型
 * 
 * 
 */
public abstract class Model {
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Inherited
	public @interface ModelSet {
		String charset(); // 编码
		
		Scope scope(); // 作用域
		
		public enum Scope {
			page, cookie, session, application
		}
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Inherited
	public @interface StringFilter {
		Filter[] filters(); // 格式化选项
		
		public enum Filter {
			Script, Style, Html, iframe, trim
		}
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Inherited
	public @interface Passme {
	}
	
	private String[] prarms = new String[] {}; // 过滤器模式的参数值
	
	private String cmd = "view";
	
	public String getCmd() {
		return cmd;
	}
	
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	public String[] getPrarms() {
		return prarms;
	}
	
	public void setPrarms(String[] prarms) {
		this.prarms = prarms;
	}
}