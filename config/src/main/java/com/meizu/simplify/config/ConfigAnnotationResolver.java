package com.meizu.simplify.config;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.config.annotation.Config;
import com.meizu.simplify.config.annotation.ReloadableResource;
import com.meizu.simplify.ioc.BeanContainer;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.annotation.StaticType;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.BeanAnnotationResolver;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.DataUtil;
import com.meizu.simplify.utils.PropertieUtil;
import com.meizu.simplify.utils.ReflectionUtil;
import com.meizu.simplify.utils.StringUtil;

/**
  * <p><b>Title:</b><i>配置属性单个注入解析器</i></p>
 * <p>Desc: 注意：可启用容器托管，不建议启用容器托管,保证模块的独立性，不依赖容器,并且本身这个模块也是bean容器初始化的一个过程，本身配置的注解信息就无法获取到</p>
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
@Init(InitTypeEnum.CONFIG)
public class ConfigAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigAnnotationResolver.class);
	//配置文件路径信息列表，包含配置文件内容
	public static final Map<String,PropertieUtil> propertiesMap = new ConcurrentHashMap<>(4);
	private Map<String,Object> propertieBeanMap = new ConcurrentHashMap<>(4);
	@Override
	public void resolve(List<Class<?>> resolveList) {
		BeanContainer container = BeanFactory.getBeanContainer();
		Map<String, Object> mapContainer = container.getMapContainer();
		Collection<Object> containerCollection = mapContainer.values();
		
		//组装所有的配置文件配置信息
		for (Object beanObj : containerCollection) {
			Class<?> beanClass = beanObj.getClass();
			ReloadableResource reloadableResource = beanClass.getAnnotation(ReloadableResource.class);
			if(reloadableResource == null || StringUtil.isBlank(reloadableResource.value())) {
				continue;
			}
			LOGGER.info("配置属性单个注入 初始化:{}",beanClass.getName());
			String reloadableResourceValue = reloadableResource.value();
//			String prefix = reloadableResource.prefix();
			PropertieUtil propertieUtil = new PropertieUtil(reloadableResourceValue,false);
			propertiesMap.put(reloadableResourceValue, propertieUtil);
			propertieBeanMap.put(reloadableResourceValue, beanObj);
		}
		//开始注入到bean对应的带config注解的属性的值
		for (Object beanObj : containerCollection) {
			Class<?> beanClass = beanObj.getClass();
			injectObjectForConfigAnno(beanObj, beanClass);
			Class<?>  parentClass = null;
			while((parentClass = beanClass.getSuperclass()) != null) {
				if(parentClass == Class.class) {
					break;
				}
				injectObjectForConfigAnno(beanObj, parentClass);
				beanClass = parentClass;
			}
		}
		List<Class<?>> staticTypeList = ClassUtil.findClassesByAnnotationClass(StaticType.class, BeanAnnotationResolver.getClasspaths());
		for (Class<?> staticType : staticTypeList) {
			injectObjectForConfigAnno(null, staticType);
		}
		
	}
	private void injectObjectForConfigAnno(Object beanObj, Class<?> beanClass) {
		Field[] fieldArr = beanClass.getDeclaredFields();
		for (Field field : fieldArr) {
		    if (field.isAnnotationPresent(Config.class)) {
		    	Config config = field.getAnnotation(Config.class);
		    	String configName = config.value();
		    	if(StringUtil.isBlank(configName)) {
		    		configName = field.getName();
		    	}
		    	Class<?> iocType = field.getType();
		    	String message = "依赖注入配置文件属性初始化: "+field.getDeclaringClass().getTypeName()+"["+iocType.getTypeName()+":"+field.getName()+"]";
		    	if(StringUtil.isNotBlank(configName)) {
		    		Set<Entry<String, PropertieUtil>> propertiesSet = propertiesMap.entrySet();
		    		for (Entry<String, PropertieUtil> entry : propertiesSet) {
		    			String configPath = entry.getKey();
		    			message+="==>>注入的配置文件"+configPath+"中的["+configName+"]配置项";
		    			PropertieUtil propertiesUtil = entry.getValue();
		    			Object value = propertiesUtil.get(configName);
		    			if(value == null) {//如果properties文件中，无对应属性，那么读取properties的关联实体，获取默认值，关联实体的属性在配置文件中格式都是[xxxx.xxx],有带前缀的
		    				Object propertiesBeanObj = propertieBeanMap.get(configPath);
		    				Class<?> propertiesBeanClass = propertiesBeanObj.getClass();
		    				ReloadableResource reloadableResource = propertiesBeanClass.getAnnotation(ReloadableResource.class);
		    				if(StringUtil.isNotBlank(reloadableResource.prefix())) {
		    					configName = configName.replace(reloadableResource.prefix()+".", "");
		    				}
		    				try {
		    					value = ReflectionUtil.invokeGetterMethod(propertiesBeanObj, configName);//读取带有ReloadableResource的Bean实例中的属性默认值
		    				}catch(java.lang.IllegalArgumentException iae) {
		    				}
		    				if(value == null) {
		    					continue;
		    				}
		    			} else {//配置文件中格式都是[xxx],不带前缀的，那么需要做类型转换，避免出错。
		    				value = DataUtil.convertType(field.getType(), value, false);
		    			}
		    			try {
		    				field.setAccessible(true);
		    				field.set(beanObj, value);
		    			} catch (IllegalArgumentException | IllegalAccessException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		    			break;
		    		}
		    	}
		    	LOGGER.debug(message);
		    }
		}
	}
}
