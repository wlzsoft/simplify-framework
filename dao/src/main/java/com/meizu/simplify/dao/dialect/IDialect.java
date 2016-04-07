package com.meizu.simplify.dao.dialect;

/**
 * 
 * <p><b>Title:</b><i> 数据库方言接口</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午5:31:44</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午5:31:44</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public interface IDialect {

	/**
	 * 
	 * 方法用途: 是否支持分页查询的记录数限制<br>
	 * 操作步骤: TODO<br>
	 * @return  
	 */
	boolean supportsLimit();

    /**
     * 
     * 方法用途: 是否支持分页查询的记录数和起始记录号限制<br>
     * 操作步骤: TODO<br>
     * @return
     */
    boolean supportsLimitOffset();

    /**
     * 
     * 方法用途: 将sql变成分页sql语句,直接使用offset,limit的值作为占位符<br>
     * 操作步骤: TODO<br>
     * @param sql
     * @param offset 分页用的起始记录号
     * @param limit 获取的最大记录数
     * @return
     */
    String getLimitString(String sql, int offset, int limit);

}
