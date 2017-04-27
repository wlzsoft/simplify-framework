package vip.simplify.config.client;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import vip.simplify.config.ConfigAnnotationResolver;
import vip.simplify.config.ReloadResourceAnnotationResolver;
import vip.simplify.config.annotation.Config;
import vip.simplify.config.api.entity.ConfigAppEntity;
import vip.simplify.config.api.entity.ConfigEntity;
import vip.simplify.config.api.service.IConfigService;
import vip.simplify.config.client.zookeeper.watch.ZookeeperNodeWatcher;
import vip.simplify.exception.StartupErrorException;
import vip.simplify.exception.UncheckedException;
import vip.simplify.ioc.Startup;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Init;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.ioc.enums.InitTypeEnum;
import vip.simplify.ioc.resolver.IAnnotationResolver;
import vip.simplify.utils.PropertieUtil;
import vip.simplify.utils.StringUtil;

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
	
	@Inject
	private IConfigService configService;
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		ConfigAppEntity app = loadConfigMeta();
		//开始更新本地配置信息
		Set<Entry<String, PropertieUtil>> propertiesSet = ConfigAnnotationResolver.propertiesMap.entrySet();
		for (Entry<String, PropertieUtil> entry : propertiesSet) {
			String configPath = entry.getKey();
			PropertieUtil propertiesUtil = entry.getValue();
			updateConfig(app, configPath,propertiesUtil);
		}
	}

	/**
	 * 
	 * 方法用途: 加载配置文件元数据信息<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public ConfigAppEntity loadConfigMeta() {
		InputStream inputStream= getClass().getClassLoader().getResourceAsStream("/META-INF/MANIFEST.MF"); 
		ConfigAppEntity app = new ConfigAppEntity();
		try {
			Manifest manifest = new Manifest(inputStream);
			Attributes attr = manifest.getMainAttributes();
			String groupId = attr.getValue("Implementation-Vendor-Id");
			if(StringUtil.isBlank(groupId)) {
				throw new StartupErrorException("加载/META-INF/MANIFEST.MF文件的Implementation-Vendor-Id属性值为空，请检查文件中是否有Implementation-Vendor-Id属性值");
			}
			String artifactId = attr.getValue("artifactId");
			if(StringUtil.isBlank(artifactId)) {
				throw new StartupErrorException("加载/META-INF/MANIFEST.MF文件的artifactId属性值为空，请检查文件中是否有artifactId属性值");
			}
			String version = attr.getValue("Implementation-Version");
			if(StringUtil.isBlank(version)) {
				throw new StartupErrorException("加载/META-INF/MANIFEST.MF文件的Implementation-Version属性值为空，请检查文件中是否有Implementation-Version属性值");
			}
			//appid start  appid格式：vip.simplify:demo:0.0.9-SNAPSHOT-dev
			app.setGroupId(groupId);
			app.setArtifactId(artifactId);
			app.setVersion(version);
			app.setEnvironment("dev");
			//appid end
		} catch (IOException e) {
			e.printStackTrace();
			throw new StartupErrorException("加载/META-INF/MANIFEST.MF文件出错，请检查是否文件格式错误");
		}
		return app;
	}

	/**
	 * 
	 * 方法用途: 更新配置文件，更新配置依赖注入项，开启监控，向zookeeper发送连接状态<br>
	 * 操作步骤: TODO<br>
	 * @param app
	 * @param configPath
	 * @param propertiesUtil
	 */
	public void updateConfig(ConfigAppEntity app,String configPath,PropertieUtil propertiesUtil) {
		
		ConfigEntity entity = new ConfigEntity();
		entity.setApp(app);
		entity.setName(configPath);
		entity.setValue(propertiesUtil.get());
		byte[] value = configService.getAndSave(entity);
		if(value != null) {
			try {
				ByteArrayInputStream changeInputStream = new ByteArrayInputStream(value);
//					1.更新配置文件，
				String path = this.getClass().getClassLoader().getResource(configPath).getPath();
				FileOutputStream writer = new FileOutputStream(path);
				propertiesUtil.getProps().clear();
				propertiesUtil.getProps().load(changeInputStream);
				propertiesUtil.getProps().store(writer, "已从zookeeper更新");
//					2更新配置实体和属性值
				List<Class<?>> resolveList = new ArrayList<Class<?>>();
				resolveList.add(ReloadResourceAnnotationResolver.class);
				resolveList.add(ConfigAnnotationResolver.class);
				Startup.resolve(null,new Startup.AnnoCallback() {
						@Override
						public void invoke(IAnnotationResolver<Class<?>> ir, Class<?> beanObj) {
							ir.resolve(new ArrayList<>());
						}
				}, Startup.getAnnotationResolverList(resolveList));
			} catch (IOException e) {
				e.printStackTrace();
				throw new UncheckedException("从zookeeper更新本地配置失败");
			}
		}
		//开启配置监控通知
		String valueStr = null;
		try {
			valueStr = new String(value,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new ZookeeperNodeWatcher(rootPath+entity.getAppid()+"/"+entity.getName(),configPath).watch(valueStr);
	}
}
