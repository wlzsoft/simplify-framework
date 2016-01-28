package com.meizu.dao.config;

import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.utils.PropertieUtil;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月28日 下午5:35:08</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月28日 下午5:35:08</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class PropertiesConfig {
	
	private static final String PROPERTIESFILE = "properties/config.properties";
	private static final PropertieUtil propertieUtils = new PropertieUtil(PROPERTIESFILE);
	public PropertieUtil getProperties() {
		return propertieUtils;
	}
}
