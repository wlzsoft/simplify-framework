package com.meizu.entity.baseEntity;

import java.io.Serializable;
import java.util.Date;

//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.meizu.data.annotations.Column;

public class BaseEntity extends IdEntity<Serializable,Integer>{
//public class BaseEntity<T extends Object> extends IdEntity<T>{
	//@Transient
	private static final long serialVersionUID = -5480206151290967747L;
	@Column(value="createId")
//	@JsonSerialize(using=UserIdGenericSerializer.class)
	private Integer createId;
	@Column(value="createTime")
	private Date createTime;
	@Column(value="updateId")
//	@JsonSerialize(using=UserIdGenericSerializer.class)
	private Integer updateId;
	@Column(value="updateTime")
	private Date updateTime;
	@Column(value="delFlag")
	private Boolean delFlag;
	public Integer getCreateId() {
		return createId;
	}
	public void setCreateId(Integer createId) {
		this.createId = createId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getUpdateId() {
		return updateId;
	}
	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Boolean isDelFlag() {
		return delFlag;
	}
	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}
}
