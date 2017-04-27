package vip.simplify.ioc;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.exception.StartupErrorException;
import vip.simplify.exception.StartupException;
import vip.simplify.ioc.annotation.Init;
import vip.simplify.ioc.enums.InitTypeEnum;
import vip.simplify.ioc.enums.StartupTypeEnum;
import vip.simplify.ioc.resolver.BeanAnnotationResolver;
import vip.simplify.ioc.resolver.IAnnotationResolver;
import vip.simplify.utils.ClassUtil;
import vip.simplify.utils.CollectionUtil;
import vip.simplify.utils.DateUtil;

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
public final class Startup {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Startup.class);
	
	/**
	 * 方法用途: 容器启动入口方法<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static StartupTypeEnum start() {
		StartupTypeEnum status = startCallback(null,new AnnoCallback() {
			@Override
			public void invoke(IAnnotationResolver<Class<?>> ir, Class<?> beanObj) {
				ir.resolve(new ArrayList<>());
			}
		});
		LOGGER.info("Start in " + DateUtil.format(new Date()));
		return status;
	}
	
	/**
	 * 方法用途: 容器的单个bean实例的解析处理入口<br>
	 * 操作步骤: TODO<br>
	 * @param beanClass
	 * @return
	 */
	public static StartupTypeEnum startBeanObj(Class<?> beanClass) {
		return startCallback(beanClass,new AnnoCallback() {
			@Override
			public void invoke(IAnnotationResolver<Class<?>> ir, Class<?> beanClass) {
				ir.resolveBeanObj(beanClass.getName());
			}
		});
	}
	
	/**
	 * 方法用途: 获取启用时注解解析处理器的处理器列表<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Map<InitTypeEnum, Class<?>> getAnnotationResolverList() {
		List<Class<?>> resolveList = ClassUtil.findClassesByParentClass(IAnnotationResolver.class, BeanAnnotationResolver.getClasspaths());
		return getAnnotationResolverList(resolveList);
	}
	
	/**
	 * 方法用途: 获取启用时注解解析处理器的处理器列表<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Map<InitTypeEnum, Class<?>> getAnnotationResolverList(List<Class<?>> resolveList) {
		Map<InitTypeEnum,Class<?>> mapResolve = new EnumMap<InitTypeEnum, Class<?>>(InitTypeEnum.class);
		for (Class<?> clazz : resolveList) {
			Init init = clazz.getAnnotation(Init.class);
			if(init == null) {
				throw new StartupErrorException("容器启动时,类:["+clazz.getName()+"]不存在Init注解，解析出错");
			}
			Class<?> clazzTemp = mapResolve.get(init.value());
			if(clazzTemp != null) {
				throw new StartupErrorException("容器启动时，有重复的bean解析操作:["+clazzTemp.getName()+"和"+clazz.getName()+"]冲突");
			}
			mapResolve.put(init.value(), clazz);
		}
		mapResolve = CollectionUtil.sortMapByKey(mapResolve, true);
		return mapResolve;
	}
	
	
	
	/**
	 * 方法用途: 容器的单个bean实例的解析处理入口<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	private static StartupTypeEnum startCallback(Class<?> beanClass,AnnoCallback call) {
        //安全退出机制:平滑停止
        Runtime.getRuntime().addShutdownHook(new Thread() {//TODO 有bug，这个时候在tomcat停止的时候，类被销毁了，这时候才调用stop方法，stop执行过程会报错
            public void run() {  
               List<Class<?>> classList = ClassUtil.findClassesByInterfaces(IStopRelease.class, BeanAnnotationResolver.getClasspaths());
               Startup.stop(classList);  
            }  
        }); 
		Map<InitTypeEnum, Class<?>> mapResolve = getAnnotationResolverList();
		resolve(beanClass, call, mapResolve);
		return StartupTypeEnum.SUCCESS;
	}

	/**
	 * 
	 * 方法用途: 加载解析Bean<br>
	 * 操作步骤: TODO<br>
	 * @param beanClass
	 * @param call
	 * @param mapResolve
	 */
	public static void resolve(Class<?> beanClass, AnnoCallback call, Map<InitTypeEnum, Class<?>> mapResolve) {
		for (Class<?> clazz : mapResolve.values()) {
			LOGGER.info("resolver invoke:{}",clazz.getName());
			try {
				Object obj = BeanFactory.getBean(clazz.getName());//启动阶段可提供容器托管
				if(obj == null) {
					obj = clazz.newInstance();
				}
				@SuppressWarnings("unchecked")
				IAnnotationResolver<Class<?>> ir = (IAnnotationResolver<Class<?>>)obj;
				call.invoke(ir,beanClass);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			} catch(StartupException ex) {
				Throwable target = ex.getTargetException();
				if(target.getClass() == StartupErrorException.class) {
					System.err.println("系统终止服务，有致命异常 ==>>\r\n"+target.getMessage());
					System.exit(-1);
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
				
		}
	}
	
	public interface AnnoCallback  {
		void invoke(IAnnotationResolver<Class<?>> ianno,Class<?> beanClass);
	}

	/**
	 * 方法用途: 应用程序退出的触发条件<br>
	 * 操作步骤: 1.自动结束：应用没有存活线程或只有后台线程时
		2.System.exit(0) 或 System.exit(-1)
		3.kill 或 ctrl+C
		4.kill -9 强制退出<br>
	 */
	public static void stop(List<Class<?>> classList) {
		for (Class<?> clazz : classList) {
			IStopRelease isr = BeanFactory.getBean(clazz.getName());
			isr.release();
		}
		LOGGER.info("系统已经停止运行，资源已释放");
	}
}
