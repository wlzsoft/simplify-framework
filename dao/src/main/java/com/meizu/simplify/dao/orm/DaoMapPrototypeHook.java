package com.meizu.simplify.dao.orm;
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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.dao.annotations.Entity;
import com.meizu.simplify.dao.annotations.Value;
import com.meizu.simplify.dao.config.PropertiesConfig;
import com.meizu.simplify.dao.exception.BaseDaoException;
import com.meizu.simplify.ioc.BeanEntity;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.BeanHook;
import com.meizu.simplify.ioc.prototype.IBeanPrototypeHook;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.CollectionUtil;
import com.meizu.simplify.utils.PropertieUtil;
import com.meizu.simplify.utils.ReflectionUtil;

//@BeanHook(Dao.class) //TODO
public class DaoMapPrototypeHook implements IBeanPrototypeHook {//需要进一步优化和重构 TODO

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoMapPrototypeHook.class);

	@Value("${system.init.bean}")
	private Boolean initBean;
	
	@Override
	public List<BeanEntity<?>> hook(Class<?> clazz) {
		
		PropertiesConfig obj = BeanFactory.getBean(PropertiesConfig.class);
		PropertieUtil propertieUtil = obj.getProperties();
		if(propertieUtil!=null) {
			initBean = propertieUtil.getBoolean("system.init.bean");
		}
		if(initBean !=null && !initBean) {
			return null;
		}
		LOGGER.debug("开始初始化Dao实例[基于通用类型map]....");
		List<BeanEntity<?>> list = new ArrayList<>();
				String beanName = "mapBaseDao";
				BeanEntity<Object> beanEntity = new BeanEntity<>();
				beanEntity.setName(beanName);
				Object dao = buildDaoObject(clazz, HashMap.class);
				beanEntity.setBeanObj(dao);
				list.add(beanEntity);
				LOGGER.info("已注入beanMap:DAO[{}]", beanName);
		
		return list;
	}

	/**
	 * 
	 * 方法用途: 构建dao对象<br>
	 * 操作步骤: 分析注解和泛型参数，并设置构造函数带泛型的参数值，最后初始化Dao，设置构造函数参数<br>
	 * @param clazz dao实体
	 * @param entityClass dao构造函数注入的参数值，实体的class
	 * @return
	 */
	private Object buildDaoObject(Class<?> clazz, Class<?> entityClass) {
		Object dao = null;
		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(Class.class);
			dao = ReflectionUtil.instantiateClass(constructor,entityClass);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new BaseDaoException("构建dao对象失败",e);
		}
		return dao;
	}
	

}
