package com.meizu.data.dialect;

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
	 * 是否支持分页查询的记录数限制
	 * @return 真(true)表示支持，假(false)表示不支持
	 */
	boolean supportsLimit();

	/**
	 * 是否支持分页查询的记录数和起始记录号限制
	 * @return 真(true)表示支持，假(false)表示不支持
	 */
    boolean supportsLimitOffset();

    /**
     * 将sql变成分页sql语句,直接使用offset,limit的值作为占位符。
     * @param sql sql语句
     * @param offset 分页用的起始记录号
     * @param limit 获取的最大记录数
     * @return 处理后的sql语句
     */
    String getLimitString(String sql, int offset, int limit);

}
