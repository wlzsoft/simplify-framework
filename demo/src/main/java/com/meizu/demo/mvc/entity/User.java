package com.meizu.demo.mvc.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.meizu.simplify.entity.BaseEntity;
import com.meizu.simplify.entity.annotations.Entity;
import com.meizu.simplify.entity.annotations.Table;
import com.meizu.simplify.entity.annotations.Transient;

@Entity
@Table(name="sys_user")
@Transient({"deleteflag"})
public class User extends BaseEntity {

	private static final long serialVersionUID = -9002719755949647466L;
	private String name = "default";
	private String username;
	private int age = 18;
	private String password;
	Date bir = null;
	double salary = 890.12;
	User lover = null;
	List<User> friends = new ArrayList<>();

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
	
	public User() {}

	public User(String name) {
		this.name = name;
		try {
			bir = new SimpleDateFormat("yyyy-MM-dd").parse("1978-02-14");
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public Date getBir() {
		return bir;
	}

	public void setBir(Date bir) {
		this.bir = bir;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public User getLover() {
		return lover;
	}

	public void setLover(User lover) {
		this.lover = lover;
	}

	public List<User> getFriends() {
		return friends;
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
	}

	public static User getTestUser() {
		User joelli = new User("joelli");
		User lucy = new User("lucy");
		joelli.setLover(lucy);
		List<User> friends = new ArrayList<>();
		friends.add(lucy);
		User lyy = new User("lyy");
		friends.add(lyy);
		joelli.setAge(36);
		joelli.setSalary(10000.01);
		return joelli;

	}

	public static List<User> getTestUsers() {
		User joelli = new User("joelli");
		joelli.setAge(36);
		joelli.setSalary(10000.01);
		User lucy = new User("lucy");
		lucy.setAge(35);
		lucy.setSalary(6000.5);

		User lyy = new User("lyy");
		lyy.setAge(37);
		lyy.setSalary(12000.01);

		List<User> all = new ArrayList<>();

		all.add(joelli);
		all.add(lucy);
		all.add(lyy);
		return all;

	}
}