package vip.simplify.demo.unit;


import org.junit.BeforeClass;
import org.junit.Test;

import vip.simplify.aop.InterceptResult;
import vip.simplify.ioc.Startup;
import vip.simplify.aop.IInterceptor;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月22日 下午1:14:20</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月22日 下午1:14:20</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class AopTest {
	
	@BeforeClass
	public static void init() {
		Startup.start();
	}
	
	@Test
	public void test() {
		String className = "TestService";
		String methodName = "doSomeThing";
		String methodFullName = className+":"+methodName;
		InterceptResult ir = new InterceptResult();
    	System.out.println("AOP：对方法["+methodFullName+"]进行逻辑切入");
        	IInterceptor.initBefore(methodFullName,ir,this);
        	long startTime = System.nanoTime();
        	IInterceptor.initAfter(methodFullName,ir,this);
        	long endTime = System.nanoTime();
        	System.out.println("方法 ["+methodFullName+"] 调用花费的时间:" +(endTime - startTime) +"ns.");
	}
}
