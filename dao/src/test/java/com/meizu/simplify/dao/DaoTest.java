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
	public void getIdNameTest() {
		Assert.assertEquals("id", BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).getIdName());
	}
	
	@Test
	public void getIdValTest() {
		com.meizu.simplify.dao.entity.Test t = new com.meizu.simplify.dao.entity.Test();
		t.setId(1);//必须设置id的值
		t.setName("lcy");
		t.setCreateId(1);
		t.setUpdateId(1);
		t.setCreateTime(new Date());
		t.setUpdateTime(new Date());
		Integer key = (Integer) BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).getId(t);
		System.out.println(key);
		Assert.assertTrue(key>0);
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
	
	@Test
	public void s3_deleteTest() {
		System.out.println("delete============================="+BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).remove(key));
	}
	
	@Test
	public void s4_deleteTest() {
		com.meizu.simplify.dao.entity.Test t = new com.meizu.simplify.dao.entity.Test();
		t.setId(918);
		System.out.println("delete============================="+BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).remove(t));
	}
	
	@Test
	public void s5_deleteTest() {
		//中文编码问题，无法正常删除
		System.out.println("delete============================="+BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).remove("url", "geny测试"));
	}
	
	@Test
	public void s6_deleteTest() {
		Integer[] ids = new Integer[] {901,902,903,904,905};
		System.out.println("delete============================="+BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).remove(ids));
	}
}
