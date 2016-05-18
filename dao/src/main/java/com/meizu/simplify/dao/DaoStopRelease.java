package com.meizu.simplify.dao;

import com.meizu.simplify.dao.datasource.DruidPoolFactory;
import com.meizu.simplify.ioc.IStopRelease;
import com.meizu.simplify.ioc.annotation.Bean;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月18日 下午6:24:07</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月18日 下午6:24:07</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class DaoStopRelease implements IStopRelease{

	@Override
	public void release() {
		DruidPoolFactory.closePool();//迁移到独立的dao模块中，这里通过Starup.stop();来触发TODO
//		是否整合到 DataSourceManager 的 AutoCloseable 的方法中，确认AutoCloseable的可靠性 TODO
	}

}
