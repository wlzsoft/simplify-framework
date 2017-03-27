package com.meizu.simplify.utils;

import com.meizu.simplify.exception.UncheckedException;

/**
 * <p><b>Title:</b><i>断言工具类，该工具类常用于对参数进行校验</i></p>
 * <p>Desc: 注意：这个工具类不同于Message工具类，Message是用于web项目中的信息提示，而AssertUtil不特定于项目类型，比如rpc服务端中也可使用,AssertUtil只用于数据校验</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 下午2:36:24</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 下午2:36:24</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class AssertUtil {

	/**
	 * 
	 * 方法用途: 断言表达式为true<br>
	 * 操作步骤: TODO<br>
	 * @param expression 表达式
	 * @param message 错误信息
	 */
	public static void isTrue(Boolean expression, String message) {
		if (!expression) {
			throw new UncheckedException(message);
		}
	}

	/**
	 * 
	 * 方法用途: 断言表达式为true<br>
	 * 操作步骤: TODO<br>
	 * @param expression 表达式
	 */
	public static void isTrue(Boolean expression) {
		isTrue(expression, "断言失败: 表达式必须是true");
	}

	/**
	 * 
	 * 方法用途: 断言对象为null<br>
	 * 操作步骤: TODO<br>
	 * @param object 待判断的对象
	 * @param message 错误信息
	 */
	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new UncheckedException(message);
		}
	}

	/**
	 * 
	 * 方法用途: 断言对象为null<br>
	 * 操作步骤: TODO<br>
	 * @param object 待判断的对象
	 */
	public static void isNull(Object object) {
		isNull(object, "断言失败: 对象必须是null");
	}

	/**
	 * 
	 * 方法用途: 断言对象不为null<br>
	 * 操作步骤: TODO<br>
	 * @param object 待判断的对象
	 * @param message 错误信息
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new UncheckedException(message);
		}
	}

	/**
	 * 
	 * 方法用途: 断言对象不为null<br>
	 * 操作步骤: TODO<br>
	 * @param object 待判断的对象
	 */
	public static void notNull(Object object) {
		notNull(object, "断言失败: 对象不能是null");
	}

	/**
	 * 
	 * 方法用途: 断言字符串为空<br>
	 * 操作步骤: TODO<br>
	 * @param text 字符串
	 * @param message 错误信息
	 */
	public static void isEmpty(String text, String message) {
		if (StringUtil.isNotEmpty(text)) {
			throw new UncheckedException(message);
		}
	}

	/**
	 * 
	 * 方法用途: 断言字符串为空<br>
	 * 操作步骤: TODO<br>
	 * @param text 字符串
	 */
	public static void isEmpty(String text) {
		isEmpty(text, "断言失败: 字符串必须是空");
	}

	/**
	 * 
	 * 方法用途: 断言字符串不为空<br>
	 * 操作步骤: TODO<br>
	 * @param text 字符串
	 * @param message 错误信息
	 */
	public static void notEmpty(String text, String message) {
		if (StringUtil.isEmpty(text)) {
			throw new UncheckedException(message);
		}
	}

	/**
	 * 断言字符串不为空。
	 * 
	 * @param text
	 *            字符串
	 */
	public static void notEmpty(String text) {
		notEmpty(text, "断言失败: 字符串不能是空");
	}

	/**
	 * 断言字符串为空字符串。
	 * 
	 * @param text
	 *            字符串
	 * @param message
	 *            错误信息
	 */
	public static void isBlank(String text, String message) {
		if (StringUtil.isNotBlank(text)) {
			throw new UncheckedException(message);
		}
	}

	/**
	 * 断言字符串为空字符串。
	 * 
	 * @param text
	 *            字符串
	 */
	public static void isBlank(String text) {
		isBlank(text, "断言失败: 字符串必须是空字符串");
	}

	/**
	 * 断言字符串不为空字符串。
	 * 
	 * @param text
	 *            字符串
	 * @param message
	 *            错误信息
	 */
	public static void notBlank(String text, String message) {
		if (StringUtil.isBlank(text)) {
			throw new UncheckedException(message);
		}
	}

	/**
	 * 断言字符串不为空字符串。
	 * 
	 * @param text
	 *            字符串
	 */
	public static void notBlank(String text) {
		notBlank(text, "断言失败: 字符串不能是空字符串");
	}

}
