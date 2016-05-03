package com.meizu.mongodb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
//import java.util.function.Consumer;





import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.data.mongodb.gridfs.GridFsOperations;





import com.meizu.simplify.ioc.annotation.Bean;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;


/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月3日 上午10:25:59</p>
 * <p>Modified By:Administrator-</p>
 * <p>Modified Date:2016年5月3日 上午10:25:59</p>
 * @author <a href="mailto:wanglizong@meizu.com" title="邮箱地址">wanglizong</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class MongoFileDao<T> extends MongoDBClient<T> {
	private String selfName = "";
	public MongoFileDao() {
		
	}
	/**
	 * 
	 * 方法用途: 获取泛型对象class<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> getEntityClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * 
	 * 方法用途: 插入文件<br>
	 * 操作步骤: TODO<br>
	 * @param name
	 * @param t
	 * @return
	 */
	public GridFSInputFile save(String dbName, String collectionName,InputStream io,String name, T t) {
		DB db = mongoClient.getDB(dbName); // 获取指定的数据库
		DBCollection dbCollection = db.getCollection(collectionName); // 获取指定的collectionName集合
		GridFS gfs = new GridFS(dbCollection.getDB(),collectionName);
		GridFSInputFile mongofile = gfs.createFile(io,name);
			mongofile.setMetaData((DBObject) JSON.parse(t.toString()));
			mongofile.save();
			return mongofile;
	}
	/**
	 * 
	 * 方法用途: 插入文件<br>
	 * 操作步骤: TODO<br>
	 * @param file
	 * @param t
	 * @return
	 */
	public String save(String dbName, String collectionName,File file, T t,Integer... size) {
		DB db = mongoClient.getDB(dbName); // 获取指定的数据库
		DBCollection dbCollection = db.getCollection(collectionName); // 获取指定的collectionName集合
		GridFS gfs = new GridFS(dbCollection.getDB(), collectionName);
		try {
			GridFSInputFile mongofile = gfs.createFile(file);
			mongofile.setMetaData((DBObject) JSON.parse(t.toString()));
			if(size!=null&&size.length>0) {
				mongofile.save(size[0]);
			} else {
				mongofile.save();
			}
			return ((ObjectId) mongofile.getId()).toString();
		} catch ( IOException e ) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 
	 * 方法用途:  取文件<br>
	 * 操作步骤: TODO<br>
	 * @param _id
	 * @return
	 */
	public GridFSDBFile findFileById(String dbName, String collectionName,String _id) {
		DB db = mongoClient.getDB(dbName); // 获取指定的数据库
		DBCollection dbCollection = db.getCollection(collectionName); // 获取指定的collectionName集合
		return new GridFS(dbCollection.getDB(),collectionName).findOne(new ObjectId(_id));
	}


}
