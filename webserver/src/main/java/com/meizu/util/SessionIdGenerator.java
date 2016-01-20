package com.meizu.util;

import java.util.UUID;


/**
 * UUID生成ID方法
 * 
 */
public class SessionIdGenerator {
	public SessionIdGenerator() {
	}

	/**
	 * 获得一个SessionId
	 */
	public static String getSessionId() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
				+ s.substring(19, 23) + s.substring(24);
	}

}
