package com.meizu.demo.system.model;

import com.meizu.simplify.mvc.model.Model;

/**
 * <p><b>Title:</b><i>表单基础模型</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月29日 下午4:49:47</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月29日 下午4:49:47</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class BaseModel extends Model {

	private Integer order = 0;
	
	private Integer currentPage;
	private Integer pageSize;
	
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
	
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	
}
