package com.meizu.simplify.enums;

/**
 * 
 * <p><b>Title:</b><i>终端类型</i></p>
 * <p>Desc: 标注来源于哪个服务</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2014年12月30日 下午5:29:22</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2014年12月30日 下午5:29:22</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 3.0
 *
 */
public enum Terminal {
	/**
	 * 后台
	 */
	SERVER,
	/**
	 * 前台
	 */
	CLIENT,
	/**
	 * 微信
	 */
	WEIXIN,
	/**
	 * 移动终端，无法确认是属于哪类终端时选择，比如诺基亚
	 */
	MOBILE,
	/**
	 * 苹果手机
	 */
	IPHONE,
	/**
	 * 苹果平板
	 */
	IPAD,
	/**
	 * android手机
	 */
	ANDROID,
	/**
	 * android 平板
	 */
	APAD,
	/**
	 * 接口
	 */
	INTERFACES;
}
