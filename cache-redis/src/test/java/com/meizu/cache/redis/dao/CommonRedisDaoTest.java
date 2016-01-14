    package com.meizu.cache.redis.dao;  

import org.junit.Test;

import com.meizu.cache.ICacheDao;
import com.meizu.cache.redis.dao.impl.CommonRedisDao;
import com.meizu.stresstester.StressTestUtils;
import com.meizu.stresstester.core.StressTask;
public class CommonRedisDaoTest {
	
	ICacheDao<String, Object> commonRedisDao = new CommonRedisDao<>("redis_ref_hosts");
	@Test
	public void testInsertStress() {
		StressTestUtils.testAndPrint(1000, 10000, new StressTask(){
			@Override
			public Object doTask() throws Exception {
				commonRedisDao.set("age", 2);
				return null;
			}
		});
	}
	
	@Test
	public void testGet() {
		commonRedisDao.set("age", 2);
		System.out.println(commonRedisDao.get("age"));
	}
}
  