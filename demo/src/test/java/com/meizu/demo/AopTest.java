package com.meizu.demo;


import org.junit.Before;
import org.junit.Test;

import com.meizu.simplify.ioc.Startup;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月22日 下午1:14:20</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月22日 下午1:14:20</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class AopTest {
	
	@Before
	public void init() {
		Startup.start();
	}
	
	@Test
	public void test() {
		String className = "com.meizu.demo.mvc.service.TestService";
		String methodName = "doSomeThing";
		String methodFullName = className+":"+methodName;
    	System.out.println("AOP：对方法["+methodFullName+"]进行逻辑切入");
        	com.meizu.simplify.aop.IInterceptor.initBefore(methodFullName,this);
        	long startTime = System.currentTimeMillis();
        	com.meizu.simplify.aop.IInterceptor.initAfter(methodFullName,this);
        	long endTime = System.currentTimeMillis();
        	System.out.println("方法 ["+methodFullName+"] 调用花费的时间:" +(endTime - startTime) +"ms.");
	}
}
