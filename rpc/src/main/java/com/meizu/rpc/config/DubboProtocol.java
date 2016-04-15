package com.meizu.rpc.config;


import com.alibaba.dubbo.config.ProtocolConfig;
import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.InitBean;
import com.meizu.simplify.ioc.annotation.Resource;

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
	
	// 服务协议
	private String name;
	// 服务端口
	private Integer port;
	
	
	@Resource
	private PropertiesConfig properties;

	@InitBean
	public void init() {
		name = properties.getProp().getString("system.dubbo.protocol.name");
		port = properties.getProp().getInteger("system.dubbo.protocol.port");
	}
	
	public DubboProtocol() {
		super.setName(name);
		super.setPort(port);
	}

}
