package vip.simplify.dao.template;

import java.util.Map;

import vip.simplify.ioc.BeanFactory;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月5日 下午6:15:51</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月5日 下午6:15:51</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class SqlTemplateFactory {
	public static String getSql(String sqlName, Map<String, Object> parameters) {
		return BeanFactory.getBean(SqlTemplate.class).render(sqlName, parameters);
	}
}
