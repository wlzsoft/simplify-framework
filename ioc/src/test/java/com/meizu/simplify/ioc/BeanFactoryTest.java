package com.meizu.simplify.ioc;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.ioc.dao.Dao;
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
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public  class BeanFactoryTest {
	private Logger LOGGER = LoggerFactory.getLogger(BeanFactoryTest.class);
	@Before
	public void before() {
		Startup.start();
	}
	@Test
	public void getBean() {
		Object obj = BeanFactory.getBean("com.meizu.simplify.ioc.service.TestService");
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
		Dao obj = BeanFactory.getBean(Dao.class);
		LOGGER.debug(obj.getSql()+"|"+obj.toString());
	}
}
