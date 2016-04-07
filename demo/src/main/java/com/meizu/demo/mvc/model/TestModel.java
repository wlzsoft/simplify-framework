package com.meizu.demo.mvc.model;

import com.meizu.demo.system.model.BaseModel;
import com.meizu.simplify.mvc.model.ModelCharsFilter;
import com.meizu.simplify.mvc.model.ModelCharsFilter.Filter;
import com.meizu.simplify.mvc.model.ModelScope;
import com.meizu.simplify.mvc.model.ModelSkip;

public class TestModel extends BaseModel {
	private String name;
	private Integer age;
	private String desc;
	public String getName() {
		return name;
	}
	
	@ModelSkip
	public void setName(String name) {
		this.name = name;
	}
	
	@ModelScope(charset="utf-8")//这个目前只对字符设置起作用，范围的设置没有实现
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	@ModelCharsFilter(filters={Filter.Html,Filter.Script})
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
