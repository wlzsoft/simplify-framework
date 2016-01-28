package com.meizu.entity;

import java.io.Serializable;

import com.meizu.dao.annotations.Key;

public class IdEntity<T,PK extends Serializable> implements Serializable{
	//@Transient
	private static final long serialVersionUID = 2654661729122034826L;
//	@NotNull
	@Key
	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
