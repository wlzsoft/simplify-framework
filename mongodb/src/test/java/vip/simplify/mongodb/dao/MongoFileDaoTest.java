package vip.simplify.mongodb.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;

import vip.simplify.mongodb.DataBase;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.Startup;

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

	@BeforeClass  
    public static void setUp() {  
		Startup.start();  
    } 
	
	
	@Test
	public void testSave() {
		DataBase dataBase = new DataBase();
		dataBase.setToken("FDFJDKFJDIFDNFDF22222");
		dataBase.setContentType("text/plain");
		try {
			DefautlMongoFileDao dao = BeanFactory.getBean(DefautlMongoFileDao.class);
			InputStream streamToUploadFrom =new  FileInputStream(new File("E:/daily.log"));
			String fileId=dao.save(streamToUploadFrom, "ss55555", dataBase);
			System.out.println(fileId);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	@Test
	public void testDown() {
		DataBase dataBase = new DataBase();
		dataBase.setToken("fdfdfdfdf");
		try {
			DefautlMongoFileDao dao = BeanFactory.getBean(DefautlMongoFileDao.class);
			byte[] tt=dao.downloadStreamByName("ss55555");
			System.out.println(tt.length);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	@Test
	public void findById() {
//		try {
//			DefaultMongoDao systemLogDao = BeanFactory.getBean(DefaultMongoDao.class);
//			GridFSFile file=systemLogDao.findById("574508dd3ea3d40c70940ece");
//			if(null!=file){
//				System.out.println(file.getFilename());
//			}else{
//				System.out.println("空");
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//		}
	}
	@Test
	public void findByName() {
//		try {
//			DefaultMongoDao systemLogDao = BeanFactory.getBean(DefaultMongoDao.class);
//			List<GridFSFile> fileList = systemLogDao.findByName("测试名称");
//			for(GridFSFile gf:fileList){
//				System.out.println(gf.getObjectId()+gf.getFilename());
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//		}
	}

	@Test
	public void downLoadByToke() {
		try {
			DefautlMongoFileDao dao = BeanFactory.getBean(DefautlMongoFileDao.class);
			byte[] tt=dao.downloadStreamByToke("FDFJDKFJDIFDNFDF");
			System.out.println(tt.length);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
