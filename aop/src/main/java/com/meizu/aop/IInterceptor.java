package com.meizu.aop;

import com.meizu.aop.cache.CacheInterceptor;
import com.meizu.aop.enums.ContextTypeEnum;
import com.meizu.aop.log.LogInterceptor;

/**
 * <p><b>Title:</b><i>拦截器接口</i></p>
 * <p>Desc: 责任链方式处理，串联所有的拦截器实现</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月19日 上午10:14:23</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月19日 上午10:14:23</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IInterceptor {
	public static Object initBefore(String methodFullName,Object o,Object... args ) {
		Handler handle = CacheInterceptor.getInstance();
//		handle.setNextHandler(new LogInterceptor())//业务处理成功后才需要记录日志，这里无需设置日志过滤器
//		.setNextHandler(handle);//环状，这里暂时不需要环状责任链,可设置，也可以不设置，默认设置上，形成闭环
		Context context = new Context();
		context.setMethodFullName(methodFullName);
		context.setThis(o);
		context.setType(ContextTypeEnum.BEFORE);
		handle.invoke(context,args);//无需指定参数，暂无传递参数，后续有需要再添加
		return -1;
	}
	
	public static Object initAfter(String methodFullName,Object o,Object... args ) {
		Handler handle = CacheInterceptor.getInstance();
		handle.setNextHandler(LogInterceptor.getInstance())
		.setNextHandler(handle);//环状，这里暂时不需要环状责任链,可设置，也可以不设置，默认设置上，形成闭环
		Context context = new Context();
		context.setMethodFullName(methodFullName);
		context.setThis(o);
		context.setType(ContextTypeEnum.AFTER);
		handle.invoke(context,args);//无需指定参数，暂无传递参数，后续有需要再添加
		return -1;
	}
	
	void before(String methodFullName,Object o,Object... args);
	void after(String methodFullName,Object o,Object... args);
}
