package com.meizu.simplify.aop.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.aop.Context;
import com.meizu.simplify.aop.Handler;
import com.meizu.simplify.aop.IInterceptor;
import com.meizu.simplify.aop.enums.ContextTypeEnum;

/**
 * <p><b>Title:</b><i>日志拦截器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月19日 上午10:13:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月19日 上午10:13:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class LogInterceptor extends Handler implements  IInterceptor{

	private static final LogInterceptor LOG_INTERCEPTOR = new LogInterceptor(); 
	private LogInterceptor() {
		
	}
	public static LogInterceptor getInstance() {
		return LOG_INTERCEPTOR;
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LogInterceptor.class);
	
	
	@Override
	public boolean before(Context context,Object... args) {
		String methodFullName = context.getMethodFullName();
		Object o = context.getThiz();
		LOGGER.info("日志切面切入：["+methodFullName+"]方法之前 切入");
		return true;
	}
	
	@Override
	public boolean after(Context context,Object... args) {
		String methodFullName = context.getMethodFullName();
		Object o = context.getThiz();
		LOGGER.info("日志切面切入：["+methodFullName+"]方法之后切入");
		return true;
	}

	@Override
	public boolean handle(Context context,Object... obj) {
		if(context.getType().equals(ContextTypeEnum.BEFORE)) {
			before(context,obj);
		} else {
			after(context,obj);
		}
		return true;
	}

	
	
}
