package com.meizu.demo.mvc.model;

import com.meizu.demo.system.model.BaseModel;
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
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
