package com.meizu.simplify.cache.redis.dao;

import java.util.List;

import org.junit.Test;

import com.meizu.simplify.cache.enums.CacheExpireTimeEnum;
import com.meizu.simplify.cache.enums.TimeEnum;
import com.meizu.simplify.cache.redis.dao.impl.ListRedisDao;
import com.meizu.simplify.stresstester.StressTestUtils;
import com.meizu.simplify.stresstester.core.StressTask;
import com.meizu.simplify.utils.entity.User;

public class ListRedisDaoTest {
	public static ListRedisDao client = new ListRedisDao("redis_ref_hosts");
	
	/**
	 * 
	 * 方法用途: 未测试通过，需调整<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void testListInsertStress() {
		StressTestUtils.testAndPrint(100, 1000, new StressTask(){
			@Override
			public Object doTask() throws Exception {
				testListInsert();
				return null;
			}
		});
	}
	
	@Test
	public void testListGet(){
		String key = "producer_list1";
		long begin = System.currentTimeMillis();
		List list = client.lrange(key,0,100);
		client.lpop(key);
		System.out.println(client.lpop(key));
		System.out.println(list.size());
		System.out.println("time:"+(System.currentTimeMillis()-begin));
	}
	
	/**
	 * 
	 * 方法用途: 未测试通过，需调整<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public  void testListInsert(){
		
		String key = "producer_list";
		User usr = new User("101001","testname");
		usr.setAddr("sfsdfsfff");
		usr.setPhone("131321324234324");
		client.lpush(key, "{'name':'lcy',id:1}");
	}
	
	
	
	@Test
	public  void testConnect(){
		client.lpush("test", "test211112223", 60);
		String value = client.lpop("test");
		System.out.println(value);
	}
	
	
	
	/**
	 * 
	 * 方法用途: 未通过测试，需调整<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public  void testLpushStress(){
		long begin = System.currentTimeMillis();
		StressTestUtils.testAndPrint(100, 10000, new StressTask(){
			int i=0;
			@Override
			public Object doTask() throws Exception {
				client.lpush("test", "test"+i++);
				client.expire("test", CacheExpireTimeEnum.CACHE_EXP_SENDCONDs,TimeEnum.SECONDS);
				return null;
			}
			
		});
		long end = System.currentTimeMillis();
		System.out.println(end-begin);
	}

}
