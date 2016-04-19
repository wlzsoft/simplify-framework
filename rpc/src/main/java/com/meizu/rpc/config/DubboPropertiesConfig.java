package com.meizu.rpc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.config.annotation.DymaicProperties;
import com.meizu.simplify.config.annotation.Reload;
import com.meizu.simplify.config.annotation.ReloadableResource;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.utils.PropertieUtil;
/**
 * <p>dubbo配置文件解析</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月15日 下午4:45:25</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年4月15日 下午4:45:25</p>
 * @author <a href="wanghaibin@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 3.0
 *
 */
@Bean
@ReloadableResource(value="properties/dubbo.properties",prefix="dubbo")
public class DubboPropertiesConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(DubboPropertiesConfig.class);
	
	@DymaicProperties
	private PropertieUtil propertieUtil;
	
	public void setPropertieUtil(PropertieUtil propertieUtil) {
		this.propertieUtil = propertieUtil;
	}
	public PropertieUtil getProp() {
		return propertieUtil;
	}
	
	@Reload
	public void setBasenames(String... basenames) {
		LOGGER.info("加载dubbo配置信息文件成功。");
	}
}
