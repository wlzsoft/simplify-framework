package com.meizu.simplify.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.meizu.simplify.dao.orm.BaseDao;
import com.meizu.simplify.ioc.Startup;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月28日 下午6:04:26</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月28日 下午6:04:26</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class DaoTest {

	@Before
	public void before() {
		Startup.start();
	}
	
	@Test
	public void insertTest() {
		com.meizu.simplify.dao.entity.Test t = new com.meizu.simplify.dao.entity.Test();
		System.out.println("save============================="+BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).save(t));
	}
	
	@Test
	public void baseDaoTest() {
		Assert.assertEquals("hahah1", BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).findById(1).getName());
	}
}
