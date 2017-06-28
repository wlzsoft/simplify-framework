package vip.simplify.mongodb.dao;

import com.mongodb.client.MongoDatabase;
import vip.simplify.config.annotation.Config;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.mongodb.MongoConn;
import vip.simplify.mongodb.config.MongodbPropertiesConfig;
import vip.simplify.utils.PropertieUtil;
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
	
	@Inject
	public MongodbPropertiesConfig mongodbPropertiesConfig;
	private MongoDatabase db;
	@Config("mongo.host")
	private String defaultHost;
	@Config("mongo.databaseName")
	private String defaultDbName;
	@Config("mongo.url")
	private String url;

	public DefaultMongoSource() {
		PropertieUtil propertieUtil = new PropertieUtil("mongo.properties");
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