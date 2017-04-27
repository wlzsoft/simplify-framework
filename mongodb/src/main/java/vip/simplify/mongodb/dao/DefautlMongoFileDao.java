package vip.simplify.mongodb.dao;

import vip.simplify.mongodb.DataBase;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.InitBean;
import vip.simplify.ioc.annotation.Resource;

@Bean
public class DefautlMongoFileDao extends  MongoFileDao<DataBase>{

	@Resource
	private DefaultMongoSource defaultMongoSource;
	
	@InitBean
	public void init() {
		db=defaultMongoSource.getDb();
	}
}
