package com.meizu.simplify.dao.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.config.annotation.Config;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.InitBean;


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
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceManager.class);
	@Config("system.debug")
	private Boolean debug;
	@InitBean
	public void init(){
		if(debug) {
			LOGGER.info("SQL数据源连接信息:"+DruidPoolFactory.getDataSourceInfo());
		}
	}
}

