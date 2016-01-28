package com.meizu.dao.dialect;

/**
 * 
 * <p><b>Title:</b><i> 数据库方言管理器接口</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午5:31:54</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午5:31:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public interface IDialectManager {

	/**
	 * 根据方言名称获取方言对象
	 * @param dialectName 方言名称
	 * @return 方言对象
	 */
	IDialect getDialect(String dialectName);

}
