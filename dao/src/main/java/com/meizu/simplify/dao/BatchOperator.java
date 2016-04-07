package com.meizu.simplify.dao;
/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午6:00:23</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午6:00:23</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public enum BatchOperator {

	/**
	 * 每次删除带or方式的次数间隔==>>批量删除的时候要考虑的，分批删除，考虑是否保留
	 */
	DELETE_CRITICAL_VAL(25),
	/**
	 * 每次刷新同步到数据库的间隔
	 */
	FLUSH_CRITICAL_VAL(50);
	
	private Integer size;
	private BatchOperator(Integer size) {
		this.size = size;
	}
	public Integer getSize() {
		return size;
	}
}
