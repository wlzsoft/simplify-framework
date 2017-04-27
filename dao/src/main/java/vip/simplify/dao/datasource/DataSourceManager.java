package vip.simplify.dao.datasource;

import vip.simplify.config.annotation.Config;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.InitBean;
import vip.simplify.ioc.annotation.Inject;


/**
 * <p><b>Title:</b><i>数据源生命周期管理</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月18日 下午4:12:00</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月18日 下午4:12:00</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class DataSourceManager {
	
	@Config("system.debug")
	private Boolean debug;
	
	@Inject
	private IDataSource dataSource;
	
	@InitBean
	public void init(){
		if(debug) {
			dataSource.print();
		}
		
	}
	
	public void closeDataSource() {
		dataSource.close();
	}
}

