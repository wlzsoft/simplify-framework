package com.meizu.simplify.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	public void s2_findUniqueTest() {
		Assert.assertEquals("lcy", BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).findUnique("name","lcy").getName());
	}
	
	@Test
	public void s2_findByIdsTest() {
		Integer[] ids = new Integer[]{1,2,3};
		List<com.meizu.simplify.dao.entity.Test> list = BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).findByIds(ids);
		for (com.meizu.simplify.dao.entity.Test test : list) {
			System.out.println(test.getId()+test.getName());
		}
		Assert.assertEquals(3, BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).findByIds(ids).size());
	}
	@Test
	public void s2_findByMutilTest() {
		List<com.meizu.simplify.dao.entity.Test> list = BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).findByMutil("id","1,2,3");
		for (com.meizu.simplify.dao.entity.Test test : list) {
			System.out.println(test.getId()+test.getName());
		}
		Assert.assertEquals(3, BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).findByMutil("id","1,2,3").size());
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
	public void s6_insertTest() {
		
		List<com.meizu.simplify.dao.entity.Test> testList = new ArrayList<>();
		for(int i=0; i<4; i++) {
			com.meizu.simplify.dao.entity.Test t = new com.meizu.simplify.dao.entity.Test();
			t.setName("lcy"+i);
			t.setCreateId(1);
			t.setUpdateId(1);
			t.setCreateTime(new Date());
			t.setUpdateTime(new Date());
			testList.add(t);
		}
		BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).save(testList);
		System.out.println("save==all=============================");
	}
	
	@Test
	public void s7_deleteTest() {
		Integer[] ids = new Integer[] {901,902,903,904,905};
		System.out.println("delete============================="+BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).remove(ids));
	}
	
	@Test
	public void s8_countTest() {
		System.out.println("count============================="+BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).count("select count(*) from test_web"));
	}
	
	@Test
	public void s9_countTest() {
		com.meizu.simplify.dao.entity.Test t = new com.meizu.simplify.dao.entity.Test();
		t.setName("lcy");
		System.out.println("count============================="+BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).count(t));
	}
	
}
