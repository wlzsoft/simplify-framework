package vip.simplify.config.info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.config.annotation.DymaicProperties;
import vip.simplify.config.annotation.Reload;
import vip.simplify.config.annotation.ReloadableResource;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.utils.PropertieUtil;


/**
 * <p><b>Title:</b><i>配置提示信息</i></p>
 * <p>Desc: 支持热加载</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月23日 上午11:10:54</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月23日 上午11:10:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@ReloadableResource(value="properties/message.properties",prefix="")
public class MessageConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageConfig.class);
	
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
		LOGGER.info("加载配置信息文件成功。");
	}
}
