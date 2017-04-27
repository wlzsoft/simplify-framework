package vip.simplify.mongodb.dao;

import vip.simplify.mongodb.DataBase;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.InitBean;
import vip.simplify.ioc.annotation.Inject;
/**
 * <p>dao</p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月24日 上午9:19:10</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月24日 上午9:19:10</p>
 * @author <a href="mailto:meizu@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 0.1
 *
 */
@Bean
public class TestDao extends  MongoFileDao<DataBase>{
	
	@Inject
	private TestDataSource logDataSource;
	
	@InitBean
	public void init() {
		db=logDataSource.getDb();
	}
	
}
