package com.meizu.simplify.config.client;

import java.util.List;

import com.meizu.simplify.config.api.entity.ConfigEntity;
import com.meizu.simplify.config.api.service.IConfigService;
import com.meizu.simplify.config.client.zookeeper.watch.ZookeeperNodeWatcher;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;

/**
  * <p><b>Title:</b><i>配置服务客户端模块启用解析</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 上午11:08:36</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 上午11:08:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@Init(InitTypeEnum.CONFIG_CLIENT)
public class ConfigClientAnnotationResolver implements IAnnotationResolver<Class<?>> {
	
	@Resource
	private IConfigService configService;
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		ConfigEntity  config = configService.get("myconfig.properties");
		System.out.println(config.getValue());
		//开启配置监控通知
//		new ZookeeperNodeWatcher("/node10000000003", "key1").watch();
	}
}
