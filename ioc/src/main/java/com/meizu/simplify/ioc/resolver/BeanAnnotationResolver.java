package com.meizu.simplify.ioc.resolver;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.Constants;
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
import com.meizu.simplify.utils.StringUtil;

/**
  * <p><b>Title:</b><i>对象创建处理解析器</i></p>
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
@Init(InitTypeEnum.BEAN)
public final class BeanAnnotationResolver implements IAnnotationResolver<Class<?>>{

	private static final Logger LOGGER = LoggerFactory.getLogger(BeanAnnotationResolver.class);
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		buildAnnotation(Bean.class);
	}

	@Override
	public void resolveBeanObj(String beanName) {
		Class<?> beanClass = BeanFactory.getBean(beanName).getClass();
		buildBeanObjAction(Bean.class,beanClass);
	}

	public static <T extends Bean> void buildAnnotation(Class<T> clazzAnno) {
		List<Class<?>> resolveList = ClassUtil.findClassesByAnnotationClass(clazzAnno, Constants.packagePrefix);//提供构建bean的总数据源
		List<Class<?>> resolvePreCoreList = new ArrayList<>();//提供预先构建bean的数据源
		List<Class<?>> resolveExtendList = new ArrayList<>();//提供扩展构建bean的数据源
		for (Class<?> clazz : resolveList) {
			Annotation[] annoArr = clazz.getAnnotations();
			if(annoArr.length==1) {//只包含bean注解
				resolvePreCoreList.add(clazz);
			} else {
				resolveExtendList.add(clazz);
			}
		}
		buildAllBeanAction(clazzAnno, resolvePreCoreList);
		buildAllBeanAction(clazzAnno, resolveExtendList);
	}

	/**
	 * 方法用途: 开始构建指定class列表中的所有bean实例<br>
	 * 操作步骤: TODO<br>
	 * @param clazzAnno
	 * @param resolveList
	 */
	private static <T extends Bean> void buildAllBeanAction(Class<T> clazzAnno, List<Class<?>> resolveList) {
		for (Class<?> clazz : resolveList) {
			buildBeanObjAction(clazzAnno, clazz);
			
		}
	}

	/**
	 * 方法用途: 创建指定类型的bean实例<br>
	 * 操作步骤: TODO<br>
	 * @param clazzAnno
	 * @param clazz
	 */
	private static <T extends Bean> void buildBeanObjAction(Class<T> clazzAnno, Class<?> clazz) {
		LOGGER.info("Bean 初始化:{}",clazz.getName());
		try {
			T beanAnnotation = clazz.getAnnotation(clazzAnno);
			if(beanAnnotation.type().equals(BeanTypeEnum.PROTOTYPE)) {//同类型多例处理
				List<Class<?>> hookList = ClassUtil.findClassesByAnnotationClass(BeanPrototypeHook.class, Constants.packagePrefix);
				for (Class<?> hookClazz : hookList) {
					BeanPrototypeHook hookBeanAnno = hookClazz.getAnnotation(BeanPrototypeHook.class);
					Class<?> serviceClass = hookBeanAnno.value();
					if(serviceClass.equals(clazz)) {
						Object hookObj = hookClazz.newInstance();
						@SuppressWarnings({ "unchecked", "rawtypes" })
						List<BeanEntity<?>> listObj = ((IBeanPrototypeHook)hookObj).hook(clazz);
						BeanFactory.addBeanList(listObj);
					}
				}
			} else {//同类型单例处理，只会返回一个实例
				Object beanObj = null;
				String beanName = null;
				Class<?> hookClazz = getSingleHook(clazz);
				if(hookClazz == null) {
					beanObj = clazz.newInstance();
					beanName = clazz.getName();
				} else {//三种情况会异常：1.BeanEntity的beanName为空  2.BeanEntity的beanName和interfaceName不一致 3.bean的class列表中的clazz的name和interfaceName不一致
					Object hookObj = hookClazz.newInstance();
					BeanEntity<?> beanObjBean = ((IBeanHook)hookObj).hook(clazz);
					if (null == beanObjBean) {
						LOGGER.error(clazz.getName()+"实例处理返回空，没有生成注入到容器中的bean对象");
						return;
					}
					beanObj = beanObjBean.getBeanObj();
					beanName = beanObjBean.getName();
					String returnBeanName = null;
					String interfaceName = null;
					Class<?>[] interfaces = beanObj.getClass().getInterfaces();
					for (Class<?> interfaceClass : interfaces) {//抽取出真实类的接口
						if(interfaceClass.getName().equals(clazz.getName())) {//如果包含，返回beanName为null
							returnBeanName = interfaceClass.getName();
							if(beanName == null) {
								beanName = returnBeanName;
								beanObjBean.setName(returnBeanName);
							}
							break;
						} else {//如不包含，寻找相关的业务类，用于错误提示，[包名为com.meizu要做成可配置 ,适应不同的包名结构TODO]
							if(interfaceClass.getName().startsWith(Constants.packagePrefix)) {
								interfaceName = interfaceClass.getName();
							}
						}
					}
					if(beanName == null||beanName.equals("")) {
						throw new StartupErrorException("bean:类型为"+clazz.getName()+"的BeanEntity实例处理返回的对象名Name属性为空,请检查确认");
					}
					if(returnBeanName ==null) {
						String endError = "";
						if(!beanName.equals(interfaceName)) {
							endError = ",并且BeanEntity返回的Name值:"+beanName+"和真实的类型:"+interfaceName+"的类型不一致";
						}
						throw new StartupErrorException("bean:类型为"+clazz.getName()+"的bean实例处理返回的对象类型为:"+interfaceName+"类型不匹配"+endError);
					}
				}
				
				if(beanObj == null) {
					LOGGER.error("bean:类型为"+clazz.getName()+"的bean实例处理返回空，没有生成注入到容器中的bean对象");
					return;
				}
				//lcy add 2016/6/3 增加Bean注解的value属性的处理
				String beanAnnoVal = beanAnnotation.value();
				if(StringUtil.isNotBlank(beanAnnoVal)) {
					beanName = beanAnnoVal;
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

	/**
	 * 
	 * 方法用途: 获取单例bean处理hook<br>
	 * 操作步骤: TODO<br>
	 * @param clazz
	 */
	private static Class<?> getSingleHook(Class<?> clazz) {
		List<Class<?>> hookList = ClassUtil.findClassesByAnnotationClass(BeanHook.class, Constants.packagePrefix);
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
