package com.meizu.simplify.codegen.resolver;

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
import com.meizu.simplify.dao.annotations.Entity;
import com.meizu.simplify.exception.BaseException;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.template.BeetlTemplate;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.ReflectionUtil;
/**
 * <p><b>Title:</b><i>dao的entity转sql的代码生成处理</i></p>
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
public class SqlByDaoEntityGenBuild {
	private static final Logger LOGGER = LoggerFactory.getLogger(SqlByDaoEntityGenBuild.class);
	public static void init(String entityClassPath,CodeGenUtil gen) {
		/**
		 * <类名,类对应requestMap方法列表>
		 */
		Map<Class<?>,List<Method>> methodMap = new HashMap<>();
		// 查找指定class路径
		if (entityClassPath != null) {
			String[] classPathArr = entityClassPath.split(",");
			for (String cpath : classPathArr) {
				List<Class<?>> entityClassList = ClassUtil.findClassesByAnnotationClass(Entity.class, cpath);
				if (entityClassList == null || entityClassList.size()<=0) {
					throw new UncheckedException("代码生成：没有扫描到配置的路径["+cpath+"]有任何Entity被注册，请检查config.properties文件system.entityClasspath的配置");
				}
				for (Class<?> entityClass : entityClassList) {
					List<Method> methodArr = null;
					try {
//						methodArr = entityClass.getDeclaredMethods();
						methodArr = ReflectionUtil.getAllMethod(entityClass);
					} catch(NoClassDefFoundError e) {
						e.printStackTrace();
						throw new BaseException("代码生成：bean["+entityClass.getName()+"] 无法找到bean中方法依赖的第三方class，确认是否缺少class文件==>"+e.getMessage());
					}
					if(methodArr.size()>0) {
						methodMap.put(entityClass, methodArr);
					}
				}
			}
			Map<String,Object> parameters = new HashMap<>();
			Set<Entry<Class<?>, List<Method>>> set = methodMap.entrySet();
			List<Map<String,String>> entityTagList = new ArrayList<>();
			List<Map<String,Object>> entityMethodTagList = new ArrayList<>();
			for (Entry<Class<?>, List<Method>> entry : set) {
				//类信息抽取
				Map<String,String> map = new HashMap<>();
				map.put("clazz", entry.getKey().getName());
				map.put("value", entry.getKey().getSimpleName().toLowerCase());
				entityTagList.add(map);
				//方法信息抽取
				List<Method> methodInfoList = entry.getValue();
				for (Method method : methodInfoList) {
					Map<String,Object> subMap = new HashMap<>();
					subMap.put("clazz", entry.getKey().getSimpleName());
					subMap.put("obj", entry.getKey().getSimpleName().toLowerCase());
					Class<?>[] parameterTypes = method.getParameterTypes();
					subMap.put("value", method.getName());
					subMap.put("returnType", method.getReturnType().getName());
					boolean isStatic = Modifier.isStatic(method.getModifiers());
					boolean isFinal = Modifier.isFinal(method.getModifiers());
					subMap.put("isStatic", isStatic);
					subMap.put("isFinal", isFinal);
					subMap.put("params", parameterTypes);
					//方法参数类型抽取
					entityMethodTagList.add(subMap);
				}
			}
			parameters.put("tagList", entityTagList);
			parameters.put("methodTagList", entityMethodTagList);
			String javaFileName = "GenSqlByDaoEntity.java";
			String codegenPath = ClassUtil.getClassPath().replace("/classes", "")+"codegen/com/meizu/simplify/codegen/";
			gen.gen(parameters, codegenPath,javaFileName);
			LOGGER.info("Framework codegen [dao代码已生成==>>"+codegenPath+javaFileName+"]");
		}
	}
	public static void main(String[] args) {
		CodeGenUtil gen = new CodeGenUtil(new BeetlTemplate());
		init("com.meizu.demo.mvc.entity", gen);
	}
}