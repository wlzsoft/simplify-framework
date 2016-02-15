package com.meizu.simplify.dao;

import java.sql.Connection;

import com.meizu.simplify.dao.datasource.DruidPoolFactory;

public class DruidPoolFactoryTest {

	public void testPool() {
		
		Connection conn2 = DruidPoolFactory.getConnection();
		// 开启事务1
		DruidPoolFactory.startTransaction();
		System.out.println("执行事务操作111111111111111....");
		DruidPoolFactory.commit();
		
		// 开启事务2
		DruidPoolFactory.startTransaction();
		System.out.println("执行事务操作222222222222....");
		DruidPoolFactory.commit();
		DruidPoolFactory.close();
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {

				public void run() {
					Connection conn2 = DruidPoolFactory.getConnection();
					for (int i = 0; i < 2; i++) {
						DruidPoolFactory.startTransaction();
						System.out.println(conn2);
						System.out.println(Thread.currentThread().getName() + "执行事务操作。。。。。。。。。。。。。");
						DruidPoolFactory.commit();
					}
					DruidPoolFactory.close();
				}
			}).start();
		}

	}
}