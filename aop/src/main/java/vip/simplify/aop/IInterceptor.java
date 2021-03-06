package vip.simplify.aop;

import vip.simplify.Constants;
import vip.simplify.aop.enums.ContextTypeEnum;
import vip.simplify.ioc.BeanFactory;

/**
 * <p><b>Title:</b><i>拦截器接口</i></p>
 * <p>Desc: 责任链方式处理，串联所有的拦截器实现
 *  缓存类似slf4j的实现机制，抽取出aop模块，独立出来，从缓存剥离开始</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月19日 上午10:14:23</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月19日 上午10:14:23</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IInterceptor {
	public  static <T extends Object> T initBefore(String methodFullName,InterceptResult ir,Object o,Object... args ) {
		Handler handle = BeanFactory.getBean("beforeCacheInterceptor");//CacheInterceptor.getBeforeInstance();
//		handle.setNextHandler(BeanFactory.getBean(Constants.packagePrefix+".aop.log.LogInterceptor"))//业务处理成功后才需要记录日志，这里无需设置日志过滤器
		handle.setNextHandler(BeanFactory.getBean(Constants.packagePrefix+".dao.TransationInterceptor"));
//		.setNextHandler(handle);//环状，这里暂时不需要环状责任链,可设置，也可以不设置，默认设置上，形成闭环
		Context context = new Context(ir);
		context.setMethodFullName(methodFullName);
		context.setThis(o);
		context.setType(ContextTypeEnum.BEFORE);
		handle.invoke(context,args);//无需指定参数，暂无传递参数，后续有需要再添加
		return context.getCallback().getResult();
	}
	
	public static Object initAfter(String methodFullName,InterceptResult ir,Object o,Object... args ) {
		Handler handle = BeanFactory.getBean("afterCacheInterceptor");//CacheInterceptor.getAfterInstance();
		handle.setNextHandler(BeanFactory.getBean(Constants.packagePrefix+".aop.log.LogInterceptor"));
		handle.setNextHandler(BeanFactory.getBean(Constants.packagePrefix+".dao.TransationInterceptor"));
//		.setNextHandler(handle);//环状，这里暂时不需要环状责任链,可设置，也可以不设置，默认设置上，形成闭环
		Context context = new Context(ir);
		context.setMethodFullName(methodFullName);
		context.setThis(o);
		context.setType(ContextTypeEnum.AFTER);
		handle.invoke(context,args);//无需指定参数，暂无传递参数，后续有需要再添加
		return -1;
	}
	
	boolean before(Context context,Object... args);
	boolean after(Context context,Object... args);
	
	public static Object initException(String methodFullName,InterceptResult ir,Object o,Object... args ) {
		/*for(StackTraceElement ste:throwable.getStackTrace()){
			System.out.println("异常堆栈异常方法:"+ste.getMethodName());
		}*/
		Handler handle = BeanFactory.getBean(Constants.packagePrefix+".dao.TransationInterceptor");
		Context context = new Context(ir);
		context.setMethodFullName(methodFullName);
		context.setThis(o);
		context.setType(ContextTypeEnum.EXCEPTION);
		handle.invoke(context,args);
		return -1;
	}
	
	public static Object initFinally(String methodFullName,InterceptResult ir,Object o,Object... args ) {
		Handler handle = BeanFactory.getBean(Constants.packagePrefix+".dao.TransationInterceptor");
		Context context = new Context(ir);
		context.setMethodFullName(methodFullName);
		context.setThis(o);
		context.setType(ContextTypeEnum.FINALLY);
		handle.invoke(context,args);
		return -1;
	}
	
	default boolean exception(Context context,Object... args) {
		return false;
	}
	default boolean finallyer(Context context,Object... args) {
		return false;
	}
	
}
