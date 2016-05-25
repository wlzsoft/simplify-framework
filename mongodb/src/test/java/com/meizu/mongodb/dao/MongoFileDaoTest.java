package com.meizu.mongodb.dao;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.meizu.mongodb.DataBase;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.Startup;
import com.mongodb.Block;
import com.mongodb.client.gridfs.model.GridFSFile;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月24日 下午5:48:21</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月24日 下午5:48:21</p>
 * @author <a href="mailto:meizu@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 0.1
 *
 */
public class MongoFileDaoTest {

	@Before  
    public void setUp() throws Exception {  
		Startup.start();  
    } 
	
	
	@Test
	public void testSave() {
		DataBase dataBase = new DataBase();
		dataBase.setToken("fdfdfdfdf");
		dataBase.setContentType("text/plain");
		try {
			TestDao systemLogDao = BeanFactory.getBean(TestDao.class);
			InputStream streamToUploadFrom = new FileInputStream(new File("E:/daily.log"));
			String l=systemLogDao.save(streamToUploadFrom, "测试名称", dataBase);
			System.out.println(l.toString());
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
	}
	@Test
	public void testDown() {
		DataBase dataBase = new DataBase();
		dataBase.setToken("fdfdfdfdf");
		try {
			TestDao systemLogDao = BeanFactory.getBean(TestDao.class);
			byte[] tt=systemLogDao.downloadStreamByName("测试名称");
			System.out.println(tt.length);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	@Test
	public void findById() {
		try {
			TestDao systemLogDao = BeanFactory.getBean(TestDao.class);
			GridFSFile file=systemLogDao.findById("574508dd3ea3d40c70940ece");
			if(null!=file){
				System.out.println(file.getFilename());
			}else{
				System.out.println("空");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	@Test
	public void findByName() {
		try {
			TestDao systemLogDao = BeanFactory.getBean(TestDao.class);
			List<GridFSFile> fileList = systemLogDao.findByName("测试名称");
			for(GridFSFile gf:fileList){
				System.out.println(gf.getObjectId()+gf.getFilename());
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
