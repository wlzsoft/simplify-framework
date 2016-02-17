package com.meizu.simplify.dao;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DaoTest {

	private static Integer key;
	@Before
	public void before() {
		Startup.start();
	}
	
	@Test
	public void s1_insertTest() {
		com.meizu.simplify.dao.entity.Test t = new com.meizu.simplify.dao.entity.Test();
		t.setName("lcy");
		t.setCreateId(1);
		t.setUpdateId(1);
		t.setCreateTime(new Date());
		t.setUpdateTime(new Date());
		System.out.println("save============================="+BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).save(t));
		key = t.getId();
	}
	
	@Test
	public void s2_findByIdTest() {
		Assert.assertEquals("lcy", BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).findById(key).getName());
	}
}
