package vip.simplify.ioc.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.simplify.dto.AttributeMetaDTO;
import vip.simplify.dto.BeanMetaDTO;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.BeanConfig;
import vip.simplify.ioc.annotation.Init;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.ioc.enums.InitTypeEnum;
import vip.simplify.utils.ClassUtil;
import vip.simplify.utils.ReflectionUtil;
import vip.simplify.utils.clazz.ClassInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
  * <p><b>Title:</b><i>基于BeanConfig注解的Bean对象创建处理解析器</i></p>
 * <p>Desc: TODO </p>
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
@Init(InitTypeEnum.BEAN_PLUGINS)
public final class BeanConfigAnnotationResolver implements IAnnotationResolver<Class<?>>{

	private static final Logger LOGGER = LoggerFactory.getLogger(BeanConfigAnnotationResolver.class);
	public static final Map<Class<?>,BeanConfig> beanConfigClassMap = new ConcurrentHashMap<>();

	@Override
	public void resolve(List<Class<?>> resolveList) {
		//1.load带Bean注解的类，并缓存起来
		Map<Class<?>,ClassInfo<BeanMetaDTO>> beanClassMap = ClassMetaResolver.getBeanClassMap();
		//2.load带BeanConfig注解的配置类
		List<Class<?>> beanConfigClasses = ClassUtil.findClassesByAnnotationClass(BeanConfig.class, BeanAnnotationResolver.getClasspaths());
		//3.解析配置类中带BeanConfig注解的属性和方法
		for (Class<?> beanConfigClass : beanConfigClasses) {
			List<Field> fieldList = ReflectionUtil.getAllField(beanConfigClass);
			for (Field field : fieldList) {
				//4.解析带BeanConfig注解的属性，并提取对应Class对象
				BeanConfig beanConfig = field.getAnnotation(BeanConfig.class);
				if(beanConfig != null) {
					Class<?> fieldClazz = field.getType();
					//5.添加提取的beanClass到Bean的类集合中，等待BeanAnnotationResover类的解析,由于BeanConfig目前没有携带其他信息，所以不会复杂注入操作，后续会为BeanConfig增加更多的配置,另外要处理Bean和BeanConfig注解信息重叠的问题
					ClassInfo<BeanMetaDTO> classInfo = buildBeanMeta(fieldClazz, beanConfig);
					beanClassMap.put(classInfo.getClazz(),classInfo);
					beanConfigClassMap.put(classInfo.getClazz(),beanConfig);
				}
			}
			List<Method> methodList = ReflectionUtil.getAllMethod(beanConfigClass);
			Object beanObj = ReflectionUtil.newInstance(beanConfigClass);
			for (Method method : methodList) {
				//4.解析带BeanConfig注解的方法
				BeanConfig beanConfig = method.getAnnotation(BeanConfig.class);
				if(beanConfig != null) {
					Object obj = ReflectionUtil.invokeMethod(beanObj,method,null,false);
					if(obj == null) {
						//该bean配置无效，bean实例为null
						LOGGER.debug(beanConfigClass.getName()+":"+method.getName()+"的beanConfig配置无效，bean实例为null");
						continue;
					}
					if(obj instanceof Class) {
						//5.添加提取的beanClass到Bean的类集合中，等待BeanAnnotationResover类的解析,由于BeanConfig目前没有携带其他信息，所以不会复杂注入操作，后续会为BeanConfig增加更多的配置,另外要处理Bean和BeanConfig注解信息重叠的问题
						ClassInfo<BeanMetaDTO> classInfo = buildBeanMeta((Class) obj, beanConfig);
						beanClassMap.put(classInfo.getClazz(),classInfo);
						beanConfigClassMap.put(classInfo.getClazz(),beanConfig);
					} else {
						//5.直接添加到Bean容器中，跳过了BeanAnnotationResover类的解析,解析和注入的过程，在Bean配置类中就已经完成
						BeanFactory.addBean(obj);
					}
				}
			}
		}
	}

	private ClassInfo<BeanMetaDTO> buildBeanMeta(Class<?> clazz, BeanConfig beanConfig) {
		BeanMetaDTO beanMetaDTO = new BeanMetaDTO();
		beanMetaDTO.setSourceName(Bean.class.getName());
		beanMetaDTO.setType(beanConfig.type());
		beanMetaDTO.setValue(beanConfig.value());
//		attribute start
		List<AttributeMetaDTO> attributeMetaDTOList = new ArrayList<>();
		List<Field> fieldList = ReflectionUtil.getAllField(clazz);
		for (Field field : fieldList) {
			for (Class<?> fieldClass : beanConfig.attributes()) {
				if (field.getType() == fieldClass) {
					AttributeMetaDTO attributeMetaDTO = new AttributeMetaDTO();
					attributeMetaDTO.setName("");
					attributeMetaDTO.setType(fieldClass);//BeanConfig方式只支持类型Type注入
					attributeMetaDTO.setDesc("");
					attributeMetaDTO.setField(field);
					attributeMetaDTOList.add(attributeMetaDTO);
					break;
				}
			}
		}
//		attribute end
		beanMetaDTO.setAttributeMetaDTOList(attributeMetaDTOList);
		//增加注解信息，目前主要用于BeanHook解析匹配
		beanMetaDTO.setAnnotationArr(beanConfig.annoType());
		ClassInfo<BeanMetaDTO> classInfo = new ClassInfo<>();
		classInfo.setClazz(clazz);
		classInfo.setInfo(beanMetaDTO);
		return classInfo;
	}

}
