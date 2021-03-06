package vip.simplify.rpc.config;

import com.alibaba.dubbo.config.RegistryConfig;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.InitBean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.utils.StringUtil;

/**
 * <p>dubbo连接注册中心配置</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月15日 下午4:45:25</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年4月15日 下午4:45:25</p>
 * @author <a href="wanghaibin@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 3.0
 */
//@Bean(type=BeanTypeEnum.PROTOTYPE)
@Bean
public class DubboRegistry extends RegistryConfig{
	
	private static final long serialVersionUID = 5690790370979517282L;

	@Inject
	private DubboPropertiesConfig dubboProperties;

	private String address;

	@InitBean
	public void init() {
		address = dubboProperties.getProp().getString("dubbo.registry.address");
		this.setAddress(address);
		String filePath=dubboProperties.getProp().getString("dubbo.registry.file");
		if(StringUtil.isNotBlank(filePath)){
			this.setFile(System.getProperty("user.home")+filePath);
		}
		String group = dubboProperties.getProp().getString("dubbo.registry.group");
		if (StringUtil.isBlank(group)) {
			group = "dubbo";
		}
		this.setGroup(group);
	}
}
