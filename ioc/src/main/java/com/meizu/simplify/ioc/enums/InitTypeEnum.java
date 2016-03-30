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
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public enum InitTypeEnum {
	
	/**
	 * bean初始化
	 */
	BEAN(1),
	/**
	 * 配置文件实体初始化
	 */
	RELOADRESOURCE(2),
	/**
	 * 依赖注入配置文件属性
	 */
	CONFIG(3),
	/**
	 *依赖注入 
	 */
	IOC(4),
	/**
	 * 数据缓存初始化
	 */
	CACHE(5),
	/**
	 * controller 地址解析初始化
	 */
	CONTROL(6),
	TEMPLATE(7),
	/**
	 * 事务解析初始化
	 */
	TRANS(8),
	/**
	 * 页面缓存初始化
	 */
	WEBCACHE(9),
	/**
	 * websocket解析初始化
	 */
	WEBSOCKET(10),
	/**
	 * sql数据库表结构创建和修改解析初始化
	 */
	DBINIT(11);
	
	public final int value;
	InitTypeEnum(int value) {
		this.value = value;
	}
}
