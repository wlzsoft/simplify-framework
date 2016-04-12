package com.meizu.simplify.utils;

import com.meizu.simplify.utils.serial.FstSerialize;
import com.meizu.simplify.utils.serial.ISerialize;

/**
 * <p><b>Title:</b><i>序列化工具类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 下午8:17:32</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 下午8:17:32</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class SerializeUtil {

	private static ISerialize<Object> serial = new FstSerialize<Object>();
	/**
	 * 
	 * 方法用途: 序列化<br>
	 * 操作步骤: TODO<br>
	 * @param obj
	 * @return
	 */
	public static byte[] serialize(Object obj) {
		return serial.serialize(obj);
	}

	/**
	 * 方法用途: 反序列化<br>
	 * 操作步骤: TODO<br>
	 * @param <T>
	 * @param sec
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  static <T> T unserialize(byte[] sec) {
		return (T) serial.unserialize(sec);
	}

	
}