package com.meizu.simplify.dao;

import java.sql.Connection;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.meizu.simplify.dao.datasource.ConnectionFactory;
import com.meizu.simplify.dao.datasource.DataSourceManager;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.test.SimplifyJUnit4ClassRunner;

@RunWith(SimplifyJUnit4ClassRunner.class)
@Bean
public class DataSourceManagerTest {
	
	@Resource
	private DataSourceManager dataSourceManager;
	
	@Test
	public void testPool() {
		
		// 开启事务1
		dataSourceManager.startTransaction();
		System.out.println("执行事务操作111111111111111....");
		ConnectionFactory.commit();
		
		// 开启事务2
		dataSourceManager.startTransaction();
		System.out.println("执行事务操作222222222222....");
		ConnectionFactory.commit();
		ConnectionFactory.close();
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {

				public void run() {
					Connection conn2 = dataSourceManager.getConnection();
					for (int i = 0; i < 2; i++) {
						dataSourceManager.startTransaction();
						System.out.println(conn2);
						System.out.println(Thread.currentThread().getName() + "执行事务操作。。。。。。。。。。。。。");
						ConnectionFactory.commit();
					}
					ConnectionFactory.close();
				}
			}).start();
		}

	}
}