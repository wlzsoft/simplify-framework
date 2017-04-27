package vip.simplify.dao.resolver;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.config.annotation.Config;
import vip.simplify.dao.orm.BaseDao;
import vip.simplify.entity.IdEntity;
import vip.simplify.entity.annotations.Entity;
import vip.simplify.entity.annotations.Table;
import vip.simplify.entity.annotations.Transient;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Init;
import vip.simplify.ioc.enums.InitTypeEnum;
import vip.simplify.ioc.resolver.BeanAnnotationResolver;
import vip.simplify.ioc.resolver.IAnnotationResolver;
import vip.simplify.utils.ClassUtil;
import vip.simplify.utils.CollectionUtil;

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
@Bean
public class DdlInitResolver implements IAnnotationResolver<Class<?>>{//TODO 后续可以resove使用注解的方式，而不用实现特定接口，而且不一定是解析注解
	private static final Logger LOGGER = LoggerFactory.getLogger(DdlInitResolver.class);
	@Config("system.isInitDB")
	private boolean isInitDB;
	@Override
	public void resolve(List<Class<?>> resolveList) {
		if(!isInitDB) {
			return;
		}
		List<Class<?>> entityClasses = ClassUtil.findClassesByAnnotationClass(Entity.class, BeanAnnotationResolver.getClasspaths());//扫描Entity注解的实体，获取实体列表
//		循环ORM对象列表
		if (CollectionUtil.isNotEmpty(entityClasses)) {
			for (Class<?> entityClass : entityClasses) {
				Table table = entityClass.getAnnotation(Table.class);
				Transient ts = entityClass.getAnnotation(Transient.class);
				@SuppressWarnings("unchecked")
				int isresult = BaseDao.getIns(entityClass.getName()).createTable((Class<IdEntity<Serializable, Integer>>) entityClass);
				if(isresult>0) {
					LOGGER.info("已创建数据库表[{}],忽略属性[{}]", table.name(),ts.value());
				} else {
					//LOGGER.info("数据库表已经存在，无需创建[{}],忽略属性[{}]", table.name(),ts.value());
				}
			}
		}
	}
}
