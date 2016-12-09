package com.meizu.simplify.config.server.service;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;

import com.meizu.rpc.annotations.ServerBean;
import com.meizu.simplify.config.annotation.Config;
import com.meizu.simplify.config.api.entity.ConfigEntity;
import com.meizu.simplify.config.api.eums.ConfigTypeEnum;
import com.meizu.simplify.config.api.service.IConfigService;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.InitBean;
import com.meizu.simplify.zookeeper.ZookeeperConnectionManager;
import com.meizu.simplify.zookeeper.ZookeeperConnectionWatcher;
import com.meizu.simplify.zookeeper.ZookeeperExecute;

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
@ServerBean
public class ConfigService implements IConfigService{
	
	@Config("rootPath")
	private String rootPath = "/simplify-config/";
	
	private ZookeeperConnectionManager connectionManager = new ZookeeperConnectionManager();
	private ZookeeperExecute execute;
	
	@InitBean
	public void init() {
		try {
			connectionManager.connect("127.0.0.1:2181", new ZookeeperConnectionWatcher());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
        execute = new ZookeeperExecute(connectionManager);
	}
	
	@Override
	public ConfigEntity get(String groupId,String artifactId,String version,String environment,String folder,String name) {
		String appid = groupId+":"+artifactId+":"+version+"-"+environment;
		String value = execute.getData(rootPath+appid+"/"+folder+name, false, null);
		ConfigEntity config = new ConfigEntity();
		config.setAppid(appid);
		config.setName(folder+name);
		config.setType(ConfigTypeEnum.File);
		config.setValue(value);
		return config;
	}
	
	@Override
	public ConfigEntity get(String groupId,String artifactId,String version,String environment,String name) {
		return get(groupId, artifactId, version, environment, "", name);
	}

	@Override
	public Boolean save(ConfigEntity config) {
		try {
			execute.writeRecursion(rootPath+config.getAppid()+"/"+config.getName(), config.getValue());
		} catch (InterruptedException | KeeperException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
