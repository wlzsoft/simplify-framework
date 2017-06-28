package vip.simplify.mongodb.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.simplify.config.annotation.DymaicProperties;
import vip.simplify.config.annotation.Reload;
import vip.simplify.config.annotation.ReloadableResource;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.utils.PropertieUtil;
/**
 * <p>Mongodb配置文件解析</p>
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
@ReloadableResource(value="mongo.properties",prefix="mongo")
public class MongodbPropertiesConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongodbPropertiesConfig.class);
	
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
		LOGGER.info("加载mongodb配置信息文件成功。");
	}
}
