package com.meizu.simplify.dao;

/**
 * <p><b>Title:</b><i>数据库公用函数dao接口</i></p>
 * <p>Desc: 考虑跨数据库扩展,考虑dialect方言集成</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月28日 下午2:59:39</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月28日 下午2:59:39</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IDBUtilDao {
	
	/**
	 * 
	 * 方法用途: 获取数据库的当前时间<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public String getDbDate();
	
	
}
