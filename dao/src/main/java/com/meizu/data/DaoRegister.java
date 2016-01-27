package com.meizu.data;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.meizu.EntityClassBeanFactoryPostProcessor;
import com.meizu.data.mybatis.Dao;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.utils.CollectionUtil;
import com.meizu.simplify.utils.DataUtil;

/**
 * 该类定义了一个Dao注册器组件。它实现了BeanFactoryPostProcessor接口，
 * 在Spring容器初始化完成后自动根据sessionFactory的packagesToScan指定的路径查找业务实体类
 * ，并为这些业务实体类注册相应的泛型Dao组件。这样就省去了在配置文件中逐个为业务实体声明对应的Dao组件。
 */
@Bean
public class DaoRegister /*extends EntityClassBeanFactoryPostProcessor*/ {
	private final Logger log = LoggerFactory.getLogger(getClass());

	//@Value(value="${system.init.bean}")
	private Boolean initBean;
//	@Override
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
//				av.addGenericArgumentValue(1);
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