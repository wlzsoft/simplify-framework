package com.meizu.mongodb;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.entity.page.Page;
import com.meizu.simplify.ioc.annotation.InitBean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

public class MongoDBClient<T>  {
	
	private static final Logger logger = LoggerFactory.getLogger(MongoDBClient.class);
	/**
	 * MongoClient的实例代表数据库连接池，是线程安全的，可以被多线程共享，客户端在多线程条件下仅维持一个实例即可
	 * Mongo是非线程安全的，目前mongodb API中已经建议用MongoClient替代Mongo
	 */
	public static  MongoClient mongoClient = null;
	private Class<T> entityClass;
	
	@Resource
	private PropertiesConfig properties;

	@InitBean
	public void init() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((java.lang.reflect.ParameterizedType) genType).getActualTypeArguments();
		entityClass = (Class) params[0];
		
		if (mongoClient == null) {
			MongoClientOptions.Builder build = new MongoClientOptions.Builder();
			build.connectionsPerHost(50);// 与目标数据库能够建立的最大connection数量为50
			build.threadsAllowedToBlockForConnectionMultiplier(50); // 如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待
			/*
			 * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为2分钟
			 * 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception
			 * 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败
			 */
			build.maxWaitTime(1000 * 60 * 2);
			build.connectTimeout(1000 * 60 * 1); // 与数据库建立连接的timeout设置为1分钟
			MongoClientOptions myOptions = build.build();
			try {
				// 数据库连接实例
				mongoClient = new MongoClient(properties.getProp().getString("system.mongo.host"), myOptions);
//				mongoClient = new MongoClient("10.2.67.28", myOptions);
			} catch (MongoException e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	public MongoDBClient() {
//		init();
	}

	public boolean delete(String dbName, String collectionName, String[] keys, Object[] values) {
		DB db = null;
		DBCollection dbCollection = null;
		if (keys != null && values != null) {
			if (keys.length != values.length) { // 如果keys和values不对等，直接返回false
				return false;
			} else {
				try {
					db = mongoClient.getDB(dbName); // 获取指定的数据库
					dbCollection = db.getCollection(collectionName); // 获取指定的collectionName集合
					BasicDBObject doc = new BasicDBObject(); // 构建删除条件
					WriteResult result = null; // 删除返回结果
					for (int i = 0; i < keys.length; i++) {
						doc.put(keys[i], values[i]); // 添加删除的条件
					}
					result = dbCollection.remove(doc); // 执行删除操作
					return result.isUpdateOfExisting();
				} catch (Exception e) {
					logger.error(e.getMessage());
				} finally {
					if (null != db) {
						// db.requestDone(); // 关闭db
						db = null;
					}
				}

			}
		}
		return false;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<T> findByPage(String dbName, String collectionName, Map<String, Object> map, int pageSize, int currentPage) {
		Page<T> resultList=null;
		DB db = null;
		DBCollection dbCollection = null;
		DBCursor cursor = null;
		long startTime=System.currentTimeMillis();
		try {
			db = mongoClient.getDB(dbName); // 获取数据库实例
			dbCollection = db.getCollection(collectionName); // 获取数据库中指定的collection集合
			BasicDBObject queryObj = new BasicDBObject(); // 构建查询条件
			if (null != map && map.size() > 0) {
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					Object key = entry.getKey();
					Object val = entry.getValue();
					queryObj.put(key.toString(), val);
				}
			}
			Integer count=dbCollection.find(queryObj).count();//总条数
			cursor = dbCollection.find(queryObj).sort(new BasicDBObject("createTime",-1)).skip((currentPage-1)*pageSize).limit(pageSize); // 查询获取数据
			logger.info("mongDB查询耗时："+(System.currentTimeMillis()-startTime)+"毫秒");
			List<T> list=new ArrayList<T>();
			while (cursor.hasNext()) {
				list.add(JSON.parseObject(JSON.toJSONString(cursor.next()),entityClass));
			}
			resultList = new Page(currentPage, pageSize, count,true);// 创建返回的结果集
			resultList.setResults(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (null != cursor) {
				cursor.close();
			}
			if (null != db) {
				// db.requestDone(); // 关闭数据库请求
			}
		}
		logger.info("总耗时："+(System.currentTimeMillis()-startTime)+"毫秒");
		return resultList;
	}
	
	public boolean inSert(String dbName, String collectionName, String[] keys, Object[] values) {
		DB db = null;
		DBCollection dbCollection = null;
		WriteResult result = null;
		if (keys != null && values != null) {
			if (keys.length != values.length) {
				return false;
			} else {
				db = mongoClient.getDB(dbName); // 获取数据库实例
				dbCollection = db.getCollection(collectionName); // 获取数据库中指定的collection集合
				BasicDBObject insertObj = new BasicDBObject();
				for (int i = 0; i < keys.length; i++) { // 构建添加条件
					insertObj.put(keys[i], values[i]);
				}
				try {
					result = dbCollection.insert(insertObj);
				} catch (Exception e) {
					logger.error(e.getMessage());
				} finally {
					if (null != db) {
						// db.requestDone(); // 请求结束后关闭db
					}
				}
				return result.isUpdateOfExisting();
			}
		}
		return false;
	}

	public boolean isExit(String dbName, String collectionName, String key, Object value) {
		DB db = null;
		DBCollection dbCollection = null;
		if (key != null && value != null) {
			try {
				db = mongoClient.getDB(dbName); // 获取数据库实例
				dbCollection = db.getCollection(collectionName); // 获取数据库中指定的collection集合
				BasicDBObject obj = new BasicDBObject(); // 构建查询条件
				obj.put(key, value);

				if (dbCollection.count(obj) > 0) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {
				if (null != db) {
					// db.requestDone(); // 关闭db
					db = null;
				}
			}

		}
		return false;
	}

	public boolean update(String dbName, String collectionName, DBObject oldValue, DBObject newValue) {
		DB db = null;
		DBCollection dbCollection = null;
		WriteResult result = null;
		if (oldValue.equals(newValue)) {
			return true;
		} else {
			try {
				db = mongoClient.getDB(dbName); // 获取数据库实例
				dbCollection = db.getCollection(collectionName); // 获取数据库中指定的collection集合
				result = dbCollection.update(oldValue, newValue);
				return result.isUpdateOfExisting();
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {
				if (null != db) {
					// db.requestDone(); // 关闭db
					db = null;
				}
			}

		}
		return false;
	}

	public DBCollection getCollection(String dbName, String collectionName) {
		return mongoClient.getDB(dbName).getCollection(collectionName);
	}

	public DB getDb(String dbName) {
		return mongoClient.getDB(dbName);
	}


	public GridFSInputFile save(String dbName, String collectionName,InputStream io,String name, T t) {
		DB db = mongoClient.getDB(dbName); // 获取指定的数据库
		DBCollection dbCollection = db.getCollection(collectionName); // 获取指定的collectionName集合
		GridFS gfs = new GridFS(dbCollection.getDB());
		GridFSInputFile mongofile = gfs.createFile(io,name);
			mongofile.setMetaData((DBObject) JSON.parse(t.toString()));
			mongofile.save();
			return mongofile;
	}
}
