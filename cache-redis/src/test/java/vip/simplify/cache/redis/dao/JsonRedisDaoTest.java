package vip.simplify.cache.redis.dao;

import com.alibaba.fastjson.TypeReference;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import vip.simplify.cache.CacheProxyDao;
import vip.simplify.cache.dao.IJsonCacheDao;
import vip.simplify.cache.entity.Goods;
import vip.simplify.cache.entity.User;
import vip.simplify.cache.enums.CacheExpireTimeEnum;
import vip.simplify.cache.redis.dao.impl.JsonRedisDao;
import vip.simplify.exception.UncheckedException;
import vip.simplify.ioc.Startup;
import vip.simplify.utils.PropertieUtil;

import java.util.HashSet;
import java.util.Set;

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
		PropertieUtil.setConfigPrefix("properties/");
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
	public void testGetKeys() {
		CacheProxyDao.getJsonCache().set("testGetKeys1","lcy1");
		CacheProxyDao.getJsonCache().set("testGetKeys2","lcy12");
		Set<String> keySet = CacheProxyDao.getJsonCache().keys("testGetKeys*");
		System.out.println("sss:"+ keySet);
	}
	
	@Test
	public void testGetKeysExcepion() {
		CacheProxyDao.getJsonCache().set("testGetKeys1","lcy1");
		CacheProxyDao.getJsonCache().set("testGetKeys2","lcy12");
		try {
			Set<String> keySet = CacheProxyDao.getJsonCache().keys("*testGetKeys");
			System.out.println("sss:"+ keySet);
		} catch (UncheckedException e) {
			if (e.getMessage().equals("只允许左边前缀匹配key") ) {
				Assert.assertTrue(true);
			}
		}
	}
	
	@Test
	public void testGetKeysAndDelete() {
		CacheProxyDao.getJsonCache().set("testGetKeysAndDelete1","lcy13");
		CacheProxyDao.getJsonCache().set("testGetKeysAndDelete2","lcy123");
		Set<String> keySet = CacheProxyDao.getJsonCache().keys("testGetKeysAndDelete*");
		System.out.println("testGetKeysAndDelete_keySet:"+ keySet);
		Assert.assertEquals(2, keySet.size());
		System.out.println("testGetKeysAndDelete_deleteCount:"+CacheProxyDao.getJsonCache().delete(keySet));
		Set<String> hitKeySet = CacheProxyDao.getJsonCache().keys("testGetKeysAndDelete*");
		Assert.assertArrayEquals(new String[]{},hitKeySet.toArray(new String[hitKeySet.size()]));
	}
	
	@Test
	public void testDeleteByKeys() {
		Set<String> keySet  = new HashSet<>();
		keySet.add("testGetKeys1");
		keySet.add("testGetKeys2");
		System.out.println("testDeleteByKeys:"+CacheProxyDao.getJsonCache().delete(keySet));
	}
	
	@Test
	public void testDeletebyKey() {
		boolean isDelete = dao.delete("FINDSALEPOINTBYFID");
		Assert.assertTrue(isDelete);
	}
	
	@Test
	public void testSearchAndDelete() {
		CacheProxyDao.getJsonCache().set("testSearchAndDelete1","lcy13");
		CacheProxyDao.getJsonCache().set("testSearchAndDelete2","lcy123");
		Long deleteCount = dao.searchAndDelete("testSearchAndDelete*");
		Assert.assertEquals(2, deleteCount.intValue());
	}
	
}
