package com.meizu.simplify.exception;

/**
 * <p><b>Title:</b><i>信息提示</i></p>
 * <p>Desc: 通过异常机制实现信息提示,支持i18n国际化</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年4月2日 下午4:56:35</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年4月2日 下午4:56:35</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class Message {
	
	/**
	 * 
	 * 方法用途: 获取配置文件中的提示信息<br>
	 * 操作步骤: TODO<br>
	 * @param code 信息编码
	 * @param values 信息变量，可以有多个 
	 * @return
	 */
	public static String get(String code, Object... values) {
		return null;
	}

	/**
	 * 
	 * 方法用途: 错误提示信息<br>
	 * 操作步骤: TODO<br>
	 * @param code 信息编码
	 * @param values  信息变量，可以有多个 
	 */
	public static void error(String code, Object... values) {
		throw new BaseException(get(code, values));
	}

	/**
	 * 
	 * 方法用途: 错误提示信息<br>
	 * 操作步骤: TODO<br>
	 * @param ex 异常对象
	 * @param code 信息编码
	 * @param values  信息变量，可以有多个 
	 */
	public static void error(Throwable ex, String code, Object... values) {
		throw new BaseException(get(code, values));
	}
	
	/**
	 * 
	 * 方法用途: 错误提示信息<br>
	 * 操作步骤: TODO<br>
	 * @param message 错误信息
	 */
	public static void error(String message) {
		throw new BaseException(message);
	}
}
