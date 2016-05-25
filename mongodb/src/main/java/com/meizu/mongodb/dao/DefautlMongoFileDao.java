package com.meizu.mongodb.dao;

import com.meizu.mongodb.DataBase;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.InitBean;
import com.meizu.simplify.ioc.annotation.Resource;

@Bean
public class DefautlMongoFileDao extends  MongoFileDao<DataBase>{

	@Resource
	private DefaultMongoSource defaultMongoSource;
	
	@InitBean
	public void init() {
		db=defaultMongoSource.getDb();
	}
}
