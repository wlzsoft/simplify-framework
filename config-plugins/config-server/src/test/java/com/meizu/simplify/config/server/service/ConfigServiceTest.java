package com.meizu.simplify.config.server.service;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.meizu.simplify.config.api.entity.ConfigEntity;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.test.SimplifyJUnit4ClassRunner;

/**
  * <p><b>Title:</b><i>配置服务配置信息操作接口</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年12月8日 下午3:54:53</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年12月8日 下午3:54:53</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@RunWith(SimplifyJUnit4ClassRunner.class)
public class ConfigServiceTest {
	
	@Resource
	private ConfigService configService;
	
	@Test
	public void testSave() {
		ConfigEntity entity = new ConfigEntity();
		entity.setGroupId("com.meizu.simplify");
		entity.setArtifactId("demo");
		entity.setVersion("1.2.1-SNAPSHOT");
		entity.setEnvironment("dev");
		entity.setName("redis-pool.properties");
		entity.setValue("#maxWaitMillis=10000");
		configService.save(entity);
	}
}
