package com.meizu.dao.orm;
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
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.BeanHook;
import com.meizu.simplify.ioc.prototype.IBeanPrototypeHook;
import com.meizu.simplify.utils.DataUtil;

@BeanHook(Dao.class)
public class DaoPrototypeHook implements IBeanPrototypeHook {

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoPrototypeHook.class);
	@Override
	public List<?> hook(Class<?> clazz) {
		List<Object> list = new ArrayList<>();
		list.add(new Dao(this.getClass()));
		list.add(new Dao(this.getClass()));
		return list;
	}
	
	//@Value(value="${system.init.bean}")
		private Boolean initBean;
//		@Override
		public void postProcessBeanFactory(BeanFactory beanFactory) {
			//ApplicatonContext还未注入，SpringContext的时机未到
			//Object obj = SpringContext.getInstance("config");
			Properties obj = (Properties) beanFactory.getBean("config");
			if(obj!=null) {
				initBean = DataUtil.parseBoolean(obj.get("system.init.bean"));
			}
			if(initBean !=null && !initBean) {
				return;
			}
			/*if (CollectionUtil.isNotEmpty(entityClasses)) {
				for (Class<?> entityClass : entityClasses) {
					AnnotatedGenericBeanDefinition daoDefinition = new AnnotatedGenericBeanDefinition(
							Dao.class);

					ConstructorArgumentValues av = new ConstructorArgumentValues();
					av.addGenericArgumentValue(entityClass);
//					av.addGenericArgumentValue(1);
					daoDefinition.setConstructorArgumentValues(av);

					String beanName = entityClass.getSimpleName();
					char[] chars = beanName.toCharArray();
					chars[0] = Character.toLowerCase(chars[0]);
					beanName = new String(chars) + "BaseDao";
					((DefaultListableBeanFactory) beanFactory)
							.registerBeanDefinition(beanName, daoDefinition);
					log.info("已注入bean[{}]", beanName);
				}
			}*/
		}

}
