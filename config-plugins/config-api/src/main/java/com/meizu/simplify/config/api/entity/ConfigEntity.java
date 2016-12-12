package com.meizu.simplify.config.api.entity;

import java.io.Serializable;

import com.meizu.simplify.config.api.eums.ConfigTypeEnum;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年12月8日 下午4:01:32</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年12月8日 下午4:01:32</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class ConfigEntity implements Serializable{
	
	private static final long serialVersionUID = -7182027887620472022L;
	
	/**
	 * 对应gradle或maven的pom的groupId
	 */
	private String groupId;
	/**
	 * 对应gradle或maven的pom的artifactId
	 */
	private String artifactId;
	/**
	 * 对应gradle或maven的pom的version
	 */
	private String version;
	/**
	 * 环境：dev(开发环境：集成开发会需要),test-function(功能测试),test-performs(性能测试:宏基准测试，介基准测试，不包含微基准测试),product(生产环境)
	 */
	private String environment;
	/**
	 * app内唯一标识一个配置
	 */
	private String name;
	/**
	 * 配置类型
	 */
	private ConfigTypeEnum type;
	/**
	 * 具体配置值，配置属性值或是配置文件内容
	 */
	private String value;
	
	/**
	 * 
	 * 方法用途: 应用id，唯一标识一个应用<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public String getAppid() {
		return groupId+":"+artifactId+":"+version+"-"+environment;
	}
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getArtifactId() {
		return artifactId;
	}
	
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public ConfigTypeEnum getType() {
		return type;
	}
	public void setType(ConfigTypeEnum type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
