package com.meizu.simplify.ioc.dao;
/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月11日 上午10:51:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月11日 上午10:51:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */

import java.util.ArrayList;
import java.util.List;

import com.meizu.simplify.ioc.annotation.BeanHook;
import com.meizu.simplify.ioc.prototype.IBeanPrototypeHook;

@BeanHook(Dao.class)
public class DaoPrototypeHook implements IBeanPrototypeHook {

	@Override
	public List<?> hook(Class<?> clazz) {
		List<Object> list = new ArrayList<>();
		list.add(new Dao());
		list.add(new Dao());
		return list;
	}

}
