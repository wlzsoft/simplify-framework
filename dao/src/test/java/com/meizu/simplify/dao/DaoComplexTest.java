package com.meizu.simplify.dao;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.meizu.simplify.dao.orm.BaseDao;
import com.meizu.simplify.ioc.Startup;

/**
  * <p><b>Title:</b><i>针对entity实体对应的dao的复杂查询的测试</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月28日 下午6:04:26</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月28日 下午6:04:26</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DaoComplexTest {

	private static Integer key;
	@BeforeClass
	public static void before() {
		Startup.start();
	}
	
	@Before
	public void s1_insertTest() {
		com.meizu.simplify.dao.entity.Test t = new com.meizu.simplify.dao.entity.Test();
		t.setName("卢创业");
		t.setCreateId(1);
		t.setUpdateId(1);
		t.setCreateTime(new Date());
		t.setUpdateTime(new Date());
		System.out.println("saveDaoComplexTest========================"+BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).save(t));
		System.out.println("saveGenIdDaoComplexTest:"+t.getFid());
		key = t.getFid();
	}
	@Test
	public void s2_findByWhereTest() {
//		List<com.meizu.simplify.dao.entity.Test> testlist = BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).where("name","lcy").findAll();
//		System.out.println(testlist);
	}
	@Test
	public void s2_findByIdTest() {
		Assert.assertNotNull(BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).findById(key).getName());
	}
	
	@Test
	public void s3_deleteTest() {
		System.out.println("deleteDaoComplexTest============================="+BaseDao.getIns(com.meizu.simplify.dao.entity.Test.class).remove(key));
	}
	
	
}
