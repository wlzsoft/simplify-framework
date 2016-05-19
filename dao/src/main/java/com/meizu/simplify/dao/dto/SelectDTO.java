package com.meizu.simplify.dao.dto;

import com.meizu.simplify.entity.page.Page;


/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月18日 上午10:21:10</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月18日 上午10:21:10</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public  class  SelectDTO  {
	private Page<?> page;
	private BaseDTO dto;
	private String orderBy;//格式 ："createTime,sort"
	
	public SelectDTO(BaseDTO dto) {
		this.dto = dto;
	}
	public SelectDTO(BaseDTO dto,Page<?> page) {
		this.dto = dto;
		this.page = page;
	}
	public SelectDTO(BaseDTO dto,Page<?> page,String orderBy) {
		this.dto = dto;
		this.page = page;
		this.orderBy = orderBy;
	}
	public  Page<?> getPage() {
		return page;
	}
	public <T> void setPage(Page<T> page) {
		this.page = page;
	}
	public BaseDTO getDto() {
		return dto;
	}
	public void setDto(BaseDTO dto) {
		this.dto = dto;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}
