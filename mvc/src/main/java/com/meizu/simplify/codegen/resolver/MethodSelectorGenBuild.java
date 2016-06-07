package com.meizu.simplify.codegen.resolver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.codegen.CodeGenUtil;
import com.meizu.simplify.exception.BaseException;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.utils.ClassPathUtil;
import com.meizu.simplify.utils.ClassUtil;
/**
 * <p><b>Title:</b><i>controll方法代码生成处理</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月28日 下午6:03:04</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年4月28日 下午6:03:04</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class MethodSelectorGenBuild {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodSelectorGenBuild.class);
	public static void init(String controllerClassPath,CodeGenUtil gen) {
		/**
		 * <controll类名,controll类对应requestMap方法列表>
		 */
		Map<Class<?>,List<Map<String,Object>>> methodMap = new HashMap<>();
		// 查找指定class路径
		if (controllerClassPath != null) {
			String[] classPathArr = controllerClassPath.split(",");
			for (String cpath : classPathArr) {
				List<Class<?>> controllerClassList = ClassUtil.findClassesByAnnotationClass(Bean.class, cpath);
				if (controllerClassList == null || controllerClassList.size()<=0) {
					throw new UncheckedException("代码生成：没有扫描到配置的路径["+cpath+"]有任何Controller被注册，请检查config.properties文件system.controllerClasspath的配置");
				}
				for (Class<?> controllerClass : controllerClassList) {
					Method[] methodArr = null;
					try {
						methodArr = controllerClass.getDeclaredMethods();
					} catch(NoClassDefFoundError e) {
						e.printStackTrace();
						throw new BaseException("代码生成：bean["+controllerClass.getName()+"] 无法找到bean中方法依赖的第三方class，确认是否缺少class文件==>"+e.getMessage());
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
						methodMap.put(controllerClass, methodList);
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
			String codegenPath = ClassPathUtil.getClassPath().replace("/classes", "")+"codegen/com/meizu/simplify/codegen/";
			gen.gen(parameters, codegenPath,javaFileName);
			LOGGER.info("Framework codegen [controll代码已生成==>>"+codegenPath+javaFileName+"]");
		}
	}
}