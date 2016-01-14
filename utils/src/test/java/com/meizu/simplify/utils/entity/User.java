package com.meizu.simplify.utils.entity;

import java.io.Serializable;

public class User implements Serializable {

	
	private static final long serialVersionUID = -9002719755949647466L;
	public User() {
		
	}
	public User(String id, String name) {
		this.id = id;
		this.name = name;
	}
	private String id;
	private String name;
	private String addr;
	private String phone;
	private String username;
	private int age;
	private String password;

	public String getAddr() {
		return addr;
	}
	
	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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