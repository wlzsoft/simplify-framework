package vip.simplify.dao.dialect;

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
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public interface IDialectManager {

	/**
	 * 
	 * 方法用途: 根据方言名称获取方言对象<br>
	 * 操作步骤: TODO<br>
	 * @param dialectName 方言名称
	 * @return
	 */
	IDialect getDialect(String dialectName);

}
