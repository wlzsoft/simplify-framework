package com.meizu.simplify.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.meizu.simplify.dao.orm.base.ISqlDataCallback;
import com.meizu.simplify.dao.orm.base.SQLExecute;
import com.meizu.simplify.datasource.route.HostRouteService;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.log.Logger;
import com.meizu.simplify.test.SimplifyJUnit4ClassRunner;

@RunWith(SimplifyJUnit4ClassRunner.class)
@Bean
public class HostRouteServiceTest {
	
	@Resource
	private Logger logger;
	
	@Resource
	private MutilDataSource dsp;
	
	@Test
	public void testSwitchHost() {
		logger.info(HostRouteService.switchHost().getName());
	}
	@Test
	public void testHostRoute() {
		Connection conn = null;
		try {
			conn = dsp.getConnection();
			conn.setAutoCommit(true);
			List<Map<String,Object>> list = SQLExecute.executeQuery(conn, "select * from test_web", new ISqlDataCallback<Map<String,Object>>() {
				@Override
				public Map<String,Object> paramCall(PreparedStatement prepareStatement,Object... obj) throws SQLException {
					return ISqlDataCallback.super.paramCall(prepareStatement);
				}
				@Override
				public Map<String,Object> resultCall(String columnLabel, Object val,Map<String,Object> t) {
					if(t == null) {
						t = new HashMap<>();
					}
					t.put(columnLabel, val);
					return t;
				}
			},null);
			logger.info(list.size()+"");
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
