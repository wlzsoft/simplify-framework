package com.meizu.mongodb.dao;

import com.meizu.mongodb.MongoConn;
import com.meizu.mongodb.config.MongodbPropertiesConfig;
import com.meizu.simplify.config.annotation.Config;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.utils.PropertieUtil;
import com.mongodb.client.MongoDatabase;
/**
 * <p>Desc:默认数据源 </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月24日 下午6:23:32</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月24日 下午6:23:32</p>
 * @author <a href="mailto:wanghaibin@meizu.com" title="邮箱地址">wanghaibin</a>
 * @version Version 3.0
 *
 */
@Bean
public class DefaultMongoSource extends MongoConn {
	
	@Resource
	public MongodbPropertiesConfig mongodbPropertiesConfig;
	private MongoDatabase db;
	@Config("mongo.host")
	private String defaultHost;
	@Config("mongo.databaseName")
	private String defaultDbName;
	@Config("mongo.url")
	private String url;

	public DefaultMongoSource() {
		PropertieUtil propertieUtil = new PropertieUtil("properties/mongo.properties");
		host = propertieUtil.getString("mongo.host");
		dbName = propertieUtil.getString("mongo.databaseName");
		url = propertieUtil.getString("mongo.url");
		this.setDb(getDatabase());
	}

	public MongoDatabase getDb() {
		return db;
	}

	public void setDb(MongoDatabase db) {
		this.db = db;
	}
	
}