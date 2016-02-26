package com.meizu.simplify.entity;

import java.io.Serializable;

import com.meizu.simplify.dao.annotations.Key;

public class IdEntity<T,PK extends Serializable> implements Serializable{
	//@Transient
	private static final long serialVersionUID = -3333906406099241984L;
//	@NotNull
	@Key
	private Integer fid;
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	
}
