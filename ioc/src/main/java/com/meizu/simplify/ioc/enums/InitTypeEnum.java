package com.meizu.simplify.ioc.enums;
/**
  * <p><b>Title:</b><i>启动顺序枚举</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月7日 下午6:22:39</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月7日 下午6:22:39</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public enum InitTypeEnum {
	/**
	 * 织入初始化
	 */
	WEAVING(10),
	/**
	 * 数据源初始化
	 */
	DATASOURCE(15),
	/**
	 * bean初始化
	 */
	BEAN(20),
	/**
	 * 配置文件实体初始化
	 */
	RELOAD_RESOURCE(30),
	/**
	 * 依赖注入配置文件属性
	 */
	CONFIG(40),
	/**
	 * 配置服务中心客户端初始化
	 */
	CONFIG_CLIENT(41),
	/**
	 *依赖注入 
	 */
	IOC(50),
	/**
	 * bean创建成功后，执行ioc操作后会调用这个init方法
	 */
	INIT_BEAN(60),
	/**
	 * 数据缓存初始化
	 */
	CACHE(70),
	/**
	 * controller 地址解析初始化
	 */
	CONTROL(80),
	/**
	 * 模版引擎初始化
	 */
	TEMPLATE(90),
	/**
	 * 事务解析初始化
	 */
	TRANS(100),
	/**
	 * 页面缓存初始化
	 */
	WEB_CACHE(110),
	/**
	 * websocket解析初始化
	 */
	WEB_SOCKET(120),
	/**
	 * sql数据库表结构创建和修改解析初始化
	 */
	DB_INIT(130),
	/**
	 * 远程方法调用，远程bean创建初始化
	 */
	SERVER_BEAN(140),
	/**
	 * 代码生成 
	 */
	METHOD_GEN(150);
	
	public final int value;
	InitTypeEnum(int value) {
		this.value = value;
	}
}
