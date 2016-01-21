package com.meizu.demo.system;

import java.util.Properties;

import com.meizu.simplify.utils.PropertieUtil;

/**
 * 同步单例配置
 * 
 */
public final class SystemConfig {

	static class SystemConfigHolder {
		static SystemConfig instance = new SystemConfig();
	}

	public static synchronized SystemConfig getInstance() {
		return SystemConfigHolder.instance;
	}

	private SystemConfig() {};

	private PropertieUtil config = new PropertieUtil("config.properties");

	private String appPath = "";

	public String getAppPath() {
		return appPath;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}

	public PropertieUtil getConfig() {
		return config;
	}

	public void setConfig(PropertieUtil config) {
		this.config = config;
	}
	
	

}
