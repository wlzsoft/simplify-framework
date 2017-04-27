package vip.simplify.aop.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.aop.Context;
import vip.simplify.aop.Handler;
import vip.simplify.aop.IInterceptor;
import vip.simplify.aop.enums.ContextTypeEnum;
import vip.simplify.ioc.annotation.Bean;

/**
 * <p><b>Title:</b><i>日志拦截器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月19日 上午10:13:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月19日 上午10:13:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class LogInterceptor extends Handler implements  IInterceptor{

	private static final Logger LOGGER = LoggerFactory.getLogger(LogInterceptor.class);
	
	
	@Override
	public boolean before(Context context,Object... args) {
		String methodFullName = context.getMethodFullName();
		Object o = context.getThiz();
		LOGGER.info("日志切面切入：["+methodFullName+"]方法之前 切入"+o);
		return true;
	}
	
	@Override
	public boolean after(Context context,Object... args) {
		String methodFullName = context.getMethodFullName();
		Object o = context.getThiz();
		LOGGER.info("日志切面切入：["+methodFullName+"]方法之后切入"+o);
		return true;
	}

	@Override
	public boolean handle(Context context,Object... obj) {
		if(context.getType().equals(ContextTypeEnum.BEFORE)) {
			before(context,obj);
		} else if(context.getType().equals(ContextTypeEnum.AFTER)) {
			after(context,obj);
		} else if(context.getType().equals(ContextTypeEnum.EXCEPTION)) {
			exception(context, obj);
		} else {
			finallyer(context, obj);
		}
		return true;
	}

	
	
}
