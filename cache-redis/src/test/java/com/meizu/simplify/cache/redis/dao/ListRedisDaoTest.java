package com.meizu.simplify.cache.redis.dao;

import java.util.List;

import org.junit.Test;

import com.meizu.simplify.cache.enums.CacheExpireTimeEnum;
import com.meizu.simplify.cache.enums.TimeEnum;
import com.meizu.simplify.cache.redis.dao.impl.ListRedisDao;
import com.meizu.simplify.stresstester.StressTestUtils;
import com.meizu.simplify.stresstester.core.StressTask;

public class ListRedisDaoTest {
	public static ListRedisDao client = new ListRedisDao("redis_ref_hosts");
	
	/**
	 * 
	 * 方法用途: 模拟队列弹出操作<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void testListGet() {
		String key = "product_list";
		long begin = System.currentTimeMillis();
		List<String> list = client.lrange(key,0,20);
		System.out.println(client.lpop(key));
		System.out.println(list.size());
		System.out.println("time:"+(System.currentTimeMillis()-begin));
	}
	
	/**
	 * 
	 * 方法用途: 模拟入队操作,插入队头<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void testListLpush() {
		String key = "product_list";
		client.lpush(key, "{'name':'lcy',id:2}");
	}
	
	/**
	 * 
	 * 方法用途: 测试模拟队列的入队和弹出，在限定时间内，如果超过60秒，数据自动删除<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void testQueuePushAndPopByTime() {
		client.lpush("test", "test211112223", 60);
		String value = client.lpop("test");
		System.out.println(value);
	}
	
	/**
	 * 
	 * 方法用途: 压力测试list结构批量入队性能<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void testLpushStress() {
		long begin = System.currentTimeMillis();
		StressTestUtils.testAndPrint(100, 1000, new StressTask() {
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
