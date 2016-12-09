package com.meizu.simplify.config.server.service;

import com.meizu.rpc.annotations.ServerBean;
import com.meizu.simplify.config.api.entity.ConfigEntity;
import com.meizu.simplify.config.api.eums.ConfigTypeEnum;
import com.meizu.simplify.config.api.service.IConfigService;
import com.meizu.simplify.ioc.annotation.Bean;

/**
  * <p><b>Title:</b><i>配置服务配置信息操作接口</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年12月8日 下午3:54:53</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年12月8日 下午3:54:53</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@ServerBean
public class ConfigService implements IConfigService{
	
	/**
	 * 
	 * 方法用途: 读取单个配置信息<br>
	 * 操作步骤: 包含配置属性和配置文件<br>
	 * @param name 用于唯一标识一个配置
	 * @return
	 */
	@Override
	public ConfigEntity get(String name) {
		ConfigEntity config = new ConfigEntity();
		config.setName("myconfig.properties");
		config.setType(ConfigTypeEnum.File);
		config.setValue("a=b");
		return config;
	}

	@Override
	public Boolean save(ConfigEntity config) {
		
		return null;
	}
}
