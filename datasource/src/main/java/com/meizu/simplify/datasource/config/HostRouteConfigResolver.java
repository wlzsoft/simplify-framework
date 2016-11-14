package com.meizu.simplify.datasource.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.meizu.simplify.datasource.config.pojo.HostSwitch;
import com.meizu.simplify.datasource.config.pojo.MasterHost;
import com.meizu.simplify.datasource.config.pojo.SlaveHost;
import com.meizu.simplify.datasource.route.DynamicDataSource;
import com.meizu.simplify.datasource.route.HostRouteService;
import com.meizu.simplify.exception.StartupErrorException;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月10日 上午11:56:03</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月10日 上午11:56:03</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Init(InitTypeEnum.DATASOURCE)
public class HostRouteConfigResolver  implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(HostRouteService.class);
	public static final ConcurrentMap<Integer, DynamicDataSource> readDataSourceMap = new ConcurrentHashMap<>();
	public static DynamicDataSource writeDataSource = null;
	@Override
	public void resolve(List<Class<?>> resolveList) {
		Yaml yaml = new Yaml();
        URL url = HostRouteConfigResolverForYamlTest.class.getClassLoader().getResource("host-switch.yaml");
        if (url == null) {
        	throw new StartupErrorException("无法加载host-switch.yaml文件，文件不存在");
        }
        HostSwitch hostSwitch = null;
		try {
			hostSwitch = (HostSwitch) yaml.load(new FileInputStream(url.getFile()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			LOGGER.error("类转换异常：HostSwitch配置加载失败"+e);
			e.printStackTrace();
		}
		if(hostSwitch == null) {
			throw new StartupErrorException("HostSwitch配置加载失败,HostSwitch对象为空");
		}
		int readDataSourceIndex = 0;
		//主库配置
		List<MasterHost> masterHostList = hostSwitch.getMasterHostList();
		for (MasterHost masterHost : masterHostList) {
			Properties properties = new Properties();
			properties.setProperty("url", masterHost.getUrl());
			properties.setProperty("username", masterHost.getUserName());
			properties.setProperty("password", masterHost.getPassword());
			DynamicDataSource mutilDataSource = new DynamicDataSource(properties);
			mutilDataSource.setName(masterHost.getName());
			mutilDataSource.setType(0);
			writeDataSource = mutilDataSource;
			//从库配置
			List<SlaveHost> slaveHostList = masterHost.getSlaveHostList();
			for (SlaveHost slaveHost : slaveHostList) {
				Properties slaveProperties = new Properties();
				slaveProperties.setProperty("url", slaveHost.getUrl());
				slaveProperties.setProperty("username", slaveHost.getUserName());
				slaveProperties.setProperty("password", slaveHost.getPassword());
				DynamicDataSource slaveMutilDataSource = new DynamicDataSource(slaveProperties);
				slaveMutilDataSource.setName(slaveHost.getName());
				slaveMutilDataSource.setType(1);
				readDataSourceMap.put(readDataSourceIndex++, slaveMutilDataSource);
			}
		}
		LOGGER.info("数据源解析初始化成功");
	}
}
