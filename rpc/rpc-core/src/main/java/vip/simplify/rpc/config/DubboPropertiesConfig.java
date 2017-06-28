package vip.simplify.rpc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.config.annotation.DymaicProperties;
import vip.simplify.config.annotation.Reload;
import vip.simplify.config.annotation.ReloadableResource;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.utils.PropertieUtil;
/**
 * <p>dubbo配置文件解析</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月15日 下午4:45:25</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年4月15日 下午4:45:25</p>
 * @author <a href="wanghaibin@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 3.0
 *
 */
@Bean
@ReloadableResource(value="dubbo.properties",prefix="dubbo")
public class DubboPropertiesConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(DubboPropertiesConfig.class);

	/**
	 * dubbo.application.name
	 */
	private String applicationName;

	/**
	 * dubbo.registry.address
	 */
	private String registryAddress;

	/**
	 * dubbo.registry.file
	 */
	private String registryFile;

	/**
	 *dubbo.protocol.name
	 */
	private String protocolName;

	/**
	 * dubbo.monitor.protocol
	 */
	private String monitorProtocol;

	/**
	 *dubbo.service.loadbalance:目前这个配置只针对Server端使用
	 */
	private String serviceLoadbalance;

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getRegistryAddress() {
		return registryAddress;
	}

	public void setRegistryAddress(String registryAddress) {
		this.registryAddress = registryAddress;
	}

	public String getRegistryFile() {
		return registryFile;
	}

	public void setRegistryFile(String registryFile) {
		this.registryFile = registryFile;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getMonitorProtocol() {
		return monitorProtocol;
	}

	public void setMonitorProtocol(String monitorProtocol) {
		this.monitorProtocol = monitorProtocol;
	}

	public String getServiceLoadbalance() {
		return serviceLoadbalance;
	}

	public void setServiceLoadbalance(String serviceLoadbalance) {
		this.serviceLoadbalance = serviceLoadbalance;
	}

	public PropertieUtil getPropertieUtil() {
		return propertieUtil;
	}

	@DymaicProperties
	private PropertieUtil propertieUtil;
	
	public void setPropertieUtil(PropertieUtil propertieUtil) {
		this.propertieUtil = propertieUtil;
	}
	public PropertieUtil getProp() {
		return propertieUtil;
	}
	
	@Reload
	public void setBasenames(String... basenames) {
		LOGGER.info("加载dubbo配置信息文件成功。");
	}
}
