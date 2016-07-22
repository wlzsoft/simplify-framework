package com.meizu.demo.mvc.model;

import com.meizu.demo.mvc.entity.Test;
import com.meizu.simplify.mvc.model.*;
import com.meizu.simplify.mvc.model.ModelCharsFilter.Filter;

import static com.meizu.simplify.mvc.model.ValidTypeEnum.*;

public class TestModel extends BaseModel {


	private String productName;
	private String name;
	private Integer age;
	private String desc;
	private Test test;

	public String getProductName() {
		return productName;
	}
//	@Valid({NotNull, Number})
	@Valid(value = {NotNull, Number},defaultMessages = {"不能为空引用"})
	public void setProductName(String productName) {
		this.productName = productName;
	}

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

	@ModelScope(charset = "utf-8") // 这个目前只对字符设置起作用，范围作用域的设置没有实现
	public void setAge(Integer age) {
		this.age = age;
	}

	public String getDesc() {
		return desc;
	}

	@ModelCharsFilter(filters = { Filter.Html, Filter.Script })
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}
	
}
