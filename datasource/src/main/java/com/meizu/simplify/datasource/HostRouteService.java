package com.meizu.simplify.datasource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.meizu.simplify.dao.datasource.ConnectionFactory;
import com.meizu.simplify.dao.datasource.DataSourceManager;
import com.meizu.simplify.datasource.config.HostRouteConfigResolverForYamlTest;
import com.meizu.simplify.datasource.config.pojo.HostSwitch;
import com.meizu.simplify.datasource.config.pojo.MasterHost;
import com.meizu.simplify.datasource.config.pojo.SlaveHost;
import com.meizu.simplify.exception.StartupErrorException;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月10日 上午11:55:41</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月10日 上午11:55:41</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class HostRouteService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HostRouteService.class);
	
	public void switchHost() {
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
		//主库配置
		List<MasterHost> masterHostList = hostSwitch.getMasterHostList();
		for (MasterHost masterHost : masterHostList) {
			Properties properties = new Properties();
			properties.setProperty("url", masterHost.getUrl());
			properties.setProperty("username", masterHost.getUserName());
			properties.setProperty("password", masterHost.getPassword());
			MutilDataSource mutilDataSource = new MutilDataSource(properties);
			mutilDataSource.setName(masterHost.getName());
			
			Connection conn = ConnectionFactory.getConnection(mutilDataSource.value());
			try {
				boolean isAutoCommit = conn.getAutoCommit();
				if(isAutoCommit) {
//			insert
				} else {
//			select
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				System.out.println("测试啊："+conn.getClientInfo());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			//从库配置
			List<SlaveHost> slaveHostList = masterHost.getSlaveHostList();
			for (SlaveHost slaveHost : slaveHostList) {
				Properties slaveProperties = new Properties();
				slaveProperties.setProperty("url", slaveHost.getUrl());
				slaveProperties.setProperty("username", slaveHost.getUserName());
				slaveProperties.setProperty("password", slaveHost.getPassword());
				MutilDataSource slaveMutilDataSource = new MutilDataSource(slaveProperties);
				slaveMutilDataSource.setName(slaveHost.getName());
				conn = ConnectionFactory.getConnection(slaveMutilDataSource.value());
				try {
					boolean isAutoCommit = conn.getAutoCommit();
					if(isAutoCommit) {
//				insert
					} else {
//				select
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					System.out.println("测试啊："+conn.getClientInfo());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
