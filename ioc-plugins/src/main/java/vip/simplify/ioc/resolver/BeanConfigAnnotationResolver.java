package vip.simplify.ioc.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.simplify.dto.AttributeMetaDTO;
import vip.simplify.dto.BeanMetaDTO;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.annotation.*;
import vip.simplify.ioc.enums.InitTypeEnum;
import vip.simplify.utils.ClassUtil;
import vip.simplify.utils.ReflectionUtil;
import vip.simplify.utils.clazz.ClassInfo;

import java.lang.annotation.Annotation;
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
	public static final Map<Class<?>,Bean> beanConfigClassMap = new ConcurrentHashMap<>();

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
				Bean bean = field.getAnnotation(Bean.class);
				if(bean != null) {
					Class<?> fieldClazz = field.getType();
					Injects injects = field.getAnnotation(Injects.class);
					//5.添加提取的beanClass到Bean的类集合中，等待BeanAnnotationResover类的解析,由于BeanConfig目前没有携带其他信息，所以不会复杂注入操作，后续会为BeanConfig增加更多的配置,另外要处理Bean和BeanConfig注解信息重叠的问题
					ClassInfo<BeanMetaDTO> classInfo = buildBeanMeta(beanConfigClass.getName(),fieldClazz, bean,field.getAnnotations(),injects);
					beanClassMap.put(classInfo.getClazz(),classInfo);
					beanConfigClassMap.put(classInfo.getClazz(),bean);
				}
			}
			List<Method> methodList = ReflectionUtil.getAllMethod(beanConfigClass);
			Object beanObj = ReflectionUtil.newInstance(beanConfigClass);
			for (Method method : methodList) {
				//4.解析带Bean注解的方法
				Bean bean = method.getAnnotation(Bean.class);
				if(bean != null) {
					Object obj = ReflectionUtil.invokeMethod(beanObj,method,null,false);
					if(obj == null) {
						//该bean配置无效，bean实例为null
						LOGGER.debug(beanConfigClass.getName()+":"+method.getName()+"的beanConfig配置无效，bean实例为null");
						continue;
					}
					if(obj instanceof Class) {
						Injects injects = method.getAnnotation(Injects.class);
						//5.添加提取的beanClass到Bean的类集合中，等待BeanAnnotationResover类的解析,由于BeanConfig目前没有携带其他信息，所以不会复杂注入操作，后续会为BeanConfig增加更多的配置,另外要处理Bean和BeanConfig注解信息重叠的问题
						ClassInfo<BeanMetaDTO> classInfo = buildBeanMeta(beanConfigClass.getName(),(Class) obj, bean,method.getAnnotations(),injects);
						beanClassMap.put(classInfo.getClazz(),classInfo);
						beanConfigClassMap.put(classInfo.getClazz(),bean);
					} else {
						//5.直接添加到Bean容器中，跳过了BeanAnnotationResover类的解析,解析和注入的过程，在Bean配置类中就已经完成
						BeanFactory.addBean(obj);
					}
				}
			}
		}
	}

	private ClassInfo<BeanMetaDTO> buildBeanMeta(String beanConfigName,Class<?> clazz, Bean bean, Annotation[] annotations, Injects injects) {
		BeanMetaDTO beanMetaDTO = new BeanMetaDTO();
		beanMetaDTO.setSourceName(Bean.class.getName());
		beanMetaDTO.setType(bean.type());
		beanMetaDTO.setValue(bean.value());
//		attribute start
		List<AttributeMetaDTO> attributeMetaDTOList = new ArrayList<>();
		if (injects != null) {
			Inject[] attributes =  injects.value();
			List<Field> fieldList = ReflectionUtil.getAllField(clazz);
			for (Inject inject : attributes) {
				if (inject.type() == Object.class) {
					LOGGER.error(beanConfigName+"的BeanConfig配置中：标注了@Bean注解的名为"+clazz.getName()+"的附属属性的@Inject注解的type属性为具体的注入实现类型");
					continue;
				}
				for (Field field : fieldList) {
					boolean isContainParent = ClassUtil.isContainParent(field.getType(),inject.type(),true);
					if (isContainParent) {
						AttributeMetaDTO attributeMetaDTO = new AttributeMetaDTO();
						attributeMetaDTO.setName(inject.name());
						attributeMetaDTO.setType(inject.type());
						attributeMetaDTO.setDesc(inject.desc());
						attributeMetaDTO.setField(field);
						attributeMetaDTOList.add(attributeMetaDTO);
						break;
					}
				}
			}
		}
		beanMetaDTO.setAttributeMetaDTOList(attributeMetaDTOList);
//		attribute end
		//增加注解信息，目前主要用于BeanHook解析匹配
		beanMetaDTO.setAnnotationArr(annotations);
		ClassInfo<BeanMetaDTO> classInfo = new ClassInfo<>();
		classInfo.setClazz(clazz);
		classInfo.setInfo(beanMetaDTO);
		return classInfo;
	}

}
