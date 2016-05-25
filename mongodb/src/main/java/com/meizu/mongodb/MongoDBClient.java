package com.meizu.mongodb;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.entity.page.Page;
import com.meizu.simplify.ioc.annotation.InitBean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.utils.ReflectionUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class MongoDBClient<T>  {
	
	private static final Logger logger = LoggerFactory.getLogger(MongoDBClient.class);
	public static  MongoClient mongoClient = null;
	private Class<T> entityClass;
	
	@Resource
	private PropertiesConfig properties;

	public MongoDBClient() {
	}
	
	@InitBean
	public void init() {
		entityClass = getEntityClass();
		if (mongoClient == null) {
			MongoClientOptions.Builder build = new MongoClientOptions.Builder();
			build.connectionsPerHost(50);// 与目标数据库能够建立的最大connection数量为50
			build.threadsAllowedToBlockForConnectionMultiplier(100); //初始化100个线程
			build.maxWaitTime(1000 * 60 * 2);//在成功获取到一个可用数据库连接之前的最长等待时间2分钟
			build.connectTimeout(1000 * 60 * 1); // 与数据库建立连接的timeout设置为1分钟
			MongoClientOptions myOptions = build.build();
			try {
				// 数据库连接实例
				mongoClient = new MongoClient(properties.getProp().getString("system.mongo.host"), myOptions);
			} catch (MongoException e) {
				logger.error(e.getMessage());
			}
		}
	}

	/**
	 * 方法用途: 数据删除
	 * 操作步骤: TODO<br>
	 * @param dbName 数据库名称
	 * @param collectionName  集合名称
	 * @param key 字段名
	 * @param value 值
	 * @return
	 */
	public boolean delete(String dbName, String collectionName, String key, Object value) {
		 MongoDatabase db = null;
		 MongoCollection<Document> dbCollection = null;
		if (key == null || value == null) {
			logger.error("删除mongodb数据字段和值不能为空！" + key + ":" + value);
			return false;
		}
		try {
			db = getDB(dbName); // 获取指定的数据库
			dbCollection = getCollection(db,collectionName); // 获取指定的collectionName集合
			BasicDBObject delObj=new BasicDBObject();
			delObj.put(key, value);
			dbCollection.deleteMany(delObj);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (null != db) {
				db = null;
			}
		}
		return false;
	}
	/**
	 * 方法用途: 多字段删除<br>
	 * 操作步骤: TODO<br>
	 * @param dbName 数据库名称
	 * @param collectionName  集合名称
	 * @param deleMap 删除条件（key：字段名，value：对应值）
	 * @return
	 */
	public boolean delete(String dbName, String collectionName, Map<String, Object> deleMap) {
		 MongoDatabase db = null;
		 MongoCollection<Document> dbCollection = null;
		if (null != deleMap && deleMap.size() > 0) {
			try {
				db = getDB(dbName); // 获取指定的数据库
				dbCollection = getCollection(db,collectionName); // 获取指定的collectionName集合
				DeleteResult result = null; // 删除返回结果
				BasicDBObject delObj=buildSearchFilter(deleMap);
				result = dbCollection.deleteMany(delObj); // 执行删除操作
				if(result.getDeletedCount()>0){
					return true;
				}
				return false;
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {
				if (null != db) {
					db = null;
					dbCollection=null;
				}
			}
		} else {
			logger.error("删除mongodb数据字段和值不能为空！");
		}
		return false;
	}

	/**
	 * 方法用途: 分页查询<br>
	 * 操作步骤: TODO<br>
	 * @param dbName 数据库表
	 * @param collectionName 集合名称（数据表）
	 * @param map 查询条件（key：字段名称  vlaue：对应值）
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<T> findByPage(String dbName, String collectionName, Map<String, Object> map, int pageSize, int currentPage) {
		Page<T> resultList = null;
		MongoDatabase db = null;
		MongoCollection<Document> dbCollection = null;
		DBCursor cursor = null;
		long startTime = System.currentTimeMillis();
		try {
			db = getDB(dbName); // 获取数据库实例
			dbCollection = getCollection(db,collectionName); // 获取数据库中指定的collection集合
			BasicDBObject queryObj=buildSearchFilter(map);
			Long count = dbCollection.count(queryObj);// 总条数
			List<T> list = new ArrayList<T>();
			Block<Document> printBlock = new Block<Document>() {
			     public void apply(final Document document) {
			    	 list.add(JSON.parseObject(JSON.toJSONString(document), entityClass));
			     }
			};
			dbCollection.find(queryObj).sort(new BasicDBObject("createTime", -1)).skip((currentPage - 1) * pageSize).limit(pageSize).forEach(printBlock); // 查询获取数据
			logger.info("mongDB查询耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
			resultList = new Page(currentPage, pageSize, count.intValue(), true);// 创建返回的结果集
			resultList.setResults(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (null != cursor) {
				cursor.close();
			}
		}
		logger.info("总耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
		return resultList;
	}

	/**
	 * 方法用途: 保存数据<br>
	 * 操作步骤: TODO<br>
	 * @param dbName
	 * @param collectionName
	 * @param t
	 * @return
	 */
	public boolean inSertOne(String dbName, String collectionName, T t) {
		MongoDatabase db = null;
		MongoCollection<Document> dbCollection = null;
		db = getDB(dbName); // 获取数据库实例
		dbCollection = getCollection(db,collectionName); // 获取数据库中指定的collection集合
		try {
			dbCollection.insertOne(buildDocument(t));
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (null != db) {
				db = null;
			}
			if (dbCollection != null) {
				dbCollection = null;
			}
		}
		return false;
	}

	/**
	 * 方法用途: 判断是数据是否存在<br>
	 * 操作步骤: TODO<br>
	 * @param dbName
	 * @param collectionName
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean isExit(String dbName, String collectionName, String key, Object value) {
		MongoDatabase db = null;
		MongoCollection<Document> dbCollection = null;
		if (key != null && value != null) {
			try {
				db = getDB(dbName); // 获取数据库实例
				dbCollection = getCollection(db,collectionName); // 获取数据库中指定的collection集合
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
					db = null;
				}
			}
		}
		return false;
	}
	@Deprecated
	//TODO 需修复
	public boolean update(String dbName, String collectionName,String key, Object value,T t) {
		MongoDatabase db = null;
		MongoCollection<Document> dbCollection = null;
		UpdateResult result = null;
		if (null==key || value==null) {
			return false;
		} else {
			try {
				db = getDB(dbName); // 获取数据库实例
				dbCollection = getCollection(db,collectionName); // 获取数据库中指定的collection集合
				result = dbCollection.updateMany(Filters.eq(key, value), buildDocument(t));
				if(result.getModifiedCount()>0){
					return true;
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {
				if (null != db) {
					db = null;
				}
			}
		}
		return false;
	}

	private MongoDatabase getDB(String dbName) {
		return mongoClient.getDatabase(dbName);
	}
	private MongoCollection<Document> getCollection(MongoDatabase db,String collectionName) {
		return db.getCollection(collectionName);
	}
	private Class<T> getEntityClass() {
		return ReflectionUtil.getSuperClassGenricType(getClass());
	}
	/**
	 * 方法用途: 构建单个实体<br>
	 * 操作步骤: TODO<br>
	 * @param t
	 * @return
	 */
	private Document buildDocument(T t) {
		Document doc = new Document();
		Field[] fields = t.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldName = field.getName();
				String upperName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Object value=t.getClass().getMethod("get" + upperName).invoke(t);
				doc.append(fieldName,value);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return doc;
	}
	/**
	 * 方法用途: 构建搜索条件<br>
	 * 操作步骤: TODO<br>
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private BasicDBObject buildSearchFilter(Map<String, Object> map) {
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
		return queryObj;
	}
}
