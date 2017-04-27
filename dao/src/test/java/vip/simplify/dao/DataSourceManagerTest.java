package vip.simplify.dao;

import java.sql.Connection;

import org.junit.Test;
import org.junit.runner.RunWith;

import vip.simplify.dao.datasource.ConnectionFactory;
import vip.simplify.dao.datasource.ConnectionManager;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.test.SimplifyJUnit4ClassRunner;

@RunWith(SimplifyJUnit4ClassRunner.class)
@Bean
public class DataSourceManagerTest {
	
	@Inject
	private ConnectionManager connectionManager;
	
	@Test
	public void testPool() {
		
		// 开启事务1
		connectionManager.startTransaction();
		System.out.println("执行事务操作111111111111111....");
		ConnectionFactory.commit();
		
		// 开启事务2
		connectionManager.startTransaction();
		System.out.println("执行事务操作222222222222....");
		ConnectionFactory.commit();
		ConnectionFactory.close();
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {

				public void run() {
					Connection conn2 = connectionManager.getConnection();
					for (int i = 0; i < 2; i++) {
						connectionManager.startTransaction();
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