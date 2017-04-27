package vip.simplify.mongodb.dao;

import vip.simplify.ioc.annotation.Inject;
import vip.simplify.mongodb.DataBase;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.InitBean;

@Bean
public class DefautlMongoFileDao extends  MongoFileDao<DataBase>{

	@Inject
	private DefaultMongoSource defaultMongoSource;
	
	@InitBean
	public void init() {
		db=defaultMongoSource.getDb();
	}
}
