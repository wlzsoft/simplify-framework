package com.meizu.simplify.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.test.SimplifyJUnit4ClassRunner;

@RunWith(SimplifyJUnit4ClassRunner.class)
@Bean
public class HostRouteServiceTest {
	@Test
	public void testSwitchHost() {
		new HostRouteService().switchHost();
	}
	@Test
	public void testHostRoute() {
		Connection virtualConnection = new MutilDataSourceConnection();
		Connection conn = new MutilDataSourceConnectionWrapper(virtualConnection);
		try {
			System.out.println(conn.isClosed());
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
