package com.meizu.simplify.mvc.model;

/**
 * <p><b>Title:</b><i>基础表单模型</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月29日 下午4:49:47</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月29日 下午4:49:47</p>
 * @author <a href="mailto:luchuangye@meizu.com">luchuangye</a>
 * @version Version 0.1
 *
 */
public class BaseModel extends Model {

	/**
	 * 排序字段名
	 */
	private String sort;
	/**
	 * 是否倒序
	 */
	private Boolean isDesc = true;
	private Integer currentPage = 1;
	private Integer pageSize = 10;
	
	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Boolean getIsDesc() {
		return isDesc;
	}

	public void setIsDesc(Boolean isDesc) {
		this.isDesc = isDesc;
	}
	
}
