package com.meizu.simplify.codegen.resolver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.codegen.ControllerMethodCodeGen;
import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.exception.BaseException;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.BeanContainer;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.webcache.web.CacheBase;

/**
  * <p><b>Title:</b><i>mvc模块初始化解析</i></p>
 * <p>Desc: 1.初始化mvc数据参数
 *          2.mvc请求地址解析器</p>
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
@Init(InitTypeEnum.METHOD_GEN)
public class MethodSelectorGenResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodSelectorGenResolver.class);
	private PropertiesConfig config = BeanFactory.getBean(PropertiesConfig.class);
	private ControllerMethodCodeGen gen = BeanFactory.getBean(ControllerMethodCodeGen.class);
	
	private String classPath; 
	
	@Override	
	public void resolve(List<Class<?>> resolveList) {
		CacheBase.init();
		init();
	}
	
	public void init() {
		/**
		 * <controll类名,controll类对应requestMap方法列表>
		 */
		Map<Class<?>,List<Map<String,Object>>> methodMap = new HashMap<>();
//		String webcharSet = config.getWebcharSet();
//		String directives = config.getDirectives(); 
		classPath = config.getControllerClasspath(); 
		// 查找指定class路径
		if (classPath != null) {
			String[] classPathArr = classPath.split(",");
			for (String cpath : classPathArr) {
				List<Class<?>> controllerClassList = ClassUtil.findClasses(cpath);
				if (controllerClassList == null || controllerClassList.size()<=0) {
					throw new UncheckedException("代码生成：没有扫描到配置的路径["+cpath+"]有任何Controller被注册，请检查config.properties文件system.classpath的配置");
				}
				for (Class<?> controllerClass : controllerClassList) {
					BeanContainer container = BeanFactory.getBeanContainer();
					Map<String, Object> mapContainer = container.getMapContainer();
					Collection<Object> containerCollection = mapContainer.values();
					for (Object beanObj : containerCollection) {
						Class<?> beanClass = beanObj.getClass();
						if(controllerClass == beanClass) {
							Method[] methodArr = null;
							try {
								methodArr = beanClass.getDeclaredMethods();
							} catch(NoClassDefFoundError e) {
								e.printStackTrace();
								throw new BaseException("代码生成：bean["+beanClass.getName()+"] 无法找到bean中方法依赖的第三方class，确认是否缺少class文件==>"+e.getMessage());
							}
							List<Map<String,Object>> methodList = new ArrayList<>();
							for (Method method : methodArr) {
								if (method.isAnnotationPresent(RequestMap.class)) {
									Class<?>[] parameterTypes = method.getParameterTypes();
									Map<String,Object> params = new HashMap<>();
									params.put("methodName", method.getName());
									params.put("returnType", method.getReturnType().getName());
									params.put("params", parameterTypes);
									methodList.add(params);
								}
							}
							if(methodList.size()>0) {
								methodMap.put(beanClass, methodList);
							}
						}
					}
				}
			}
			Map<String,Object> parameters = new HashMap<>();
			Set<Entry<Class<?>, List<Map<String,Object>>>> set = methodMap.entrySet();
			List<Map<String,String>> controllerTagList = new ArrayList<>();
			List<Map<String,Object>> controllerMethodTagList = new ArrayList<>();
			for (Entry<Class<?>, List<Map<String,Object>>> entry : set) {
				//controll类信息抽取
				Map<String,String> map = new HashMap<>();
				map.put("clazz", entry.getKey().getName());
				map.put("value", entry.getKey().getSimpleName().toLowerCase());
				controllerTagList.add(map);
				//方法信息抽取
				List<Map<String,Object>> methodInfoList = entry.getValue();
				for (Map<String,Object> mi : methodInfoList) {
					Map<String,Object> subMap = new HashMap<>();
					subMap.put("clazz", entry.getKey().getSimpleName());
					subMap.put("obj", entry.getKey().getSimpleName().toLowerCase());
					subMap.put("returnType", mi.get("returnType"));
					subMap.put("value", String.valueOf(mi.get("methodName")));
					subMap.put("params", mi.get("params"));
					//方法参数类型抽取
					controllerMethodTagList.add(subMap);
				}
			}
			parameters.put("controllerTagList", controllerTagList);
			parameters.put("controllerMethodTagList", controllerMethodTagList);
			String javaFileName = "GenMethodSelector.java";
			String codegenPath = getClass().getClassLoader().getResource("").getPath().replace("classes", "")+"codegen/";
			gen.gen(parameters, codegenPath,javaFileName);
			LOGGER.info("Framework codegen [controll代码已生成==>>"+codegenPath+javaFileName+"]");
		}
	}
	
}
