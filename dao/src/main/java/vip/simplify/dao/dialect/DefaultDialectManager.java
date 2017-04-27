package vip.simplify.dao.dialect;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <p><b>Title:</b><i> 默认的数据库方言管理器实现类，对数据库方言对象进行管理。</i></p>
 * <p>Desc:
 * 目前已经实现的数据库方言包括：
 * <pre>
   	方言名称                      方言类
    mysql          vip.simplify.data.dialect.MySQLDialect
 * </pre>
 * 如果没有指定有效的方言名称，将使用默认的方言类vip.simplify.data.dialect.Dialect。</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午5:30:52</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午5:30:52</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class DefaultDialectManager implements IDialectManager {

	private Map<String, IDialect> dialects;

	public void setDialects(Map<String, IDialect> dialects) {
		this.dialects = dialects;
		initDialects();
	}

	private IDialect defaultDialect = new Dialect();

	protected void initDialects() {
		if (dialects == null) {
			dialects = new HashMap<String, IDialect>();
			dialects.put("mysql", new MySQLDialect());
		}
	}

	@Override
	public IDialect getDialect(String dialectName) {
		IDialect dialect = dialects.get(dialectName);
		if (dialect == null) {
			dialect = defaultDialect;
		}
		return dialect;
	}

}
