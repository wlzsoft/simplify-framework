package vip.simplify.rpc.config;


import com.alibaba.dubbo.config.ProtocolConfig;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.InitBean;
import vip.simplify.ioc.annotation.Resource;

/**
 * <p>dubbo服务提供者协议</p>
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
public class DubboProtocol  extends ProtocolConfig{
	
	private static final long serialVersionUID = -1091315288158711793L;
	
	
	@Resource
	private DubboPropertiesConfig dubboProperties;

	@InitBean
	public void init() {
		this.setName(dubboProperties.getProp().getString("dubbo.protocol.name"));
		this.setPort(dubboProperties.getProp().getInteger("dubbo.protocol.port"));
//		this.setThreads(dubboProperties.getProp().getInteger("dubbo.protocol.threads"));
		this.setSerialization(dubboProperties.getProp().getString("dubbo.protocol.serialization"));
	}
	
	public DubboProtocol() {
		
	}

}
