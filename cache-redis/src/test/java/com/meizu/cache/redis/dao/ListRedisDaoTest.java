package com.meizu.cache.redis.dao;

import java.util.List;

import org.junit.Test;

import com.meizu.cache.enums.CacheExpireTimeEnum;
import com.meizu.cache.enums.TimeEnum;
import com.meizu.cache.redis.dao.impl.ListRedisDao;
import com.meizu.simplify.utils.entity.User;
import com.meizu.stresstester.StressTestUtils;
import com.meizu.stresstester.core.StressTask;

public class ListRedisDaoTest {
	public static ListRedisDao client = new ListRedisDao("redis_ref_hosts");
	
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
