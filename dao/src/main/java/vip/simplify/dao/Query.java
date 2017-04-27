package vip.simplify.dao;

import java.io.Serializable;
import java.util.List;

import vip.simplify.dao.dto.WhereDTO;
import vip.simplify.dao.orm.Dao;
import vip.simplify.entity.IdEntity;

/**
 * <p><b>Title:</b><i>mybatis查询条件封装</i></p>
 * <p>Desc: 暂未实现</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月18日 下午6:09:27</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月18日 下午6:09:27</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class Query<T extends IdEntity<Serializable,Integer>> {

	private String sql;
	private Object[] params;
	private String sortName;
	private String sortMethod;
	private int currentRecord;
	private int pageSize;
	private Dao<T, Serializable> dao;
	public Query(Dao<T, Serializable> dao,String sql, Object... params) {
		this.sql = sql;
		this.params = params;
		this.dao = dao;
	}

	public Query<T> setCurrentRecord(int currentRecord) {
		this.currentRecord = currentRecord;
		return this;
	}

	public Query<T> setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}
	
	public Query<T> setSortName(String sortName) {
		this.sortName = sortName;
		return this;
	}
	
	public Query<T> setSortMethod(boolean isDesc) {
		if(isDesc) {
			this.sortMethod = "desc";
		} else {
			this.sortMethod = "asm";
		}
		return this;
	}
	
	public List<T> list() {
		List<T> list = dao.find(sql +" order by "+sortName +" "+ sortMethod + " limit " +currentRecord+"," + pageSize,params);
		return list;
	}

	public T uniqueResult() {
		// TODO Auto-generated method stub
		return null;
	}

	public void add(WhereDTO null1) {
		// TODO Auto-generated method stub
		
	}

	

	

}
