package vip.simplify.mongodb.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import vip.simplify.entity.page.Page;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
/**
 * <p>默认dao</p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月24日 上午9:19:10</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月24日 上午9:19:10</p>
 * @author <a href="mailto:wanghaibin@meizu.com" >wanghaibin</a>
 * @version Version 3.0
 *
 */
@Bean
public class DefaultMongoDao{
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultMongoDao.class);
	@Inject
	private  DefaultMongoSource defaultMongoSource;
	
	/**
	 * 方法用途: 数据删除
	 * 操作步骤: TODO<br>
	 * @param dbName 数据库名称
	 * @param collectionName  集合名称
	 * @param key 字段名
	 * @param value 值
	 * @return
	 */
	public boolean delete(Class<?> entityClass,String key, Object value) {
		MongoCollection<Document> dbCollection=this.getConllection(entityClass.getSimpleName());
		if (key == null || value == null) {
			logger.error("删除mongodb数据字段和值不能为空！" + key + ":" + value);
			return false;
		}
		try {
			BasicDBObject delObj=new BasicDBObject();
			delObj.put(key, value);
			dbCollection.deleteMany(delObj);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
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
	public boolean delete(Class<?> entityClass,Map<String, Object> deleMap) {
		MongoCollection<Document> dbCollection=this.getConllection(entityClass.getSimpleName());
		if (null != deleMap && deleMap.size() > 0) {
			try {
				DeleteResult result = null; // 删除返回结果
				BasicDBObject delObj=buildSearchFilter(deleMap);
				result = dbCollection.deleteMany(delObj); // 执行删除操作
				if(result.getDeletedCount()>0){
					return true;
				}
				return false;
			} catch (Exception e) {
				logger.error(e.getMessage());
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
	public Page<Object> findByPage(Class<?> entityClass, Map<String, Object> map, int pageSize, int currentPage) {
		MongoCollection<Document> dbCollection=this.getConllection(entityClass.getSimpleName());
		Page<Object> resultList = null;
		long startTime = System.currentTimeMillis();
		try {
			BasicDBObject queryObj = buildSearchFilter(map);
			Long count = dbCollection.count(queryObj);// 总条数
			List<Object> list = new ArrayList<Object>();
			Iterator iter = dbCollection.find(queryObj).sort(new BasicDBObject("createTime", -1)).skip((currentPage - 1) * pageSize).limit(pageSize).iterator(); // 查询获取数据
			while (iter.hasNext()) {
				Document doc=(Document) iter.next();
				list.add(JSON.parse(doc.toJson()));
			}
			logger.info("mongDB查询耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
			resultList = new Page(currentPage, pageSize, count.intValue(), true);// 创建返回的结果集
			resultList.setResults(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
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
	public boolean inSertOne(Object entity) {
		MongoCollection<Document> dbCollection=this.getConllection(entity.getClass().getSimpleName());
		try {
			dbCollection.insertOne(buildDocument(entity));
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
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
	public boolean isExit(Class<?> entityClass, String key, Object value) {
		MongoCollection<Document> dbCollection=this.getConllection(entityClass.getSimpleName());
		if (key != null && value != null) {
			try {
				BasicDBObject obj = new BasicDBObject(); // 构建查询条件
				obj.put(key, value);
				if (dbCollection.count(obj) > 0) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return false;
	}
	/**
	 * 方法用途: 更新数据<br>
	 * 操作步骤: TODO<br>
	 * @param key 字段名称
	 * @param value 字段值
	 * @param t 需要更新的数据
	 * @return
	 */
	public boolean update(String key, Object value,Class<?> entityClass) {
		MongoCollection<Document> dbCollection=this.getConllection(entityClass.getSimpleName());
		UpdateResult result = null;
		if (null == key || value == null) {
			return false;
		} else {
			try {
				result = dbCollection.updateOne(Filters.eq(key, value), buildDocument(entityClass));
				if (result.getModifiedCount() > 0) {
					return true;
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return false;
	}
	/**
	 * 方法用途: 构建单个实体<br>
	 * 操作步骤: TODO<br>
	 * @param t
	 * @return
	 */
	private Document buildDocument(Object entityClass) {
		Document doc = new Document();
		Field[] fields = entityClass.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldName = field.getName();
				String upperName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Object value = entityClass.getClass().getMethod("get" + upperName).invoke(entityClass);
				doc.append(fieldName, value);
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
	
	private MongoCollection<Document> getConllection(String conllectionName) {
		return defaultMongoSource.getDb().getCollection(conllectionName);
	}
}
