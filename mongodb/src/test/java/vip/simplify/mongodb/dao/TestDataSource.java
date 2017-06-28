package vip.simplify.mongodb.dao;

import vip.simplify.mongodb.MongoConn;
import vip.simplify.mongodb.config.MongodbPropertiesConfig;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.utils.PropertieUtil;
import com.mongodb.client.MongoDatabase;
/**
 * 数据源
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月24日 下午6:23:32</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月24日 下午6:23:32</p>
 * @author <a href="mailto:meizu@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 0.1
 *
 */
@Bean
public class TestDataSource extends MongoConn {
	
	@Inject
	public MongodbPropertiesConfig mongodbPropertiesConfig;
	private MongoDatabase db;

	public TestDataSource() {
		PropertieUtil propertieUtil = new PropertieUtil("mongo.properties");
		host = propertieUtil.getString("mongo.host");
		dbName = propertieUtil.getString("mongo.log_databaseName");
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