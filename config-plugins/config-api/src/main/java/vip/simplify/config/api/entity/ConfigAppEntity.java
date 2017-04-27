package vip.simplify.config.api.entity;

import java.io.Serializable;

/**
 * <p><b>Title:</b><i>TODO</i></p> <p>Desc: TODO</p> <p>source folder: {@docRoot} </p> <p>Copyright:Copyright(c)2014</p> <p>Company:meizu</p> <p>Create Date:2016年12月12日 上午11:25:21</p> <p>Modified By:luchuangye-</p> <p>Modified Date:2016年12月12日 上午11:25:21</p>
 * @author  <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version  Version 0.1
 */
public class ConfigAppEntity implements Serializable{
	private static final long serialVersionUID = 7375287643571340569L;
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
}