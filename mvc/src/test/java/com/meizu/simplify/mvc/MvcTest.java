package com.meizu.simplify.mvc;

import org.junit.Test;

import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.Startup;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc:-verbose:class -javaagent:E:/workspace-new/simplify-framework/weaving/target/weaving-{version}.jar -Daop.properties=E:/workspace-new/simplify-framework/weaving/src/main/resources/aop.properties
 *   通过verbose:class 可以打印详细的classload所load的class有哪些，和整个loaded的过程
 * </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月20日 下午5:39:11</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月20日 下午5:39:11</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class MvcTest {
	@Test
	public void testMvc() {
		Startup.start();
		long start = System.currentTimeMillis();
		User bb = new User();
		bb.setName("yyyyy");
		BeanFactory.getBean(TestService.class).doSomeThing(bb);
		System.out.println((System.currentTimeMillis() - start) + "ms");
	}
	@Test
	public  void testCache() {
    	long start = System.currentTimeMillis();
        TestService h = new TestService();
        User bb = new User();
        bb.setName("yyyyy");
        h.doSomeThing(bb);
        System.out.println(System.currentTimeMillis()-start);
    }
}
