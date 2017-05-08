package vip.simplify.ioc.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.simplify.dto.AttributeMetaDTO;
import vip.simplify.dto.BeanMetaDTO;
import vip.simplify.exception.UncheckedException;
import vip.simplify.ioc.BeanContainer;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.IInterfaceHandler;
import vip.simplify.ioc.annotation.*;
import vip.simplify.ioc.enums.InitTypeEnum;
import vip.simplify.ioc.hook.IIocHook;
import vip.simplify.utils.ClassUtil;
import vip.simplify.utils.ObjectUtil;
import vip.simplify.utils.clazz.ClassInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

		for (Map.Entry<Class<?>,ClassInfo<BeanMetaDTO>> clazzInfoEntry : ClassMetaResolver.getStaticTypeClassInfoMap().entrySet()) {
			ClassInfo<BeanMetaDTO> staticTypeClassInfo = clazzInfoEntry.getValue();
			injectObjectForResourceAnno(null,staticTypeClassInfo);
		}
	}
	/**
	 * 方法用途: 解析执行bean实例，并进行依赖注入<br>
	 * 操作步骤: TODO<br>
	 * @param beanName
	 */
	@Override
	public void resolveBeanObj(String beanName) {
		Object beanObj = BeanFactory.getBean(beanName);
		Class<?> beanClass = beanObj.getClass();
		ClassInfo<BeanMetaDTO> beanMetaDTOClassInfo = ClassMetaResolver.getBeanClassMap().get(beanClass);
		if(beanMetaDTOClassInfo != null) { //不带Bean注解的父类，也会执行，所以需要过来掉
			injectObjectForResourceAnno(beanObj,beanMetaDTOClassInfo);
		}
	}
	
	/**
	 * 
	 * 方法用途: 为指定Resource的注解指定属性注入对象<br>
	 * 操作步骤: TODO<br>
	 * @param beanObj
	 * @param classInfo 这个属性在包含本类和所有父类的属性
	 */
	private void injectObjectForResourceAnno(Object beanObj,ClassInfo<BeanMetaDTO> classInfo) {
		List<AttributeMetaDTO> attributeMetaDTOList = classInfo.getInfo().getAttributeMetaDTOList();
		if(attributeMetaDTOList == null) {
			return;
		}
		Class<?> currentBeanClass = classInfo.getClazz();
		for (AttributeMetaDTO attributeMetaDTO : attributeMetaDTOList) {
			Field field = attributeMetaDTO.getField();
		    if (field.isAnnotationPresent(Inject.class)) {
		    	Inject inject = field.getAnnotation(Inject.class);
		    	String resourceName = inject.name();
		    	if(ObjectUtil.isNull(resourceName)) {
		    		resourceName = "";
		    	}
		    	
		    	Class<?> iocType = field.getType();
		    	String message = "依赖注入属性初始化: "+field.getDeclaringClass().getTypeName()+"["+iocType.getTypeName()+":"+field.getName()+"]";
		    	if(!resourceName.trim().equals("")) {
		    		message+="==>>注入多例中的["+resourceName+"]实例";
		    	}
		    	Object iocBean = null;
		    	Class<?> hookClazz = getIocHook(iocType);
	    		if(hookClazz!= null) {//定义钩子执行
					try {
						Object hookObj = hookClazz.newInstance();
						resourceName  = ((IIocHook)hookObj).hook(field.getDeclaringClass(),field);
						if (null == resourceName) {
							LOGGER.error(field.getDeclaringClass().getName()+"依赖注入类型["+field.getDeclaringClass().getTypeName()+"["+iocType.getTypeName()+":"+field.getName()+"]返回空，注入失败");
							continue;
						}
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
						continue;
					}
	    		} else {//正常注入流程执行
	    			if(iocType.isInterface()||Modifier.isAbstract(iocType.getModifiers())) {//FIXED author:lcy date:2016/5/20 desc:增加抽象类支持
	    				List<Class<?>> clazzList = ClassUtil.findClassesByParentClass(iocType,BeanAnnotationResolver.getClasspaths());
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
	    		}
		    	if(!resourceName.trim().equals("")) {
		    		iocBean = BeanFactory.getBean(resourceName);
		    	} else {
		    		iocBean = BeanFactory.getBean(iocType);
		    	}
		    	try {
		    		field.setAccessible(true);
		    		//下面的beanObj 可以为null，前提是field是static的
//		    		int modifyValue = field.getModifiers();
//		    		if(Modifier.isStatic(modifyValue)||Modifier.isFinal(modifyValue)) {
//		    			field.set(null, iocBean);
//		    		} else {
		    			field.set(beanObj, iocBean);//注意，不同的ClassLoader实例所load的class，是属于不同的类型,这里会注入失败，异常。
//		    		}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					//下面不能使用beanClass参数，需要通过beanObj.getClass()获取，因为beanClass有可能是bean的parent Class
					if(currentBeanClass.getClassLoader() != iocBean.getClass().getClassLoader()) {//相同的ClassLoader才可以注入，避免java.lang.IllegalArgumentException: Can not set vip.simplify.xxx(UnsafeFieldAccessorImpl)异常
						LOGGER.error("注入失败：参数无效异常==>>不能设置"+currentBeanClass.getName()+"对象的"+iocBean.getClass().getName()+":"+field.getName()+"属性的值，因为将要设置的值的ClassLoader:"+iocBean.getClass().getClassLoader()+"和目标对象的ClassLoader:"+currentBeanClass.getClassLoader()+"不是同一个对象，要求必须是同一个ClassLoader类型并且是同一个对象，否则认为是不同的类型");
					}
					e.printStackTrace();
				}
		    	LOGGER.debug(message);
		    }
		}
	}
	
	/**
	 * 
	 * 方法用途: 依赖注入处理hook<br>
	 * 操作步骤: TODO 待处理 和 BeanAnnotationResolver.getSingleHook方法重复，可复用这个代码<br>
	 * @param clazz
	 */
	private static Class<?> getIocHook(Class<?> clazz) {
		List<Class<?>> hookList = ClassUtil.findClassesByAnnotationClass(IocHook.class, BeanAnnotationResolver.getClasspaths());
		for (Class<?> hookClazz : hookList) {
			IocHook hookIocAnno = hookClazz.getAnnotation(IocHook.class);
			Class<?> hookIocClass = hookIocAnno.value();
			if(hookIocClass.equals(clazz)) {
				return hookClazz;
			}
		}
		return null;
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
		List<Class<?>> handleInterfaceList = ClassUtil.findClassesByAnnotationClass(HandleInterface.class, BeanAnnotationResolver.getClasspaths());
		if(handleInterfaceList.size() < 1) {
			throw new UncheckedException("没有查找到相应的@HandleInterface注解标注的类，确保指定扫描包名正确["+BeanAnnotationResolver.getClasspaths()+"]");
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
