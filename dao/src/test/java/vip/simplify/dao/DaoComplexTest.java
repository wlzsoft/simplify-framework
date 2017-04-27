package vip.simplify.dao;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import vip.simplify.dao.orm.BaseDao;
import vip.simplify.ioc.Startup;

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
		vip.simplify.dao.entity.Test t = new vip.simplify.dao.entity.Test();
		t.setName("卢创业");
		t.setCreateId(1);
		t.setUpdateId(1);
		t.setCreateTime(new Date());
		t.setUpdateTime(new Date());
		System.out.println("saveDaoComplexTest========================"+ BaseDao.getIns(vip.simplify.dao.entity.Test.class).save(t));
		System.out.println("saveGenIdDaoComplexTest:"+t.getFid());
		key = t.getFid();
	}
	@Test
	public void s2_findByWhereTest() {
//		List<Test> testlist = BaseDao.getIns(Test.class).where("name","lcy").findAll();
//		System.out.println(testlist);
	}
	@Test
	public void s2_findByIdTest() {
		Assert.assertNotNull(BaseDao.getIns(vip.simplify.dao.entity.Test.class).findById(key).getName());
	}
	
	@Test
	public void s3_deleteTest() {
		System.out.println("deleteDaoComplexTest============================="+BaseDao.getIns(vip.simplify.dao.entity.Test.class).remove(key));
	}
	
	
}
