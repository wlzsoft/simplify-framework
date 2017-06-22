package vip.simplify.cache.redis.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.fastjson.TypeReference;

import vip.simplify.cache.dao.IJsonCacheDao;
import vip.simplify.cache.entity.Goods;
import vip.simplify.cache.entity.User;
import vip.simplify.cache.enums.CacheExpireTimeEnum;
import vip.simplify.cache.redis.dao.impl.JsonRedisDao;
import vip.simplify.ioc.Startup;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午5:54:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午5:54:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */

public class JsonRedisDaoTest {
	
	IJsonCacheDao<User> dao = new JsonRedisDao<>("redis_ref_hosts",User.class);
	@SuppressWarnings("rawtypes")
	IJsonCacheDao<Goods> godosDao = new JsonRedisDao<>("redis_ref_hosts",Goods.class);
	
	@BeforeClass
	public static void before() {
		Startup.start();
	}
	
	@Test
	public void testSet() {
		User user = new User();
		user.setName("lcy");
		Assert.assertTrue(dao.set("simplify_test",CacheExpireTimeEnum.CACHE_EXP_SENDCOND_60.timesanmp(), user));
		String name = dao.get("simplify_test").getName();
		System.out.println(name);
		Assert.assertEquals("lcy",name);
		//以下代码执行会冲突
		/*user.setName("lcy2");
		User userResult = dao.getAndSet("simplify_test", user);
		Assert.assertEquals(userResult.getName(),"lcy2");*/
	}
	
	@Test
	public void testGetSet() {
		User user = new User();
		user.setName("lcy2");
		User userResult = dao.getAndSet("simplify_test2", user);//数据不存在的情况下，第一次调用这个方法，会返回null
		Assert.assertEquals(userResult.getName(),"lcy2");
	}
	
	/**
	 * 
	 * 方法用途: 支持泛型转换<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void testSetByGenric() {
		User user = new User();
		user.setName("lcy234");
		Goods<User> goods = new Goods<>();
		goods.setT(user);
		Assert.assertTrue(godosDao.set("simplify_test1_Genric",CacheExpireTimeEnum.CACHE_EXP_SENDCOND_60.timesanmp(), goods));
	}
	
	/**
	 * 
	 * 方法用途: 支持泛型转换<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void testGetByGenric() {
		Goods<User> goodsResult = godosDao.get("simplify_test1_Genric", new TypeReference<Goods<User>>() {} );//数据不存在的情况下，第一次调用这个方法，会返回null
		Assert.assertEquals(goodsResult.getT().getName(),"lcy234");
	}
	
	@Test
	public void testDelete() {
//		boolean isDelete = dao.delete("FINDSALEPOINTBYFID");
//		Assert.assertTrue(isDelete);
	}
}
