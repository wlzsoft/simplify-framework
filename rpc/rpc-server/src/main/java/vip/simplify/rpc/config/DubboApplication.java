package vip.simplify.rpc.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.InitBean;
import vip.simplify.ioc.annotation.Inject;

/**
 * <p>dubbo应用配置></p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月15日 下午4:45:25</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年4月15日 下午4:45:25</p>
 * @author <a href="wanghaibin@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 3.0
 */
@Bean
public class DubboApplication  extends ApplicationConfig{
	
	private static final long serialVersionUID = -594938967258096101L;
	
	@Inject
	private DubboPropertiesConfig dubboProperties;

	private String applicationName;

	@InitBean
	public void init() {
		this.setName(dubboProperties.getProp().getString("dubbo.application.name"));
		this.setOwner(dubboProperties.getProp().getString("dubbo.application.owner"));
	}

	public DubboApplication() {
		super.setName(applicationName);
	}

}
