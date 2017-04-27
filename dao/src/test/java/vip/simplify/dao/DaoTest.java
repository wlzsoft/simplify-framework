package vip.simplify.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import vip.simplify.dao.datasource.SingleDataSource;
import vip.simplify.dao.entity.TestUser;
import vip.simplify.dao.orm.BaseDao;
import vip.simplify.entity.page.Page;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.Startup;

/**
  * <p><b>Title:</b><i>针对entity实体对应的dao的测试</i></p>
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
public class DaoTest {

	private static Integer key;
	@BeforeClass
	public static void before() {
		Startup.start();
	}
	
	@Before
	public void s1_insertTest() {
		vip.simplify.dao.entity.Test t = new vip.simplify.dao.entity.Test();
		t.setName("卢创业");
		t.setUrl("卢创业test");
		t.setCreateId(1);
		t.setUpdateId(1);
		t.setCreateTime(new Date());
		t.setUpdateTime(new Date());
		System.out.println("save============================="+ BaseDao.getIns(vip.simplify.dao.entity.Test.class).save(t));
		System.out.println("saveGenId:"+t.getFid());
		key = t.getFid();
	}
	
	@Test
	public void getIdNameTest() {
		Assert.assertEquals("fid", BaseDao.getIns(vip.simplify.dao.entity.Test.class).getIdName());
	}
	
	@Test
	public void getIdValTest() {
		vip.simplify.dao.entity.Test t = new vip.simplify.dao.entity.Test();
		t.setFid(1);//必须设置id的值
		t.setName("lcy");
		t.setCreateId(1);
		t.setUpdateId(1);
		t.setCreateTime(new Date());
		t.setUpdateTime(new Date());
		@SuppressWarnings("deprecation")
		Integer key = (Integer) BaseDao.getIns(vip.simplify.dao.entity.Test.class).getId(t);
		System.out.println(key);
		Assert.assertTrue(key>0);
	}
	
	@Test
	public void s2_findUniqueTest() {
		Assert.assertEquals("lcy", BaseDao.getIns(vip.simplify.dao.entity.Test.class).findUnique("name","lcy").getName());
	}
	
	@Test
	public void s2_findByIdsTest() {
		Integer[] ids = new Integer[]{1,2,3};
		List<vip.simplify.dao.entity.Test> list = BaseDao.getIns(vip.simplify.dao.entity.Test.class).findByIds(ids);
		for (vip.simplify.dao.entity.Test test : list) {
			System.out.println(test.getFid()+test.getName());
		}
		Assert.assertEquals(3, BaseDao.getIns(vip.simplify.dao.entity.Test.class).findByIds(ids).size());
	}
	@Test
	public void s2_findByMutilTest() {
		List<vip.simplify.dao.entity.Test> list = BaseDao.getIns(vip.simplify.dao.entity.Test.class).findByMutil("name","lcy");
		for (vip.simplify.dao.entity.Test test : list) {
			System.out.println(test.getFid()+test.getName());
		}
		Assert.assertTrue(BaseDao.getIns(vip.simplify.dao.entity.Test.class).findByMutil("name","lcy").size()>0);
	}
	
	@Test
	public void s2_findByIdTest() {
		System.out.println("aa:"+key);
		Assert.assertNotNull(BaseDao.getIns(vip.simplify.dao.entity.Test.class).findById(key).getName());
	}
	@Test
	public void s2_findAllTest() {
		Assert.assertTrue(BaseDao.getIns(vip.simplify.dao.entity.Test.class).findAll().size()>0);
	}
	/*@Test
	public void s2_findPageSqlTest() {
		@SuppressWarnings("deprecation")
		Page<Test> page = BaseDao.getIns(Test.class).findPage(1,10,"createTime",true,"select * from test_web where name=?",true,"lcy");
		Assert.assertEquals(page.getTotalRecord(), page.getResults().size());
	}
	@Test
	public void s2_findPageMutilSqlTest() {
		@SuppressWarnings("deprecation")
		Page<Test> page = BaseDao.getIns(Test.class).findPage(1,10,"createTime",true,"select * from (select test_web.*,test_user.name as createName from test_web inner join test_user on test_web.createId=test_user.fid where test_web.name=?) as temp","lcy");
		Assert.assertEquals(page.getTotalRecord(), page.getResults().size());
	}*/
	@Test
	public void s2_findPageMutilSql5Test() {
		//针对包含distinct关键字的测试
		Page<vip.simplify.dao.entity.Test> page = BaseDao.getIns(vip.simplify.dao.entity.Test.class).findPage(1,10,"select * from (select distinct test_user.name as createName from test_web inner join test_user on test_web.createId=test_user.fid where test_web.name=? order by createName desc) as temp","lcy");
		Assert.assertEquals(page.getTotalRecord(), page.getResults().size());
	}
	@Test
	public void s2_findPageMutilSql2Test() {
		//针对不包含distinct关键字的测试
		Page<vip.simplify.dao.entity.Test> page = BaseDao.getIns(vip.simplify.dao.entity.Test.class).findPage(1,10,"select * from (select test_web.*,test_user.name as createName from test_web inner join test_user on test_web.createId=test_user.fid where test_web.name=? order by createTime desc) as temp","lcy");
		Assert.assertTrue(page.getTotalRecord()>= page.getResults().size());
	}
	@Test
	public void s2_findPageMutilSql3Test() {
		Page<vip.simplify.dao.entity.Test> page = BaseDao.getIns(vip.simplify.dao.entity.Test.class).findPage(1,10,"select test_web.*,test_user.name as createName from test_web inner join test_user on test_web.createId=test_user.fid where test_web.name=? order by createTime asc","lcy");
		Assert.assertTrue(page.getTotalRecord()>= page.getResults().size());
	}
	@Test
	public void s2_findPageMutilSql4Test() {
		Page<Map<String,Object>> page = BaseDao.getInsMap().findPage(1,10,"select test_web.*,test_user.name as createName from test_web inner join test_user on test_web.createId=test_user.fid where test_web.name=? order by createTime asc","lcy");
		System.out.println(page);
//		Assert.assertEquals(page.getTotalRecord(), page.getResults().size());
	}
	@Test
	public void s2_findPageMutilSqlSubStringTest() {
 String sql = "select * from test_web where name=?";
		sql = sql.substring(sql.indexOf("from"));
		System.out.println("select count(1) "+sql);
		sql = "select * from (select test_web.*,test_user.name as createName from test_web inner join test_user on test_web.createId=test_user.fid where test_web.name=?) as temp";
		sql = sql.substring(sql.indexOf("from"));
		System.out.println("select count(1) "+sql);
		sql = "select * from (select test_web.*,test_user.name as createName from test_web inner join test_user on test_web.createId=test_user.fid where test_web.name=? order by createTime asc) as temp";
		sql = sql.substring(sql.indexOf("from"));
		sql = sql.replaceAll("order\\s*by.*(desc|asc)", "");
		System.out.println("select count(1) "+sql);
		sql = "select test_web.*,test_user.name as createName from test_web inner join test_user on test_web.createId=test_user.fid where test_web.name=? order by createTime desc";
		sql = sql.substring(sql.indexOf("from"));
		sql = sql.replaceAll("order\\s*by.*(desc|asc)", "");
		System.out.println("select count(1) "+sql);
	}
	@Test
	public void s2_findPageTest() {
		vip.simplify.dao.entity.Test t = new vip.simplify.dao.entity.Test();
		t.setName("lcy");
		Page<vip.simplify.dao.entity.Test> page = BaseDao.getIns(vip.simplify.dao.entity.Test.class).findPage(1,10,t);
		Assert.assertTrue(page.getTotalRecord()>= page.getResults().size());
	}
	@Test
	public void s2_findPageOderByTest() {
		vip.simplify.dao.entity.Test t = new vip.simplify.dao.entity.Test();
		t.setName("lcy");
		Page<vip.simplify.dao.entity.Test> page = BaseDao.getIns(vip.simplify.dao.entity.Test.class).findPage(1,10,"createTime",true,t);
		Assert.assertTrue(page.getTotalRecord()>= page.getResults().size());
	}
	@Test
	public void s2_findByTest() {
		Assert.assertTrue(BaseDao.getIns(vip.simplify.dao.entity.Test.class).findBy("name","lcy").size()>0);
	}
	
	@Test
	public void s3_deleteTest() {
		System.out.println("delete============================="+BaseDao.getIns(vip.simplify.dao.entity.Test.class).remove(key));
	}
	
	@Test
	public void s4_deleteTest() {
		vip.simplify.dao.entity.Test t = new vip.simplify.dao.entity.Test();
		t.setFid(918);
		System.out.println("delete============================="+BaseDao.getIns(vip.simplify.dao.entity.Test.class).remove(t));
	}
	
	@Test
	public void s5_deleteTest() {
		//中文编码问题，无法正常删除-->已经解决：由于jdbc的连接的编码属性设置有问题，修改jdbc驱动连接的配置信息就可以了。
		System.out.println("delete============================="+BaseDao.getIns(vip.simplify.dao.entity.Test.class).remove("url", "卢创业test"));
	}
	
	@Test
	public void s6_insertTest() {
		
		List<vip.simplify.dao.entity.Test> testList = new ArrayList<>();
		for(int i=0; i<4; i++) {
			vip.simplify.dao.entity.Test t = new vip.simplify.dao.entity.Test();
			t.setName("lcy"+i);
			t.setCreateId(1);
			t.setUpdateId(1);
			t.setCreateTime(new Date());
			t.setUpdateTime(new Date());
			testList.add(t);
		}
		BaseDao.getIns(vip.simplify.dao.entity.Test.class).save(testList);
		System.out.println("save==all=============================");
	}
	@Test
	public void s6_updateTest() {
		vip.simplify.dao.entity.Test t = new vip.simplify.dao.entity.Test();
		t.setName("lcycc1");
		t.setFid(1);
		System.out.println("update==============================="+BaseDao.getIns(vip.simplify.dao.entity.Test.class).update(t));
	}
	@Test
	public void s6_update2Test() {
		vip.simplify.dao.entity.Test t = new vip.simplify.dao.entity.Test();
		t.setName("lcycc2");
		t.setFid(1);
		System.out.println("update2==============================="+BaseDao.getIns(vip.simplify.dao.entity.Test.class).update(t,false));
	}
	@Test
	public void s7_deleteTest() {
		Integer[] ids = new Integer[] {901,902,903,904,905};
		System.out.println("delete============================="+BaseDao.getIns(vip.simplify.dao.entity.Test.class).remove(ids));
	}
	
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
		
		SingleDataSource dataSource = BeanFactory.getBean(SingleDataSource.class);
		dataSource.print();
		vip.simplify.dao.entity.Test t = new vip.simplify.dao.entity.Test();
		t.setName("lcy");
		System.out.println("count============================="+BaseDao.getIns(vip.simplify.dao.entity.Test.class).count(t));
	}
	@Test
	public void s10_findByTest() {
		vip.simplify.dao.entity.Test t = new vip.simplify.dao.entity.Test();
		t.setName("lcy");
		List<vip.simplify.dao.entity.Test> list = BaseDao.getIns(vip.simplify.dao.entity.Test.class).findBy(t);
		for (vip.simplify.dao.entity.Test test : list) {
			System.out.println(test.getFid()+test.getName());
		}
	}
	@Test
	public void s10_findByPojoTest() {
		vip.simplify.dao.entity.Test test = BaseDao.getInsPojo().find(vip.simplify.dao.entity.Test.class, "select fid,name from test_web").get(0);
		System.out.println(test.getName());
	}
	
	@Test
	public void s11_insertUserTest() {
		TestUser t = new TestUser();
		t.setFid(3);
		t.setName("卢创业");
		t.setCreateId(1);
		t.setUpdateId(1);
		t.setCreateTime(new Date());
		t.setUpdateTime(new Date());
		System.out.println("save============================="+BaseDao.getIns(TestUser.class).save(t));
		System.out.println("saveUserTestGenId:"+t.getFid());
		BaseDao.getIns(TestUser.class).remove(3);
		System.out.println("removeUserTestGenId:3");
	}
}
