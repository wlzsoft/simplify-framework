package com.meizu.simplify.ioc;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.exception.StartupErrorException;
import com.meizu.simplify.exception.StartupException;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.enums.StartupTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.CollectionUtil;
import com.meizu.simplify.utils.ReflectionUtil;

/**
  * <p><b>Title:</b><i>bean管理容器启动器</i></p>
 * <p>Desc: TODO 1.在第一次启动时，初始化到内存中，下次直接启动，无需重新执行一次分析：A.可以把容器序列化到磁盘上，B.可以序列化到redis中
 *          2.容器运行中，修改部分配置文件,一般指的的properties配置文件的修改，主要存在于properties目录下，A.通过reload指令触发重新加载配置,B.自动加载</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月7日 下午6:20:24</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月7日 下午6:20:24</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class Startup {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Startup.class);
	public static StartupTypeEnum start() {
		List<Class<?>> resolveList = ClassUtil.findClassesByParentClass(IAnnotationResolver.class, "com.meizu");
		Map<InitTypeEnum,Class<?>> mapResolve = new EnumMap<InitTypeEnum, Class<?>>(InitTypeEnum.class);
		for (Class<?> clazz : resolveList) {
			Init init = clazz.getAnnotation(Init.class);
			Class<?> clazzTemp = mapResolve.get(init.value());
			if(clazzTemp != null) {
				throw new StartupErrorException("容器启动时，有重复的bean解析操作:["+clazzTemp.getName()+"和"+clazz.getName()+"]冲突");
			}
			mapResolve.put(init.value(), clazz);
		}
		mapResolve = CollectionUtil.sortMapByKey(mapResolve, true);
		for (Class<?> clazz : mapResolve.values()) {
			LOGGER.info("resolver invoke:{}",clazz.getName());
			try {
				Object obj = clazz.newInstance();
				ReflectionUtil.invokeMethod(obj, "resolve", new Class[]{List.class}, new Object[]{new ArrayList<>()});
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(StartupException ex) {
				Throwable target = ex.getTargetException();
				if(target.getClass() == StartupErrorException.class) {
					System.err.println("系统终止服务，有致命异常 ==>>\r\n"+target.getMessage());
					System.exit(-1);
				}
//				ex.printStackTrace();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
				
		}
		return StartupTypeEnum.SUCCESS;
	}

}
