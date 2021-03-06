package vip.simplify.log.hook;

import org.slf4j.LoggerFactory;
import vip.simplify.dto.AttributeMetaDTO;
import vip.simplify.dto.BeanMetaDTO;
import vip.simplify.exception.UncheckedException;
import vip.simplify.ioc.BeanEntity;
import vip.simplify.ioc.annotation.BeanPrototypeHook;
import vip.simplify.ioc.hook.IBeanPrototypeHook;
import vip.simplify.ioc.resolver.ClassMetaResolver;
import vip.simplify.log.Logger;
import vip.simplify.utils.CollectionUtil;
import vip.simplify.utils.ReflectionUtil;
import vip.simplify.utils.clazz.ClassInfo;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p><b>Title:</b><i>Log多例工厂钩子函数</i></p>
 * <p>Desc: 用于生成Log实例，以是否标注@Resource来注入实例，而并非是标注Bean注解
 *          注意：比较特殊。1.和rpc的client实例有点类似,并非自己可以编写的类实例，无法通过Bean注解来解析
 *                         2.不能对所有业务类都创建一个日志实体，因为有的业务类不需要记录日志。业务类是指标注了Bean注解的类
 *          TODO 后续考虑第三方库的实例如果在不能@Bean的方法是初始化，是否通过配置方式</p>
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
@BeanPrototypeHook(Logger.class)
public class LogPrototypeHook implements IBeanPrototypeHook<Logger> {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LogPrototypeHook.class);

	@Override
	public List<BeanEntity<Logger>> hook(Class<Logger> clazz) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("开始初始化SLFJ的Log实例....");
		}
		List<BeanEntity<Logger>> list = new ArrayList<>();
		Map<Class<?>,ClassInfo<BeanMetaDTO>> beanClassMap = ClassMetaResolver.getBeanClassMap();//扫描Entity注解的实体，获取实体列表,这里必须直接从缓存中获取
		if (CollectionUtil.isNotEmpty(beanClassMap)) {
			for (Map.Entry<Class<?>,ClassInfo<BeanMetaDTO>> beanClassInfoEntry : beanClassMap.entrySet()) {
				ClassInfo<BeanMetaDTO> beanClassInfo = beanClassInfoEntry.getValue();
				Class<?> beanClass = beanClassInfo.getClazz();
				BeanMetaDTO beanMetaDTO = beanClassInfo.getInfo();
				//1.过滤无需添加到Bean容器中的对象-过滤条件是：BeanClass中没有标注@Inject private Logger的属性
				List<AttributeMetaDTO> attributeMetaDTOList = beanMetaDTO.getAttributeMetaDTOList();
				for (AttributeMetaDTO attributeMetaDTO : attributeMetaDTOList) {
					if(attributeMetaDTO.getField().getType().equals(Logger.class)) {
						//2.构建添加到Bean容器中的对象
						String beanName = beanClass.getName();
						beanName = beanName + "Log";
						BeanEntity<Logger> beanEntity = new BeanEntity<>();
						beanEntity.setName(beanName);
						Logger logger = buildLogObject(clazz, beanClass);
						beanEntity.setBeanObj(logger);
						list.add(beanEntity);
						LOGGER.info("已注入bean:LOG[{}]", beanName);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 
	 * 方法用途: 构建Log对象<br>
	 * 操作步骤: 分析注解和泛型参数，并设置构造函数带泛型的参数值，最后初始化Log，设置构造函数参数<br>
	 * @param clazz
	 * @param beanClass
	 * @return
	 */
	private <T> T buildLogObject(Class<T> clazz, Class<?> beanClass) {
		T dao = null;
		try {
			Constructor<T> constructor = clazz.getDeclaredConstructor(Class.class);
			dao = ReflectionUtil.newInstance(constructor,beanClass);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new UncheckedException("构建Log对象失败",e);
		}
		return dao;
	}
}
