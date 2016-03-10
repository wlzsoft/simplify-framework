package com.meizu.simplify.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.meizu.simplify.dao.annotations.Column;
import com.meizu.simplify.dao.annotations.Transient;

public class BaseEntity extends IdEntity<Serializable, Integer> {
	private static final long serialVersionUID = -6176797953739560746L;
	
	@Column(value = "createId")
	public Integer createId;
	
	@Column(value = "createTime")
	@JSONField (format="yyyy-MM-dd HH:mm:ss")
	public Date createTime;
	
	@Column(value = "updateId")
	public Integer updateId;
	
	@Column(value = "updateTime")
	@JSONField (format="yyyy-MM-dd HH:mm:ss")
	public Date updateTime;
	
	@Column(value = "deleteflag")
	public Boolean deleteflag;
	
	@Transient
	public String createName;
	@Transient
	public String updateName;

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

	public Boolean getDeleteflag() {
		return deleteflag;
	}

	public void setDeleteflag(Boolean deleteflag) {
		this.deleteflag = deleteflag;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
}
