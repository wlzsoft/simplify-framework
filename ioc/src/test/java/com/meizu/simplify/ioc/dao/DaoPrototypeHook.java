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

import com.meizu.simplify.ioc.BeanEntity;
import com.meizu.simplify.ioc.annotation.BeanHook;
import com.meizu.simplify.ioc.prototype.IBeanPrototypeHook;

@BeanHook(Dao.class)
public class DaoPrototypeHook implements IBeanPrototypeHook {

	@Override
	public List<BeanEntity<?>> hook(Class<?> clazz) {
		List<BeanEntity<?>> list = new ArrayList<>();
		BeanEntity<Object> beanEntity = new BeanEntity<>();
		beanEntity.setName("test1BaseDao");
		beanEntity.setBeanObj(new Dao());
		list.add(beanEntity);
		
		BeanEntity<Object> beanEntity2 = new BeanEntity<>();
		beanEntity2.setName("test2BaseDao");
		beanEntity2.setBeanObj(new Dao());
		list.add(beanEntity2);
		return list;
	}

}
