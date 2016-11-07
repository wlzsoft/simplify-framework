package com.meizu.simplify.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.meizu.simplify.dao.datasource.DruidPoolFactory;
import com.meizu.simplify.test.SimplifyJUnit4ClassRunner;

@RunWith(SimplifyJUnit4ClassRunner.class)
public class DataSourceTest {
	@Test
	public void test() {
		Connection conn = DruidPoolFactory.getConnection();
		try {
			boolean isAutoCommit = conn.getAutoCommit();
			if(isAutoCommit) {
//				insert
			} else {
//				select
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
