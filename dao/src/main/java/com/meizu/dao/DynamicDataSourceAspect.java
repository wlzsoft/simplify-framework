package com.meizu.dao;

import com.meizu.simplify.ioc.annotation.Bean;
/**
 * <p><b>Title:</b><i>动态数据源切面</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年4月28日 下午2:59:56</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年4月28日 下午2:59:56</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
//@Aspect  
@Bean
public class DynamicDataSourceAspect {
	

	/**
	 * 
	 * 方法用途:  在dao方法执行之前设置切换的数据源，并且在dao方法执行完成后，清除treadloal的数据，做收尾工作<br>
	 * 操作步骤: TODO<br>
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
		
		// 根据具体情况，自动解析，通过DynamicDataSourceHolder.setDataSourceName() 指定不同数据源   
		for (Object o : pjp.getArgs()) {
			//TODO
		}
		
		// 第三种方式：在dao方法上配置的注解
		DataSource dataSource = pjp.getTarget().getClass()
				.getAnnotation(DataSource.class);
		if (dataSource == null) {
			String methodName = pjp.getSignature().getName();
			dataSource = pjp.getTarget().getClass()
					.getMethod(methodName).getAnnotation(DataSource.class);
		}
		if (dataSource != null) {
			//设置数据源
			DynamicDataSourceHolder
					.setDataSourceName(dataSource.value());
		}
		Object object = pjp.proceed();
		if (dataSource != null) {
			// 设置成功后清除threadlocal的数据
			DynamicDataSourceHolder.setClear();
		}
		return object;
	}

}