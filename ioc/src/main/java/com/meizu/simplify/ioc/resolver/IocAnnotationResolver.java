package com.meizu.simplify.ioc.resolver;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.BeanContainer;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.IInterfaceHandler;
import com.meizu.simplify.ioc.annotation.DefaultBean;
import com.meizu.simplify.ioc.annotation.HandleInterface;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.ObjectUtil;

/**
  * <p><b>Title:</b><i>依赖注入解析器</i></p>
 * <p>Desc: 注意：无法启用容器托管，不允许使用bean注解</p>
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
@Init(InitTypeEnum.IOC)
public final class IocAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(IocAnnotationResolver.class);
	@Override
	public void resolve(List<Class<?>> resolveList) {
		BeanContainer container = BeanFactory.getBeanContainer();
		Map<String, Object> mapContainer = container.getMapContainer();
		Set<String> containerCollection = mapContainer.keySet();
		for (String beanName : containerCollection) {
			resolveBeanObj(beanName);
		}
	}
	/**
	 * 方法用途: 解析执行bean实例，并进行依赖注入<br>
	 * 操作步骤: TODO<br>
	 * @param beanObj
	 */
	@Override
	public void resolveBeanObj(String beanName) {
		Object beanObj = BeanFactory.getBean(beanName);
		Class<?> beanClass = beanObj.getClass();
		injectObjectForResourceAnno(beanObj, beanClass);
		
		Class<?>  parentClass = null;
		while((parentClass = beanClass.getSuperclass()) != null) {
			if(parentClass == Class.class) {
				break;
			}
			injectObjectForResourceAnno(beanObj, parentClass);
			beanClass = parentClass;
		}
	}
	/**
	 * 
	 * 方法用途: 为指定Resource的注解指定属性注入对象<br>
	 * 操作步骤: TODO<br>
	 * @param beanObj
	 * @param beanClass
	 */
	private void injectObjectForResourceAnno(Object beanObj, Class<?> beanClass) {
		Field[] fieldArr = beanClass.getDeclaredFields();
		for (Field field : fieldArr) {
		    if (field.isAnnotationPresent(Resource.class)) {
		    	Resource resource = field.getAnnotation(Resource.class);
		    	String resourceName = resource.name();
		    	if(ObjectUtil.isNull(resourceName)) {
		    		resourceName = "";
		    	}
		    	
		    	
		    	Class<?> iocType = field.getType();
		    	String message = "依赖注入属性初始化: "+field.getDeclaringClass().getTypeName()+"["+iocType.getTypeName()+":"+field.getName()+"]";
		    	if(!resourceName.trim().equals("")) {
		    		message+="==>>注入多例中的["+resourceName+"]实例";
		    	}
		    	Object iocBean = null;
		    	if(iocType.isInterface()) {
		    		List<Class<?>> clazzList = ClassUtil.findClassesByInterfaces(iocType,"com.meizu");
		    		int clazzSize = clazzList.size();
		    		if(clazzSize>1) {
		    			DefaultBean defaultBean = iocType.getAnnotation(DefaultBean.class);
		    			if(defaultBean == null) {
		    				throw new UncheckedException("接口："+iocType.getName()+"不允许有多个实现类，如果需要多个实现类并存，请使用@DefaultBean注解");
		    			}
		    			iocType = getDefaultBean(iocType,defaultBean);
		    		} else if(clazzSize<1) {
		    			LOGGER.debug("接口："+iocType.getName()+"无实现类，无法注入bean");
		    			//throw new UncheckedException("接口："+iocType.getName()+"无实现类，无法注入bean");
		    		} else {
		    			iocType = clazzList.get(0);
		    		}
		    	}
		    	if(!resourceName.trim().equals("")) {
		    		iocBean = BeanFactory.getBean(resourceName);
		    	} else {
		    		iocBean = BeanFactory.getBean(iocType);
		    	}
		    	try {
		    		field.setAccessible(true);
					field.set(beanObj, iocBean);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	LOGGER.debug(message);
		    }
		}
	}
	/**
	 * 
	 * 方法用途: 获取默认bean类型<br>
	 * 操作步骤: TODO<br>
	 * @param iocType 
	 * @param defaultBean
	 * @return
	 */
	private Class<?> getDefaultBean(Class<?> iocType, DefaultBean defaultBean) {
		Class<?> defaultBeanClass = defaultBean.value();
		if(defaultBeanClass != Object.class) {
			return defaultBeanClass;
		} 
		List<Class<?>> handleInterfaceList = ClassUtil.findClassesByAnnotationClass(HandleInterface.class, "com.meizu");
		if(handleInterfaceList.size() < 1) {
			throw new UncheckedException("没有查找到相应的@HandleInterface注解标注的类，确保指定扫描包名正确["+"com.meizu"+"]");
		}
		for (Class<?> handleInterfaceClass : handleInterfaceList) {
			Object obj = BeanFactory.getBean(handleInterfaceClass);
			if(obj == null) {
				throw new UncheckedException("handleInterface："+handleInterfaceClass.getName()+"对应的bean对象为空，确认是否已经注入到bean容器中，确定是否类上已经标注@bean注解");
			}
			HandleInterface hi = obj.getClass().getAnnotation(HandleInterface.class);
			if(hi == null) {
				throw new UncheckedException("handleInterface："+handleInterfaceClass.getName()+"不包含@HandleInterface注解");
			}
			if(hi.value() == iocType) {
				IInterfaceHandler iih = (IInterfaceHandler) obj;
				return iih.handle();
			} 
		}
		throw new UncheckedException("接口："+iocType.getName()+"没有对应的标注了@HandleInterface("+iocType.getName()+".class)注解的handle处理类");
	}
}
