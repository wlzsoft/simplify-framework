package vip.simplify.rpc.config;


import com.alibaba.dubbo.config.MonitorConfig;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.InitBean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.utils.StringUtil;

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
	
	
	@Inject
	private DubboPropertiesConfig dubboProperties;

	@InitBean
	public void init() {
		String monitorPro=dubboProperties.getProp().getString("dubbo.monitor.protocol");
		if (StringUtil.isNotBlank(monitorPro)) {
			setProtocol(monitorPro);
		}
		
	}
	
	public DubboMonitor() {
		
	}

}
