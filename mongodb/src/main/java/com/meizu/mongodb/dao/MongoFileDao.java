package com.meizu.mongodb.dao;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.Block;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.Filters;

/**
 * <p>文件操作</p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月23日 上午11:41:35</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月23日 上午11:41:35</p>
 * @author <a href="mailto:wanghaibin@meizu.com" title="邮箱地址">wnaghaibin</a>
 * @version Version 3.0
 * @param <T>  DataBase
 */
public class MongoFileDao<T>{
	
	public MongoDatabase db;
	private String selfName;
	
	public MongoFileDao() {
		selfName = getEntityClass().getSimpleName();
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getEntityClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	 * 方法用途: 插入文件<br>
	 * 操作步骤: TODO<br>
	 * @param name
	 * @param t
	 * @return String 主键Id
	 */
	public String save(InputStream io, String name, T t) {
		GridFSBucket gridFSBucket = GridFSBuckets.create(db,selfName);
		GridFSUploadOptions options = new GridFSUploadOptions().metadata(buildDocument(t));
		ObjectId fileId = gridFSBucket.uploadFromStream(name, io, options);
		return fileId.toString();
	}

	/**
	 * 方法用途: Id查询<br>
	 * 操作步骤: TODO<br>
	 * @param id ObjectId
	 * @return
	 */
	public GridFSFile findById(String objectId) {
		GridFSBucket gridFSBucket = GridFSBuckets.create(db,selfName);
		GridFSFile fileInfo = gridFSBucket.find(new BsonDocument("_id", new BsonObjectId(new ObjectId(objectId)))).first();
		return fileInfo;
	}
	
	/**
	 * 方法用途: 文件名称查询<br>
	 * 操作步骤: TODO<br>
	 * @param fileName
	 * @return
	 */
	public List<GridFSFile> findByName(String fileName) {
		GridFSBucket gridFSBucket = GridFSBuckets.create(db,selfName);
		List<GridFSFile> fileList = new ArrayList<GridFSFile>();
		gridFSBucket.find(Filters.eq("filename", fileName)).forEach(new Block<GridFSFile>() {
			@Override
			public void apply(final GridFSFile gridFSFile) {
				fileList.add(gridFSFile);
			}
		});
		return fileList;
	}
	
	/**
	 * 
	 * 方法用途: 下载文件<br>
	 * 操作步骤: TODO<br>
	 * @param name文件名称
	 * @return
	 */
	public byte[] downloadStreamByName(String name) {
		try {
			GridFSBucket gridFSBucket = GridFSBuckets.create(db,selfName);
			GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStreamByName(name);
			int fileLength = (int) downloadStream.getGridFSFile().getLength();
			byte[] bytesToWriteTo = new byte[fileLength];
			downloadStream.read(bytesToWriteTo);
			downloadStream.close();
			return bytesToWriteTo;
		} catch (Exception e) {
			return new byte[0];
		}
	}
	
	/**
	 * 方法用途:删除<br>
	 * 操作步骤: TODO<br>
	 * @param objectId
	 * @return
	 */
	public boolean delById(String objectId){
		GridFSBucket gridFSBucket = GridFSBuckets.create(db,selfName);
		gridFSBucket.delete(new ObjectId(objectId));
		return true;
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
	
}
