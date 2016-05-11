package com.meizu.rpc.config;


import com.alibaba.dubbo.config.MonitorConfig;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.InitBean;
import com.meizu.simplify.ioc.annotation.Resource;

/**
 * <p>dubbo监控</p>
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
public class DubboMonitor  extends MonitorConfig{
	
	private static final long serialVersionUID = -1091315288158711793L;
	
	
	@Resource
	private DubboPropertiesConfig dubboProperties;

	@InitBean
	public void init() {
		setProtocol(dubboProperties.getProp().getString("dubbo.monitor.protocol"));
	}
	
	public DubboMonitor() {
		
	}

}
