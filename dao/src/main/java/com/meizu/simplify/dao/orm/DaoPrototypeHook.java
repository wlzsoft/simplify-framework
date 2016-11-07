package com.meizu.simplify.dao.orm;
/**
  * <p><b>Title:</b><i>dao多例工厂钩子函数</i></p>
 * <p>Desc: 用于生成dao实例，以entity类型为唯一实例，进行初始化</p>
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

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.dao.exception.BaseDaoException;
import com.meizu.simplify.entity.IdEntity;
import com.meizu.simplify.entity.annotations.Entity;
import com.meizu.simplify.ioc.BeanEntity;
import com.meizu.simplify.ioc.annotation.BeanPrototypeHook;
import com.meizu.simplify.ioc.hook.IBeanPrototypeHook;
import com.meizu.simplify.ioc.resolver.BeanAnnotationResolver;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.CollectionUtil;
import com.meizu.simplify.utils.ReflectionUtil;

@BeanPrototypeHook(Dao.class)
public class DaoPrototypeHook implements IBeanPrototypeHook<Dao<IdEntity<Serializable,Integer>, Serializable>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoPrototypeHook.class);

	@Override
	public List<BeanEntity<Dao<IdEntity<Serializable, Integer>, Serializable>>> hook(
			Class<Dao<IdEntity<Serializable, Integer>, Serializable>> clazz) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("开始初始化Dao实例....");
		}
		List<BeanEntity<Dao<IdEntity<Serializable, Integer>, Serializable>>> list = new ArrayList<>();
		List<Class<?>> entityClasses = ClassUtil.findClassesByAnnotationClass(Entity.class, BeanAnnotationResolver.getClasspaths());//扫描Entity注解的实体，获取实体列表
//		循环ORM对象列表
		if (CollectionUtil.isNotEmpty(entityClasses)) {
			for (Class<?> entityClass : entityClasses) {

				/*String beanName = entityClass.getSimpleName();
				char[] chars = beanName.toCharArray();
				chars[0] = Character.toLowerCase(chars[0]);
				beanName = new String(chars) + "BaseDao";*///bugfix: 修复没有包名情况的冲突问题[getSimpleName改成getName,无需设置第一个字母为小写] lcy 2016/8/15
				String beanName = entityClass.getName();
				beanName = beanName + "BaseDao";
				BeanEntity<Dao<IdEntity<Serializable, Integer>, Serializable>> beanEntity = new BeanEntity<>();
				beanEntity.setName(beanName);
				Dao<IdEntity<Serializable, Integer>, Serializable> dao = buildDaoObject(clazz, entityClass);
				beanEntity.setBeanObj(dao);
				list.add(beanEntity);
				LOGGER.info("已注入bean:DAO[{}]", beanName);
			}
		}
		
		return list;
	}

	/**
	 * 
	 * 方法用途: 构建dao对象<br>
	 * 操作步骤: 分析注解和泛型参数，并设置构造函数带泛型的参数值，最后初始化Dao，设置构造函数参数<br>
	 * @param clazz
	 * @param entityClass
	 * @return
	 */
	private <T> T buildDaoObject(Class<T> clazz, Class<?> entityClass) {
		T dao = null;
		try {
			Constructor<T> constructor = clazz.getDeclaredConstructor(Class.class);
			dao = ReflectionUtil.newInstance(constructor,entityClass);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new BaseDaoException("构建dao对象失败",e);
		}
		return dao;
	}

	

}
