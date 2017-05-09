package vip.simplify.config.server.service;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;

import vip.simplify.rpc.annotations.ServerBean;
import vip.simplify.config.annotation.Config;
import vip.simplify.config.api.entity.ConfigAppEntity;
import vip.simplify.config.api.entity.ConfigEntity;
import vip.simplify.config.api.eums.ConfigTypeEnum;
import vip.simplify.config.api.service.IConfigService;
import vip.simplify.exception.UncheckedException;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.InitBean;
import vip.simplify.zookeeper.ZookeeperConnectionManager;
import vip.simplify.zookeeper.ZookeeperConnectionWatcher;
import vip.simplify.zookeeper.ZookeeperExecute;

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

	@Config("config.registry.address")
	private String registryAddress = "127.0.0.1:2181";
	
	private ZookeeperConnectionManager connectionManager = new ZookeeperConnectionManager();
	private ZookeeperExecute execute;
	
	@InitBean
	public void init() {
		try {
			connectionManager.connect(registryAddress, new ZookeeperConnectionWatcher());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
        execute = new ZookeeperExecute(connectionManager);
	}
	
	@Override
	public ConfigEntity get(ConfigAppEntity app, String folder, String name) {
		ConfigEntity config = new ConfigEntity();
		config.setApp(app);
		config.setName(folder+name);
		config.setType(ConfigTypeEnum.File);
		String value = execute.getData(rootPath+config.getAppid()+"/"+folder+name, false, null);
		config.setValue(value);
		return config;
	}
	
	@Override
	public ConfigEntity get(ConfigAppEntity app,String name) {
		return get(app, "", name);
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
	
	@Override
	public byte[] getAndSave(ConfigEntity config) {
		try {
			return execute.writeRecursionAndReturn(rootPath+config.getAppid()+"/"+config.getName(), config.getValue());
		} catch (InterruptedException | KeeperException e) {
			throw new UncheckedException(e);
		}
	}
}
