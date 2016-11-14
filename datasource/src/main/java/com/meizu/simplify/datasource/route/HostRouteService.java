package com.meizu.simplify.datasource.route;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.datasource.config.HostRouteConfigResolver;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月10日 上午11:55:41</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月10日 上午11:55:41</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class HostRouteService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HostRouteService.class);
	
	/**
	 * 
	 * 方法用途: 只读数据源路由选择<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static DynamicDataSource switchHost() {
		DynamicDataSource mutilDataSource = HostRouteConfigResolver.readDataSourceMap.get(new Random().nextInt(65536)%HostRouteConfigResolver.readDataSourceMap.size());
		LOGGER.info("所选只读数据源为："+mutilDataSource.getName());
		return mutilDataSource;
	}
}
