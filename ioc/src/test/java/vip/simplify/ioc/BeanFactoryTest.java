package vip.simplify.ioc;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.Constants;
import vip.simplify.ioc.dao.Dao;
import vip.simplify.ioc.service.ITestService;
import vip.simplify.ioc.service.TestService;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月8日 下午4:00:33</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月8日 下午4:00:33</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public  class BeanFactoryTest {
	private Logger LOGGER = LoggerFactory.getLogger(BeanFactoryTest.class);
	@BeforeClass
	public static void before() {
		Startup.start();
	}
	@Test
	public void getBean() {
		Object obj = BeanFactory.getBean(Constants.packagePrefix+".simplify.ioc.service.TestService");
		LOGGER.debug(obj+"");
	}
	@Test
	public void getBeanByClass() {
		TestService obj = BeanFactory.getBean(TestService.class);
		LOGGER.debug(obj.getDemoService().getName()+"");
	}
	
	@Test
	public void getBeanByInterface() {
		ITestService obj = BeanFactory.getBean(TestService.class);
		LOGGER.debug(obj.getDemoService().getName()+"");
	}
	
	/**
	 * 
	 * 方法用途: 多实例bean获取<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void getBeanByPrototype() {
		//1.多例bean，无法通过这种方式获取到bean，应该有多个Dao实例，无法确定返回哪个bean
        //2.可以通过getBeanByPrototypeForText的方式获取多例bean的具体实例，如果是Dao的话，可以通过二次封装的BaseDao，直接自动获取bean并操作
		//3.可以通过注解标注的方式，来选择具体要返回的bean实例
		Dao obj = BeanFactory.getBean(Dao.class);
		Assert.assertNull(obj);
	}
	@Test
	public void getBeanByPrototypeForText() {
		Dao obj = BeanFactory.getBean("test1BaseDao");
		LOGGER.debug(obj.getSql()+"|"+obj.toString());
		Dao obj2 = BeanFactory.getBean("test2BaseDao");
		LOGGER.debug(obj2.getSql()+"|w"+obj2.toString());
	}
}
