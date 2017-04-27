package vip.simplify.mongodb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;

/**
 * <p>mongodb数据连接</p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月23日 上午11:41:35</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月23日 上午11:41:35</p>
 * @author <a href="mailto:wanghaibin@meizu.com" title="邮箱地址">wnaghaibin</a>
 * @version Version 3.0
 *
 */
public class MongoConn {
	
	private static final Logger logger = LoggerFactory.getLogger(MongoConn.class);
	public static String dbName;
	public static String host;
	public static String url;//mongodb://localhost:27017,localhost:27018,localhost:27019
	
	/**
	 * 方法用途: 获取客户端连接<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static MongoClient getConn() {
		MongoClientOptions.Builder build = new MongoClientOptions.Builder();
		//TODO 待测试验证调优参数
//		build.connectionsPerHost(50);// 与目标数据库能够建立的最大connection数量为50
//		build.threadsAllowedToBlockForConnectionMultiplier(100); // 初始化100个线程
//		build.maxWaitTime(1000 * 60 * 2);// 在成功获取到一个可用数据库连接之前的最长等待时间2分钟
//		build.connectTimeout(1000 * 60 * 1); // 与数据库建立连接的timeout设置为1分钟
		MongoClientOptions myOptions = build.build();
		try {
			// 数据库连接实例
			MongoClient mongoClient =null;
			if(host!=null){
				mongoClient=new MongoClient(host, myOptions);
			}else{
				MongoClientURI connectionString = new MongoClientURI(url,build);
				mongoClient = new MongoClient(connectionString);
			}
			return mongoClient;
		} catch (MongoException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 方法用途:  获取指定数据库<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public  MongoDatabase getDatabase() {
		MongoClient mongoClient=MongoConn.getConn();
		if (mongoClient!= null) {
			try {
				return mongoClient.getDatabase(dbName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
