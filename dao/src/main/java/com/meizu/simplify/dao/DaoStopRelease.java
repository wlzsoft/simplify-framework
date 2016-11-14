package com.meizu.simplify.dao;

import com.meizu.simplify.dao.datasource.DataSourceManager;
import com.meizu.simplify.ioc.IStopRelease;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;

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

	@Resource
	private DataSourceManager dataSourceManager;
	
	@Override
	public void release() {
		dataSourceManager.close();
	}

}
