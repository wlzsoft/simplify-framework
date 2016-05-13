package com.meizu.simplify.ioc.resolver;

import java.lang.annotation.Annotation;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.exception.StartupErrorException;
import com.meizu.simplify.ioc.BeanEntity;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.BeanHook;
import com.meizu.simplify.ioc.annotation.BeanPrototypeHook;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.BeanTypeEnum;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.hook.IBeanHook;
import com.meizu.simplify.ioc.hook.IBeanPrototypeHook;
import com.meizu.simplify.utils.ClassUtil;

/**
  * <p><b>Title:</b><i>对象创建处理解析器</i></p>
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
@Init(InitTypeEnum.BEAN)
public final class BeanAnnotationResolver implements IAnnotationResolver<Class<?>>{

	private static final Logger LOGGER = LoggerFactory.getLogger(BeanAnnotationResolver.class);
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		buildAnnotation(Bean.class);
	}

	public static <T extends Bean> void buildAnnotation(Class<T> clazzAnno) {
		List<Class<?>> resolveList;
		resolveList = ClassUtil.findClassesByAnnotationClass(clazzAnno, "com.meizu");
		for (Class<?> clazz : resolveList) {
			LOGGER.info("Bean 初始化:{}",clazz.getName());
			try {
				T beanAnnotation = clazz.getAnnotation(clazzAnno);
        		if(beanAnnotation.type().equals(BeanTypeEnum.PROTOTYPE)) {
        			List<Class<?>> hookList = ClassUtil.findClassesByAnnotationClass(BeanPrototypeHook.class, "com.meizu");
        			for (Class<?> hookClazz : hookList) {
						BeanPrototypeHook hookBeanAnno = hookClazz.getAnnotation(BeanPrototypeHook.class);
						Class<?> serviceClass = hookBeanAnno.value();
						if(serviceClass.equals(clazz)) {
							Object hookObj = hookClazz.newInstance();
							List<BeanEntity<?>> listObj = ((IBeanPrototypeHook)hookObj).hook(clazz);
							BeanFactory.addBeanList(listObj);
						}
					}
        		} else {
        			Object beanObj = null;
        			String beanName = null;
        			Class<?> hookClazz = getSingleHook(clazz);
        			if(hookClazz == null) {
						beanObj = clazz.newInstance();
						beanName = clazz.getName();
        			} else {
        				Object hookObj = hookClazz.newInstance();
        				BeanEntity<?> beanObjBean = ((IBeanHook)hookObj).hook(clazz);
						if (null == beanObjBean) {
							LOGGER.error(clazz.getName()+"实例处理返回空，没有生成注入到容器中的bean对象");
	        				continue;
						}
        				beanObj = beanObjBean.getBeanObj();
        				beanName = beanObjBean.getName();
        				String returnBeanName = beanObj.getClass().getName();
        				if(returnBeanName != clazz.getName()) {
        					throw new StartupErrorException("bean:类型为"+clazz.getName()+"的bean实例处理返回的对象类型为:"+returnBeanName+"类型不匹配");
        				}
        			}
        			
        			if(beanObj == null) {
        				LOGGER.error("bean:类型为"+clazz.getName()+"的bean实例处理返回空，没有生成注入到容器中的bean对象");
        				continue;
        			}
        			BeanFactory.addBean(beanName,beanObj);
        		}
				
			} catch (InstantiationException e) {
				e.printStackTrace();
				LOGGER.debug("bean:"+clazz.getName()+"初始化失败==>>请检查是否构造函数执行过程出错");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				LOGGER.debug("bean:"+clazz.getName()+"初始化失败==>>或是构造函数未提供，或设置为私有的");
			}
			
		}
	}

	/**
	 * 
	 * 方法用途: 获取单例bean处理hook<br>
	 * 操作步骤: TODO<br>
	 * @param clazz
	 */
	private static Class<?> getSingleHook(Class<?> clazz) {
		List<Class<?>> hookList = ClassUtil.findClassesByAnnotationClass(BeanHook.class, "com.meizu");
		for (Class<?> hookClazz : hookList) {
			BeanHook hookBeanAnno = hookClazz.getAnnotation(BeanHook.class);
			Class<?> annoClass = hookBeanAnno.value();
			Annotation[] annos = clazz.getAnnotations();
			for (Annotation anno : annos) {
				if(annoClass.equals(anno.annotationType())) {
					return hookClazz;
				}
			}
		}
		return null;
	}
}
