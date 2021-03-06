package vip.simplify.ioc.dao;
/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月11日 上午10:51:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月11日 上午10:51:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */

import java.util.ArrayList;
import java.util.List;

import vip.simplify.ioc.BeanEntity;
import vip.simplify.ioc.annotation.BeanPrototypeHook;
import vip.simplify.ioc.hook.IBeanPrototypeHook;

@BeanPrototypeHook(Dao.class)
public class DaoPrototypeHook implements IBeanPrototypeHook<Dao> {

	@Override
	public List<BeanEntity<Dao>> hook(Class<Dao> clazz) {
		List<BeanEntity<Dao>> list = new ArrayList<>();
		BeanEntity<Dao> beanEntity = new BeanEntity<>();
		beanEntity.setName("test1BaseDao");
		beanEntity.setBeanObj(new Dao());
		list.add(beanEntity);
		
		BeanEntity<Dao> beanEntity2 = new BeanEntity<>();
		beanEntity2.setName("test2BaseDao");
		beanEntity2.setBeanObj(new Dao());
		list.add(beanEntity2);
		return list;
	}
	

}
