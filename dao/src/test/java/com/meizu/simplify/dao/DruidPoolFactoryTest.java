package com.meizu.simplify.dao;

import java.sql.Connection;

import org.junit.BeforeClass;
import org.junit.Test;

import com.meizu.simplify.dao.datasource.ConnectionFactory;
import com.meizu.simplify.dao.datasource.DruidPoolFactory;
import com.meizu.simplify.ioc.Startup;

public class DruidPoolFactoryTest {
	
	
	@BeforeClass
	public static void init() {
		Startup.start();
	}
	

	@Test
	public void testPool() {
		
//		Connection conn2 = DruidPoolFactory.getConnection();
		// 开启事务1
		DruidPoolFactory.startTransaction();
		System.out.println("执行事务操作111111111111111....");
		ConnectionFactory.commit();
		
		// 开启事务2
		DruidPoolFactory.startTransaction();
		System.out.println("执行事务操作222222222222....");
		ConnectionFactory.commit();
		ConnectionFactory.close();
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {

				public void run() {
					Connection conn2 = DruidPoolFactory.getConnection();
					for (int i = 0; i < 2; i++) {
						DruidPoolFactory.startTransaction();
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