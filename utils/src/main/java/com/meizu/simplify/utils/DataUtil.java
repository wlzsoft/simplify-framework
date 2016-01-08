package com.meizu.simplify.utils;

/**
 * <p><b>Title:</b><i>参数解析工具。</i></p>
 * <p>Desc: 提供各种对数据进行处理的方法,待优化和调整  TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月20日 下午12:44:59</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月20日 下午12:44:59</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class DataUtil {

	/**
	 * 构造方法，禁止实例化
	 */
	private DataUtil() {
	}
	

	/**
	 * 
	 * 方法用途: 将参数解析为int类型，为空时返回0<br>
	 * 操作步骤: TODO<br>
	 * @param param
	 * @return
	 */
	public static Integer parseInt(Object value) {
		return parseInt(value, 0);
	}

	/**
	 * 
	 * 方法用途: 将参数解析为int类型，为空时返回defaultValue<br>
	 * 操作步骤: TODO<br>
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static int parseInt(Object value, int defaultValue) {
		if (value == null) {
			return defaultValue;
		} 
        try {
        	return new Integer(value.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
	}

}
