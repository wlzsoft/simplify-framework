package com.meizu.mongodb.dao;

import com.meizu.mongodb.DataBase;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.InitBean;
import com.meizu.simplify.ioc.annotation.Resource;
/**
 * <p>dao</p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月24日 上午9:19:10</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月24日 上午9:19:10</p>
 * @author <a href="mailto:meizu@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 0.1
 *
 */
@Bean
public class TestDao extends  MongoFileDao<DataBase>{
	
	@Resource
	private TestDataSource logDataSource;
	
	@InitBean
	public void init() {
		db=logDataSource.getDb();
	}
	
}
