package vip.simplify.dao;

import java.util.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import vip.simplify.dao.orm.BaseDao;
import vip.simplify.ioc.Startup;

/**
  * <p><b>Title:</b><i>针对无entity的dao的测试</i></p>
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
public class DaoSimpleTest {

	private static Integer key;
	@BeforeClass
	public static void before() {
		Startup.start();
	}
	
	@Before
	public void s1_insertTest() {
		Integer id = BaseDao.getTable().transiented("delFlag","deleteflag").save("test_web","name","lcy","createId",1,"updateId",1,"createTime",new Date(),"updateTime",new Date());
		System.out.println("saveGenId:"+id);
		key = id;
		System.out.println(key);
	}
	
	/*@Test
	public void s2_findUniqueTest() {
		Assert.assertEquals("lcy", "lcy");
		BaseDao.getTable().findUnique("name","lcy");//.getName();
	}*/
	
	/*
	@Test
	public void s2_findByIdsTest() {
		Integer[] ids = new Integer[]{1,2,3};
		List<Test> list = BaseDao.getIns(Test.class).findByIds(ids);
		for (Test test : list) {
			System.out.println(test.getFid()+test.getName());
		}
		Assert.assertEquals(3, BaseDao.getIns(Test.class).findByIds(ids).size());
	}
	@Test
	public void s2_findByMutilTest() {
		List<Test> list = BaseDao.getIns(Test.class).findByMutil("name","lcy");
		for (Test test : list) {
			System.out.println(test.getFid()+test.getName());
		}
		Assert.assertTrue(BaseDao.getIns(Test.class).findByMutil("name","lcy").size()>0);
	}
	
	@Test
	public void s2_findByIdTest() {
		System.out.println("aa:"+key);
		Assert.assertNotNull(BaseDao.getIns(Test.class).findById(key).getName());
	}
	@Test
	public void s2_findAllTest() {
		Assert.assertTrue(BaseDao.getIns(Test.class).findAll().size()>0);
	}
	@Test
	public void s2_findPageSqlTest() {
		@SuppressWarnings("deprecation")
		Page<Test> page = BaseDao.getIns(Test.class).findPage(1,10,"createTime",true,"select * from test_web where name=?","lcy");
		Assert.assertEquals(page.getTotalRecord(), page.getResults().size());
	}
	@Test
	public void s2_findPageMutilSqlTest() {
		@SuppressWarnings("deprecation")
		Page<Test> page = BaseDao.getIns(Test.class).findPage(1,10,"createTime",true,"select * from (select test_web.*,user.name as createName from test_web inner join user on test_web.createId=user.id where test_web.name=?) as temp","lcy");
		Assert.assertEquals(page.getTotalRecord(), page.getResults().size());
	}
	@Test
	public void s2_findPageMutilSql2Test() {
		Page<Test> page = BaseDao.getIns(Test.class).findPage(1,10,"select * from (select test_web.*,user.name as createName from test_web inner join user on test_web.createId=user.id where test_web.name=? order by createTime desc) as temp","lcy");
		Assert.assertEquals(page.getTotalRecord(), page.getResults().size());
	}
	@Test
	public void s2_findPageMutilSql3Test() {
		Page<Test> page = BaseDao.getIns(Test.class).findPage(1,10,"select test_web.*,user.name as createName from test_web inner join user on test_web.createId=user.id where test_web.name=? order by createTime asc","lcy");
		Assert.assertEquals(page.getTotalRecord(), page.getResults().size());
	}
	@Test
	public void s2_findPageTest() {
		Test t = new Test();
		t.setName("lcy");
		Page<Test> page = BaseDao.getIns(Test.class).findPage(1,10,t);
		Assert.assertEquals(page.getTotalRecord(), page.getResults().size());
	}
	@Test
	public void s2_findPageOderByTest() {
		Test t = new Test();
		t.setName("lcy");
		Page<Test> page = BaseDao.getIns(Test.class).findPage(1,10,"createTime",true,t);
		Assert.assertEquals(page.getTotalRecord(), page.getResults().size());
	}
	@Test
	public void s2_findByTest() {
		Assert.assertTrue(BaseDao.getIns(Test.class).findBy("name","lcy").size()>0);
	}
	*/
	@Test
	public void s3_deleteTest() {
		System.out.println("delete============================="+BaseDao.getTable().transiented("delFlag","deleteflag").remove("test_web","fid",key));
	}
	/*
	
	@Test
	public void s6_updateTest() {
		Test t = new Test();
		t.setName("lcycc1");
		t.setFid(1);
		System.out.println("update==============================="+BaseDao.getIns(Test.class).update(t));
	}
	@Test
	public void s6_update2Test() {
		Test t = new Test();
		t.setName("lcycc2");
		t.setFid(1);
		System.out.println("update2==============================="+BaseDao.getIns(Test.class).update(t,false));
	}*/
	@Test
	public void s7_deleteTest() {
		Object[] ids = new Object[] {901,902,903,904,905,1828,1827};
		System.out.println("delete============================="+BaseDao.getTable().transiented("delFlag","deleteflag").remove("test_web","fid",ids));
	}
	/*
	@Test
	public void s8_countTest2() {
		System.out.println("count============================="+BaseDao.getInsMap().count("select count(*) from test_web where name=?","awsesdf"));
	}
	
	@Test
	public void s8_countTest() {
		System.out.println("count============================="+BaseDao.getInsMap().count("select count(*) from test_web where name=?","lcy"));
	}
	
	@Test
	public void s9_countTest() {
		Test t = new Test();
		t.setName("lcy");
		System.out.println("count============================="+BaseDao.getIns(Test.class).count(t));
	}
	@Test
	public void s10_findByTest() {
		Test t = new Test();
		t.setName("lcy");
		List<Test> list = BaseDao.getIns(Test.class).findBy(t);
		for (Test test : list) {
			System.out.println(test.getFid()+test.getName());
		}
	}*/
	
}
