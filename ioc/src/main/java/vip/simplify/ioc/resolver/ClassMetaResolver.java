package vip.simplify.ioc.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.simplify.Constants;
import vip.simplify.dto.AttributeMetaDTO;
import vip.simplify.dto.BeanMetaDTO;
import vip.simplify.exception.StartupErrorException;
import vip.simplify.ioc.BeanEntity;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.annotation.*;
import vip.simplify.ioc.enums.BeanTypeEnum;
import vip.simplify.ioc.enums.InitTypeEnum;
import vip.simplify.ioc.hook.IBeanHook;
import vip.simplify.ioc.hook.IBeanPrototypeHook;
import vip.simplify.utils.ClassUtil;
import vip.simplify.utils.PropertieUtil;
import vip.simplify.utils.ReflectionUtil;
import vip.simplify.utils.StringUtil;
import vip.simplify.utils.clazz.ClassInfo;
import vip.simplify.utils.clazz.IFindClassCallBack;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
  * <p><b>Title:</b><i>Class元数据解析器</i></p>
 * <p>Desc: 支持提取Bean注解和StaticType注解的类信息，以及这些注解对应类的属性的注解等元数据信息的提取，比如属性上的@Inject</p>
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
public final class ClassMetaResolver {

	private static  Map<Class<?>,ClassInfo<BeanMetaDTO>> beanClassInfoMap;
	
	/**
	 * 方法用途: 获取bean的Class对象列表<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Map<Class<?>,ClassInfo<BeanMetaDTO>> getBeanClassMap() {
		if (beanClassInfoMap == null) {
			beanClassInfoMap = ClassUtil.findClassesByAnnotationClass(Bean.class, new IFindClassCallBack<BeanMetaDTO>() {
				@Override
				public BeanMetaDTO resolve(Class<?> c) {
					Bean bean = c.getAnnotation(Bean.class);
					BeanMetaDTO beanMetaDTO = new BeanMetaDTO();
					beanMetaDTO.setSourceName(Bean.class.getName());
					beanMetaDTO.setType(bean.type());
					beanMetaDTO.setValue(bean.value());
					List<Field> fieldList = ReflectionUtil.getAllField(c);
					List<AttributeMetaDTO> attributeMetaDTOList = resolverFieldMeta(fieldList);
					beanMetaDTO.setAttributeMetaDTOList(attributeMetaDTOList);
					Annotation[] annoArr = c.getAnnotations();
					beanMetaDTO.setAnnotationArr(annoArr);
					return beanMetaDTO;
				}
			}, BeanAnnotationResolver.getClasspaths());//提供构建bean的总数据源
		}
		return beanClassInfoMap;
	}
	private static Map<Class<?>,ClassInfo<BeanMetaDTO>> staticTypeClassInfoMap = ClassUtil.findClassesByAnnotationClass(StaticType.class, new IFindClassCallBack<BeanMetaDTO>() {
		@Override
		public BeanMetaDTO resolve(Class<?> c) {
			BeanMetaDTO beanMetaDTO = new BeanMetaDTO();
			beanMetaDTO.setSourceName(StaticType.class.getName());
			List<Field> fieldList = ReflectionUtil.getAllField(c);
			List<AttributeMetaDTO> attributeMetaDTOList = resolverFieldMeta(fieldList);
			beanMetaDTO.setAttributeMetaDTOList(attributeMetaDTOList);
			Annotation[] annoArr = c.getAnnotations();
			beanMetaDTO.setAnnotationArr(annoArr);
			return beanMetaDTO;
		}
	}, BeanAnnotationResolver.getClasspaths());

	public static List<AttributeMetaDTO> resolverFieldMeta(List<Field> fieldList) {
		List<AttributeMetaDTO> attributeMetaDTOList = new ArrayList<>();
		for (Field field : fieldList) {
            if (field.isAnnotationPresent(Inject.class)) {
                Inject inject = field.getAnnotation(Inject.class);
                AttributeMetaDTO attributeMetaDTO = new AttributeMetaDTO();
                attributeMetaDTO.setName(inject.name());
                attributeMetaDTO.setType(inject.type());
                attributeMetaDTO.setDesc(inject.desc());
                attributeMetaDTO.setField(field);
                attributeMetaDTOList.add(attributeMetaDTO);
            }
        }
        return attributeMetaDTOList;
	}

	public static Map<Class<?>,ClassInfo<BeanMetaDTO>> getStaticTypeClassInfoMap() {
		return staticTypeClassInfoMap;
	}
	
}
