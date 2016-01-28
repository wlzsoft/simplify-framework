package com.meizu.dao.mybatis;

import com.meizu.simplify.ioc.annotation.Bean;



/**
 * 
 * <p>Title: 公用dao</p>
 * <p>Description: </p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * @version 1.0
 */
@Bean
public interface ICommonDao {
	
	/**
	 * 
	 * 方法用途: 获取mysql数据库的当前时间<br>
	 * 操作步骤: <br>
	 * @return
	 */
	public String getMysqlDbDate();
	
	
}
