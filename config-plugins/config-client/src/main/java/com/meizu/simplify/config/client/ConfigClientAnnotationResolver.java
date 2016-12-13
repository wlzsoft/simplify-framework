package com.meizu.simplify.config.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import com.meizu.simplify.config.ConfigAnnotationResolver;
import com.meizu.simplify.config.annotation.Config;
import com.meizu.simplify.config.api.entity.ConfigAppEntity;
import com.meizu.simplify.config.api.entity.ConfigEntity;
import com.meizu.simplify.config.api.service.IConfigService;
import com.meizu.simplify.config.client.zookeeper.watch.ZookeeperNodeWatcher;
import com.meizu.simplify.exception.StartupErrorException;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.utils.PropertieUtil;
import com.meizu.simplify.utils.StringUtil;

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
	
	@Config("rootPath")
	private String rootPath = "/simplify-config/";
	
	@Resource
	private IConfigService configService;
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		InputStream inputStream= getClass().getClassLoader().getResourceAsStream("/META-INF/MANIFEST.MF"); 
		ConfigAppEntity app = new ConfigAppEntity();
		try {
			Manifest manifest = new Manifest(inputStream);
			Attributes attr = manifest.getMainAttributes();
			String groupId = attr.getValue("Implementation-Vendor-Id");
			if(StringUtil.isBlank(groupId)) {
				throw new StartupErrorException("加载/META-INF/MANIFEST.MF文件的groupId属性值为空，请检查文件中是否有groupId属性值");
			}
			String artifactId = attr.getValue("artifactId");
			if(StringUtil.isBlank(groupId)) {
				throw new StartupErrorException("加载/META-INF/MANIFEST.MF文件的artifactId属性值为空，请检查文件中是否有artifactId属性值");
			}
			String version = attr.getValue("Implementation-Version");
			if(StringUtil.isBlank(groupId)) {
				throw new StartupErrorException("加载/META-INF/MANIFEST.MF文件的version属性值为空，请检查文件中是否有version属性值");
			}
			//appid start  appid格式：com.meizu.simplify:demo:1.2.1-SNAPSHOT-dev
			app.setGroupId(groupId);
			app.setArtifactId(artifactId);
			app.setVersion(version);
			app.setEnvironment("dev");
			//appid end
		} catch (IOException e) {
			e.printStackTrace();
			throw new StartupErrorException("加载/META-INF/MANIFEST.MF文件出错，请检查是否文件格式错误");
		}
		
		Set<Entry<String, PropertieUtil>> propertiesSet = ConfigAnnotationResolver.propertiesMap.entrySet();
		for (Entry<String, PropertieUtil> entry : propertiesSet) {
			String configPath = entry.getKey();
			ConfigEntity entity = new ConfigEntity();
			entity.setApp(app);
			entity.setName(configPath);
			PropertieUtil propertiesUtil = entry.getValue();
			entity.setValue(propertiesUtil.get());
			configService.save(entity);
//			ConfigEntity  config = configService.get("com.meizu.simplify","demo","1.2.1-SNAPSHOT","dev",configPath);
//			System.out.println(config.getValue());
			//开启配置监控通知
			new ZookeeperNodeWatcher(rootPath+entity.getAppid()+"/"+entity.getName()).watch();
		}
	}
}
