package vip.simplify.config.api.service;

import vip.simplify.rpc.annotations.ClientBean;
import vip.simplify.config.api.entity.ConfigAppEntity;
import vip.simplify.config.api.entity.ConfigEntity;
import vip.simplify.ioc.annotation.Bean;

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
//@Bean
//@ClientBean
public interface IConfigService {

	/**
	 * 
	 * 方法用途: 读取单个配置信息<br>
	 * 操作步骤: 包含配置属性和配置文件<br>
	 * @param app 
	 * @param name 用于app内唯一标识一个配置
	 * @return
	 */
	ConfigEntity get(ConfigAppEntity app, String name);

	/**
	 * 
	 * 方法用途: 读取单个配置信息<br>
	 * 操作步骤: 包含配置属性和配置文件<br>
	 * @param app 
	 * @param folder 前缀目录
	 * @param name 用于app内唯一标识一个配置
	 * @return
	 */
	ConfigEntity get(ConfigAppEntity app, String folder, String name);
	
	/**
	 * 
	 * 方法用途: 保存单个配置文件<br>
	 * 操作步骤: appid格式：[groupId:artifactId:version-environment]  vip.simplify:demo:0.0.9-SNAPSHOT-dev<br>
	 * @param config
	 * @return
	 */
	public Boolean save(ConfigEntity config);

	/**
	 * 
	 * 方法用途: 保存单个配置文件,如果服务端配置信息存在，那么直接返回，否则返回null<br>
	 * 操作步骤: TODO<br>
	 * @param config
	 * @return
	 */
	public byte[] getAndSave(ConfigEntity config);
}
