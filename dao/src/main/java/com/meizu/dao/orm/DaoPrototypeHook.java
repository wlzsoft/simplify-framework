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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.dao.annotations.Entity;
import com.meizu.dao.annotations.Value;
import com.meizu.dao.config.PropertiesConfig;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.BeanHook;
import com.meizu.simplify.ioc.prototype.IBeanPrototypeHook;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.CollectionUtil;
import com.meizu.simplify.utils.PropertieUtil;

@BeanHook(Dao.class)
public class DaoPrototypeHook implements IBeanPrototypeHook {

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoPrototypeHook.class);

	@Value("${system.init.bean}")
	private Boolean initBean;
	
	@Override
	public List<?> hook(Class<?> clazz) {
		
		PropertiesConfig obj = BeanFactory.getBean(PropertiesConfig.class);
		PropertieUtil propertieUtil = obj.getProperties();
		if(propertieUtil!=null) {
			initBean = propertieUtil.getBoolean("system.init.bean");
		}
		if(initBean !=null && !initBean) {
			return null;
		}
		LOGGER.debug("开始初始化Dao实例....");
		List<Object> list = new ArrayList<>();
		List<Class<?>> entityClasses = ClassUtil.findClassesByAnnotationClass(Entity.class, "com.meizu");//扫描Entity注解的实体，获取实体列表
//		循环ORM对象列表
		if (CollectionUtil.isNotEmpty(entityClasses)) {
			for (Class<?> entityClass : entityClasses) {

				String beanName = entityClass.getSimpleName();
				char[] chars = beanName.toCharArray();
				chars[0] = Character.toLowerCase(chars[0]);
				beanName = new String(chars) + "BaseDao";
//				list.add(new Dao(this.getClass()));//TODO 改造,支持beanname的定义
				LOGGER.info("已注入bean:DAO[{}]", beanName);
			}
		}
		
		return list;
	}

}
