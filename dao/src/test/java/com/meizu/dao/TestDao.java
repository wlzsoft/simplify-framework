package com.meizu.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.meizu.dao.datasource.DruidPoolFactory;


public class TestDao  {
 
	public Object findByTo(short to,int top)  {
		try {
			Connection conn = DruidPoolFactory.getConnection();
			conn.prepareStatement("select * from test where id = ?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null; 
	}
	 
}