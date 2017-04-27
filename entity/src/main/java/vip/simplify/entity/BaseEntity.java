package vip.simplify.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import vip.simplify.entity.annotations.Column;
import vip.simplify.entity.annotations.Transient;

/**
 * <p><b>Title:</b><i>基础实体类</i></p>
 * <p>Desc: 注 TODO 这里对于createName和updateName后续可以考虑是否可以通过在createId上使用注解来实现，而不需要显示指定这两个字段</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月18日 下午1:09:55</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月18日 下午1:09:55</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
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
	
	@Column(value = "remark")
	public String remark;
	
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
	
	public void setDeleteflag(Boolean deleteflag) {
		this.deleteflag = deleteflag;
	}

	public Boolean getDeleteflag() {
		return deleteflag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
