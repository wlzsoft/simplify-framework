package com.meizu.simplify.cache;

import org.junit.Test;

import com.meizu.simplify.cache.entity.User;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.Startup;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc:-verbose:class -javaagent:E:/workspace-new/simplify-framework/weaving/target/weaving-1.2.0-SNAPSHOT.jar -Daop.properties=E:/workspace-new/simplify-framework/weaving/src/main/resources/aop.properties
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
public class ServiceCacheTest {
	@Test
	public void testMvc() {
		Startup.start();
		long start = System.currentTimeMillis();
		User bb = new User();
		bb.setName("yyyyy");
		BeanFactory.getBean(UserService.class).doSomeThing(bb);
		System.out.println((System.currentTimeMillis() - start) + "ms");
	}
	@Test
	public  void testCache() {
    	long start = System.currentTimeMillis();
    	UserService h = new UserService();
        User bb = new User();
        bb.setName("yyyyy");
        h.doSomeThing(bb);
        System.out.println(System.currentTimeMillis()-start);
    }
}
