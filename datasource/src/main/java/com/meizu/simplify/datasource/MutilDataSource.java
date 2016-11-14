package com.meizu.simplify.datasource;

import javax.sql.DataSource;

import com.meizu.simplify.dao.datasource.IDataSource;
import com.meizu.simplify.datasource.route.HostRouteService;
import com.meizu.simplify.ioc.annotation.Bean;

/**
  * <p><b>Title:</b><i>多数据源实现</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月14日 上午11:14:17</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月14日 上午11:14:17</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class MutilDataSource implements IDataSource{

	@Override
	public DataSource value() {
		return HostRouteService.switchHost().value();
	}

	@Override
	public String print() {
		return HostRouteService.switchHost().print();
	}

	@Override
	public void init() {
		HostRouteService.switchHost().init();
		
	}

	@Override
	public void close() {
		HostRouteService.switchHost().close();
	}

}
