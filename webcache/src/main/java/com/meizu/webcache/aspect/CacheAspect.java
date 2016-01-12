package com.meizu.webcache.aspect;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.ClearComment;
import com.meizu.simplify.utils.Md5Util;
import com.meizu.simplify.utils.StringUtils;
import com.meizu.webcache.annotation.Aspect;
import com.meizu.webcache.annotation.CacheSet;
import com.meizu.webcache.web.Cache;
import com.meizu.webcache.web.CacheBase;


/**
 * 
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月4日 下午6:26:54</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月4日 下午6:26:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
@Aspect
public class CacheAspect {
	@Resource
	private HttpServletRequest request;
	private boolean isError; // 拦截时是否抛出异常
//	@Pointcut("execution(* com.meizu*entity*.*(..))")
//	public void pointCut(){
//	}
	@Around("@annotation(com.meizu.cache.annotation.CacheSet)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		return joinPoint.proceed();
    }
	/**
	 * 方法用途: 方法开始时触发<br>
	 * 操作步骤: 拦截方法前先将错误标识置为false<br>
	 * @param join
	 * @throws Throwable
	 */
	@Before("@annotation(com.meizu.cache.annotation.CacheSet)")
	public void onBefore(JoinPoint join) throws Throwable {
		this.isError = false;
		
		//需要废弃 TODO
		MethodSignature signature = (MethodSignature) join.getSignature();
		Method doMethod = signature.getMethod();
		// 检查静态规则配置
		if (doMethod.isAnnotationPresent(CacheSet.class)) {
			CacheSet cacheSet = (CacheSet) doMethod.getAnnotation(CacheSet.class);
			Cache cache = CacheBase.getCache(cacheSet);
			if(cache != null){
//				String cacheContent = cache.readCache(cacheSet, staticName);
//				if(cacheContent != null){
//					response.setCharacterEncoding(MvcInit.charSet);
//					response.setContentType("text/html; charset=" + MvcInit.charSet);
//					response.getWriter().print(cacheContent);
					System.out.println("debug:UrlCache -> read Cache.");
//				}
			}
		}
		
	}
	/**
	 * 方法用途: 切面处理异常时触发<br>
	 * 操作步骤: 当拦截方法抛出异常时，将在此拦截，拦截方式同onReturn()，记录操作执行错误日志。 正常放回后，该方法将不会执行。<br>
	 * @param ex
	 * @throws Throwable
	 */
	@AfterThrowing(pointcut = "@annotation(com.meizu.cache.annotation.CacheSet)", throwing = "ex")
	public void onThrowing(Throwable ex) throws Throwable {
		this.isError = true;
		throw ex;
	}
	
	/**
	 * 方法用途: 方法返回时触发<br>
	 * 操作步骤: 当拦截方法正常返回后，对调用LogByMethod标记方法执行日志记录。<br>
	 * @param join 业务拦截切点
	 * @throws BusinessException
	 */
	@AfterReturning(pointcut="@annotation(com.meizu.cache.annotation.CacheSet)",returning="value")
	public void onReturn(JoinPoint join,Object value) throws UncheckedException {
		//需要废弃
		if (this.isError) {
			return; // 业务类若抛异常就不记录数据库日志
		}
		MethodSignature signature = (MethodSignature) join.getSignature();
		Method doMethod = signature.getMethod();
		// 检查静态规则配置
		if (doMethod.isAnnotationPresent(CacheSet.class)) {
			CacheSet cacheSet = (CacheSet) doMethod.getAnnotation(CacheSet.class);
			// 跳转前检查静态规则
			if (cacheSet != null && cacheSet.mode() != CacheSet.CacheMode.nil) {
				//页面级别内容
				String content = "test"; // TODO 
				// 是否去除空格
				if(cacheSet.removeSpace()) {
					content = ClearComment.clear(content);
					content = StringUtils.removeSpace(content);
				}
				Cache cache = CacheBase.getCache(cacheSet);
				String url = request.getServerName() + request.getRequestURI() + StringUtils.isNotNull(request.getQueryString());
				String staticName = Md5Util.md5(url) + ".lv";
				if(cache != null && cache.doCache(cacheSet, staticName, content,null)){
					// 缓存成功.
				}
			}
		}
	}

}
