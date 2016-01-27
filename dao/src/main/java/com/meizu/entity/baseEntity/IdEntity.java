package com.meizu.entity.baseEntity;

import java.io.Serializable;
import java.util.Date;

import com.meizu.data.annotations.Transient;
import com.meizu.data.annotations.Key;
import com.meizu.data.annotations.Column;

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
