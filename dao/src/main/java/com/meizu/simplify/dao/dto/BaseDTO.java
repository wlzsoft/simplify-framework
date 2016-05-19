package com.meizu.simplify.dao.dto;

import java.io.Serializable;
import java.util.List;

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
public class BaseDTO implements Serializable {
	private static final long serialVersionUID = 6682115598657256308L;
//	private String table;
//	public String getTable() {
//		return table;
//	}
//	public void setTable(String table) {
//		this.table = table;
//	}
	public BaseDTO() {
		
	}
	private String sql;
	private String preSql;
	/**
	 * 连接类型：or 或   and 
	 */
	private LinkType linkType = LinkType.OR;
	
	private List<WhereDTO> whereList;
	
	private String orderBy;//格式 ："createTime,sort"
	private String limit;
	
	private Page page;
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public String getPreSql() {
		return preSql;
	}
	public void setPreSql(String preSql) {
		this.preSql = preSql;
	}
	
	public List<WhereDTO> getWhereList() {
		return whereList;
	}
	public void setWhereList(List<WhereDTO> whereList) {
		this.whereList = whereList;
	}
	public LinkType getLinkType() {
		return linkType;
	}
	public void setLinkType(LinkType linkType) {
		this.linkType = linkType;
	}
	
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public enum LinkType {
		AND,OR
	}
}
