package com.meizu.demo.mvc.entity;

import com.meizu.simplify.dao.annotations.Entity;
import com.meizu.simplify.dao.annotations.Table;
import com.meizu.simplify.dao.annotations.Transient;
import com.meizu.simplify.entity.BaseEntity;

@Entity
@Table(name="sys_user")
@Transient({"deleteflag"})
public class User extends BaseEntity {

	private static final long serialVersionUID = -9002719755949647466L;
	private String name;
	private String username;
	private int age;
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}