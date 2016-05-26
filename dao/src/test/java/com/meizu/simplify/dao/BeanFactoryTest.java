package com.meizu.simplify.dao;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.dao.orm.Dao;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.Startup;
import com.meizu.simplify.ioc.service.ITestService;
import com.meizu.simplify.ioc.service.TestService;

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
	public void getBeanByClass() {
		TestService obj = BeanFactory.getBean(TestService.class);
		LOGGER.debug(obj.getDemoService().getName()+"");
	}
	
	@Test
	public void getBeanByInterface() {
		ITestService obj = BeanFactory.getBean(TestService.class);
		LOGGER.debug(obj.getDemoService().getName()+"");
	}
	
	@Test
	public void getBeanByPrototypeForText() {
		Dao<com.meizu.simplify.dao.entity.Test,Integer> obj = BeanFactory.getBean("testBaseDao");
		LOGGER.debug(obj.findById(1).getName()+"|"+obj.toString());
	}
}
