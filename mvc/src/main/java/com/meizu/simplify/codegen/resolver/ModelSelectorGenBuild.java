package com.meizu.simplify.codegen.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import com.meizu.simplify.mvc.model.Model;
import com.meizu.simplify.template.BeetlTemplate;
import com.meizu.simplify.utils.ClassPathUtil;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.ReflectionUtil;
import com.meizu.simplify.utils.StringUtil;
/**
 * <p><b>Title:</b><i>mvc的model注入的代码生成处理</i></p>
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
public class ModelSelectorGenBuild {
	private static final Logger LOGGER = LoggerFactory.getLogger(ModelSelectorGenBuild.class);
	public static void init(String modelClassPath,CodeGenUtil gen) {
		/**
		 * <类名,类对应方法列表>
		 */
		Map<Class<?>,List<Method>> methodMap = new HashMap<>();
		// 查找指定class路径
		if (modelClassPath != null) {
			String[] classPathArr = modelClassPath.split(",");
			for (String cpath : classPathArr) {
				List<Class<?>> modelClassList = ClassUtil.findClassesByParentClass(Model.class, cpath);
				Class<Annotation> entityClass = null;
				try {
					@SuppressWarnings({"unchecked" })
					Class<Annotation> entityClassTemp = (Class<Annotation>) Class.forName("com.meizu.simplify.entity.annotations.Entity");
					entityClass = entityClassTemp;
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
					throw new UncheckedException("Entity注解加载失败");
				}
				List<Class<?>> entityClassList = ClassUtil.findClassesByAnnotationClass(entityClass, cpath);
				modelClassList.addAll(entityClassList);
				if (modelClassList == null || modelClassList.size()<=0) {
					throw new UncheckedException("代码生成：没有扫描到配置的路径["+cpath+"]有任何Model被注册，请检查config.properties文件system.modelClasspath的配置");
				}
				for (Class<?> modelClass : modelClassList) {
					List<Method> methodArr = null;
					try {
//						methodArr = modelClass.getDeclaredMethods();
						methodArr = ReflectionUtil.getAllMethod(modelClass);
					} catch(NoClassDefFoundError e) {
						e.printStackTrace();
						throw new BaseException("代码生成：bean["+modelClass.getName()+"] 无法找到bean中方法依赖的第三方class，确认是否缺少class文件==>"+e.getMessage());
					}
					if(methodArr.size()>0) {
						methodMap.put(modelClass, methodArr);
					}
				}
			}
			Map<String,Object> parameters = new HashMap<>();
			Set<Entry<Class<?>, List<Method>>> set = methodMap.entrySet();
			List<Map<String,String>> modelTagList = new ArrayList<>();
			List<Map<String,Object>> modelMethodTagList = new ArrayList<>();
			for (Entry<Class<?>, List<Method>> entry : set) {
				//类信息抽取
				Map<String,String> map = new HashMap<>();
				map.put("clazz", entry.getKey().getName());
				map.put("value", entry.getKey().getSimpleName().toLowerCase());
				modelTagList.add(map);
				//方法信息抽取
				List<Method> methodInfoList = entry.getValue();
				for (Method method : methodInfoList) {
					Map<String,Object> subMap = new HashMap<>();
					subMap.put("clazz", entry.getKey().getSimpleName());
					subMap.put("obj", entry.getKey().getSimpleName().toLowerCase());
					Class<?>[] parameterTypes = method.getParameterTypes();
					subMap.put("value", method.getName());
					subMap.put("fieldName", StringUtil.lowerCaseByFirst(method.getName().substring(3)));
					subMap.put("returnType", method.getReturnType().getName());
					boolean isStatic = Modifier.isStatic(method.getModifiers());
					boolean isFinal = Modifier.isFinal(method.getModifiers());
					subMap.put("isStatic", isStatic);
					subMap.put("isFinal", isFinal);
					subMap.put("params", parameterTypes);
					//方法参数类型抽取
					modelMethodTagList.add(subMap);
				}
			}
			parameters.put("tagList", modelTagList);
			parameters.put("methodTagList", modelMethodTagList);
			String javaFileName = "GenModelSelector.java";
			String codegenPath = ClassPathUtil.getClassPath().replace("/classes", "")+"codegen/com/meizu/simplify/codegen/";
			gen.gen(parameters, codegenPath,javaFileName);
			LOGGER.info("Framework codegen [model代码已生成==>>"+codegenPath+javaFileName+"]");
		}
	}
	/**
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param args com.meizu.demo.mvc.model,com.meizu.demo.mvc.entity 多个参数用逗号隔开
	 */
	public static void main(String[] args) {
		CodeGenUtil gen = new CodeGenUtil(new BeetlTemplate());
		init(args[0], gen);
	}
}