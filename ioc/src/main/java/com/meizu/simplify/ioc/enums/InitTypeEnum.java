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
	 * bean创建成功后，执行ioc操作后会调用这个init方法
	 */
	INITBEAN(5),
	/**
	 * 数据缓存初始化
	 */
	CACHE(6),
	/**
	 * controller 地址解析初始化
	 */
	CONTROL(7),
	TEMPLATE(8),
	/**
	 * 事务解析初始化
	 */
	TRANS(9),
	/**
	 * 页面缓存初始化
	 */
	WEBCACHE(10),
	/**
	 * websocket解析初始化
	 */
	WEBSOCKET(11),
	/**
	 * sql数据库表结构创建和修改解析初始化
	 */
	DBINIT(12),
	/**
	 * sql数据库表结构创建和修改解析初始化
	 */
	DUBBOBEAN(13);
	
	public final int value;
	InitTypeEnum(int value) {
		this.value = value;
	}
}
