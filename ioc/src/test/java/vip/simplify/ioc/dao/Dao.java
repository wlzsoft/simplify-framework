package vip.simplify.ioc.dao;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.enums.BeanTypeEnum;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月11日 上午10:59:33</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月11日 上午10:59:33</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean(type=BeanTypeEnum.PROTOTYPE)
public class Dao {
	public String getSql() {
		return "select * from table;";
	}
}
