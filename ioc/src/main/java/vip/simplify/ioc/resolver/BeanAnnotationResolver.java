package vip.simplify.ioc.resolver;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.Constants;
import vip.simplify.dto.BeanMetaDTO;
import vip.simplify.exception.StartupErrorException;
import vip.simplify.ioc.BeanEntity;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.BeanHook;
import vip.simplify.ioc.annotation.BeanPrototypeHook;
import vip.simplify.ioc.annotation.Init;
import vip.simplify.ioc.enums.BeanTypeEnum;
import vip.simplify.ioc.enums.InitTypeEnum;
import vip.simplify.ioc.hook.IBeanHook;
import vip.simplify.ioc.hook.IBeanPrototypeHook;
import vip.simplify.utils.ClassUtil;
import vip.simplify.utils.PropertieUtil;
import vip.simplify.utils.StringUtil;
import vip.simplify.utils.clazz.ClassInfo;

/**
  * <p><b>Title:</b><i>Bean对象创建处理解析器</i></p>
 * <p>Desc: 注意：该无法启用容器托管，不允许使用bean注解，因为容器需要通过这个类初始化并创建，它先于Bean容器执行</p>
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
		ClassInfo<BeanMetaDTO> beanMetaDTOClassInfo = ClassMetaResolver.getBeanClassMap().get(beanClass);
		buildBeanObjAction(Bean.class,beanMetaDTOClassInfo);
	}

	public static <T extends Bean> void buildAnnotation(Class<T> clazzAnno) {
		List<ClassInfo<BeanMetaDTO>> resolvePreCoreList = new ArrayList<>();//提供预先构建bean的数据源
		List<ClassInfo<BeanMetaDTO>> resolveExtendList = new ArrayList<>();//提供扩展构建bean的数据源
		for (Map.Entry<Class<?>,ClassInfo<BeanMetaDTO>> clazzInfoEntry : ClassMetaResolver.getBeanClassMap().entrySet()) {
			ClassInfo<BeanMetaDTO> clazzInfo = clazzInfoEntry.getValue();
			Class<?> clazz = clazzInfo.getClazz();
            Annotation[] annoArr = clazz.getAnnotations();
			if (annoArr.length == 1) {//只包含bean注解
				resolvePreCoreList.add(clazzInfo);
			} else {
				resolveExtendList.add(clazzInfo);
			}
		}
		buildAllBeanAction(clazzAnno, resolvePreCoreList);
		buildAllBeanAction(clazzAnno, resolveExtendList);
	}

	/**
	 * 方法用途: 获取bean的classpath<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static String[] getClasspaths() {
		String[] classpathArr = new String[]{Constants.packagePrefix};
		PropertieUtil propertieUtil = new PropertieUtil("properties/config.properties",false);
		String classpaths = propertieUtil.getString("system.classpaths");
		if(StringUtil.isNotBlank(classpaths)) {
			boolean hasSystemPath = false;
			for (String classpath : classpaths.split(",")) {
				if(classpath.equals(classpathArr[0])) {
					hasSystemPath = true; 
					break;
				}
			}
			if(!hasSystemPath&&!classpaths.contains(classpathArr[0])) {
				classpaths = classpathArr[0]+","+classpaths;
			}
			classpathArr = classpaths.split(",");
		}
		return classpathArr;
	}

	/**
	 * 方法用途: 开始构建指定class列表中的所有bean实例<br>
	 * 操作步骤: TODO<br>
	 * @param clazzAnno
	 * @param resolveList
	 */
	private static <T extends Bean> void buildAllBeanAction(Class<T> clazzAnno, List<ClassInfo<BeanMetaDTO>> resolveList) {
		for (ClassInfo<BeanMetaDTO> clazzInfo : resolveList) {
			buildBeanObjAction(clazzAnno, clazzInfo);
			
		}
	}

	/**
	 * 方法用途: 创建指定类型的bean实例<br>
	 * 操作步骤: TODO<br>
	 * @param clazzAnno
	 * @param clazzInfo
	 */
	private static <T extends Bean> void buildBeanObjAction(Class<T> clazzAnno, ClassInfo<BeanMetaDTO> clazzInfo) {
		Class<?> clazz = clazzInfo.getClazz();
		LOGGER.debug("Bean 开始初始化:{}",clazz.getName());
		try {
			BeanMetaDTO beanMetaDTO = clazzInfo.getInfo();
			if(beanMetaDTO.getType().equals(BeanTypeEnum.PROTOTYPE)) {//同类型多例处理
				List<Class<?>> hookList = ClassUtil.findClassesByAnnotationClass(BeanPrototypeHook.class, BeanAnnotationResolver.getClasspaths());
				for (Class<?> hookClazz : hookList) {
					BeanPrototypeHook hookBeanAnno = hookClazz.getAnnotation(BeanPrototypeHook.class);
					Class<?> serviceClass = hookBeanAnno.value();
					if(serviceClass.equals(clazz)) {
						Object hookObj = hookClazz.newInstance();
						@SuppressWarnings({ "unchecked", "rawtypes" })
						List<BeanEntity<?>> listObj = ((IBeanPrototypeHook)hookObj).hook(clazz);
						BeanFactory.addBeanList(listObj);
						LOGGER.info("Bean 已创建:{}(BeanPrototypeHook模式)",clazz.getName());
					}
				}
			} else {//同类型单例处理，只会返回一个实例
				Object beanObj = null;
				String beanName = null;
				BeanHookDTO<?> beanHookDTO = getSingleHook(beanMetaDTO);
				Class<?> hookClazz = null;
				if (beanHookDTO != null) {
					hookClazz = beanHookDTO.getHookClazz();
				}
				if(hookClazz == null) {
					beanObj = clazz.newInstance();
					beanName = clazz.getName();
				} else {//三种情况会异常：1.BeanEntity的beanName为空  2.BeanEntity的beanName和interfaceName不一致 3.bean的class列表中的clazz的name和interfaceName不一致
					Object hookObj = hookClazz.newInstance();
					BeanEntity<?> beanObjBean = ((IBeanHook)hookObj).hook(clazz,beanHookDTO.getAnnotation());
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
						} else {//如不包含，寻找相关的业务类，用于错误提示，[包名为vip.simplify要做成可配置 ,适应不同的包名结构TODO]
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
				String beanAnnoVal = beanMetaDTO.getValue();
				if(StringUtil.isNotBlank(beanAnnoVal)) {
					beanName = beanAnnoVal;
				}
				BeanFactory.addBean(beanName,beanObj);
				LOGGER.info("Bean 已创建:{}",clazz.getName());
			}
			
		} catch (InstantiationException e) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("bean:"+clazz.getName()+"初始化失败==>>请检查是否构造函数执行过程出错",e);
			}
		} catch (IllegalAccessException e) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("bean:"+clazz.getName()+"初始化失败==>>或是构造函数未提供，或设置为私有的",e);
			}
		} catch(Error e) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("bean:"+clazz.getName()+"初始化失败==>>请检查出现的致命错误",e);
			}
		} catch(Throwable e) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("bean:"+clazz.getName()+"初始化失败==>>请检查出现的未知异常",e);
			}
		}
	}

	/**
	 * 
	 * 方法用途: 获取单例bean处理hook<br>
	 * 操作步骤: TODO<br>
	 * @param beanMetaDTO
	 */
	private static BeanHookDTO<Annotation> getSingleHook(BeanMetaDTO beanMetaDTO) {
		List<Class<?>> hookList = ClassUtil.findClassesByAnnotationClass(BeanHook.class, BeanAnnotationResolver.getClasspaths());
		for (Class<?> hookClazz : hookList) {
			BeanHook hookBeanAnno = hookClazz.getAnnotation(BeanHook.class);
			Class<?> annoClass = hookBeanAnno.value();
			Annotation[] beanAnnoArr = beanMetaDTO.getAnnotationArr();
			if (beanAnnoArr == null) {
				return null;
			}
			for (Annotation beanAnno : beanAnnoArr) {
				if (annoClass.equals(beanAnno.annotationType())) {
					BeanHookDTO<Annotation> beanHookDTO = new BeanHookDTO<>();
					beanHookDTO.setHookClazz(hookClazz);
					beanHookDTO.setAnnotation(beanAnno);
					return beanHookDTO;
				}
			}
		}
		return null;
	}

	static class BeanHookDTO<T extends Annotation> {
		private Class<?> hookClazz;
		private T annotation;

		public Class<?> getHookClazz() {
			return hookClazz;
		}

		public void setHookClazz(Class<?> hookClazz) {
			this.hookClazz = hookClazz;
		}

		public T getAnnotation() {
			return annotation;
		}

		public void setAnnotation(T annotation) {
			this.annotation = annotation;
		}
	}
}
