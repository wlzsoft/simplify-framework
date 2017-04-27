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
import vip.simplify.ioc.annotation.InitBean;
import vip.simplify.utils.ReflectionGenericUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
/**
 * <p>dao操作基类</p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月23日 上午11:41:35</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月23日 上午11:41:35</p>
 * @author <a href="mailto:wanghaibin@meizu.com" title="邮箱地址">wanghaibin</a>
 * @version Version 3.0
 * @param <T> collection集合
 */
public class MongoBaseDao<T> {
	private static final Logger logger = LoggerFactory.getLogger(MongoBaseDao.class);
	private Class<T> entityClass;
	private MongoCollection<Document> dbCollection;

	@InitBean
	public void init() {
		entityClass = getEntityClass();
	}
	/**
	 * 方法用途: 数据删除
	 * 操作步骤: TODO<br>
	 * @param key 字段名
	 * @param value 值
	 * @return
	 */
	public boolean delete( String key, Object value) {
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
	 * @param deleMap 删除条件（key：字段名，value：对应值）
	 * @return
	 */
	public boolean delete(Map<String, Object> deleMap) {
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
	 * @param map 查询条件（key：字段名称  vlaue：对应值）
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<T> findByPage( Map<String, Object> map, int pageSize, int currentPage) {
		Page<T> resultList = null;
		long startTime = System.currentTimeMillis();
		try {
			BasicDBObject queryObj=buildSearchFilter(map);
			Long count = getDbCollection().count(queryObj);// 总条数
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
		}
		logger.info("总耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
		return resultList;
	}

	/**
	 * 方法用途: 保存数据<br>
	 * 操作步骤: TODO<br>
	 * @param t
	 * @return
	 */
	public boolean inSertOne( T t) {
		try {
			dbCollection.insertOne(buildDocument(t));
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 方法用途: 判断是数据是否存在<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean isExit( String key, Object value) {
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
	public boolean update(String key, Object value, T t) {
		UpdateResult result = null;
		if (null == key || value == null) {
			return false;
		} else {
			try {
				result = dbCollection.updateOne(Filters.eq(key, value), buildDocument(t));
				if (result.getModifiedCount() > 0) {
					return true;
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private Class<T> getEntityClass() {
		return (Class<T>) ReflectionGenericUtil.getSuperClassGenricTypeForFirst(getClass());
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
				Object value = t.getClass().getMethod("get" + upperName).invoke(t);
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
	public MongoCollection<Document> getDbCollection() {
		return dbCollection;
	}
	public void setDbCollection(MongoCollection<Document> dbCollection) {
		this.dbCollection = dbCollection;
	}
	
}
