package com.meizu.simplify.dao.orm;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.entity.IdEntity;
import com.meizu.simplify.entity.annotations.Entity;
import com.meizu.simplify.entity.annotations.Table;
import com.meizu.simplify.entity.annotations.Transient;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.CollectionUtil;

/**
  * <p><b>Title:</b><i>依赖注入解析器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 上午11:08:36</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 上午11:08:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Init(InitTypeEnum.DB_INIT)
public class DdlInit implements IAnnotationResolver<Class<?>>{//TODO 后续可以resove使用注解的方式，而不用实现特定接口，而且不一定是解析注解
	private static final Logger LOGGER = LoggerFactory.getLogger(DdlInit.class);
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		List<Class<?>> entityClasses = ClassUtil.findClassesByAnnotationClass(Entity.class, "com.meizu");//扫描Entity注解的实体，获取实体列表
//		循环ORM对象列表
		if (CollectionUtil.isNotEmpty(entityClasses)) {
			for (Class<?> entityClass : entityClasses) {
				Table table = entityClass.getAnnotation(Table.class);
				Transient ts = entityClass.getAnnotation(Transient.class);
				int isresult = BaseDao.getIns(entityClass.getSimpleName()).createTable((Class<IdEntity<Serializable, Integer>>) entityClass);
				if(isresult>0) {
					LOGGER.info("已创建数据库表[{}],忽略属性[{}]", table.name(),ts.value());
				} else {
					//LOGGER.info("数据库表已经存在，无需创建[{}],忽略属性[{}]", table.name(),ts.value());
				}
			}
		}
	}
}
