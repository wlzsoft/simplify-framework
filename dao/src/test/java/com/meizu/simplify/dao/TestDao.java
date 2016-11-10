package com.meizu.simplify.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.meizu.simplify.dao.datasource.DataSourceManager;


public class TestDao  {
 
	public Object findByTo(short to,int top)  {
		try {
			Connection conn = DataSourceManager.getConnection();
			conn.prepareStatement("select * from test where id = ?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null; 
	}
	 
}