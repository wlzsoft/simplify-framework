package com.meizu.simplify.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * <p><b>Title:</b><i>byte和char互转工具</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月1日 下午6:43:22</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月1日 下午6:43:22</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class ByteAndCharConvertUtil {
	
	/**
	 * 方法用途: char转byte<br>
	 * 操作步骤: TODO<br>
	 * @param chars
	 * @return
	 */
	public static byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		return bb.array();

	}

	/**
	 * 
	 * 方法用途: byte转char<br>
	 * 操作步骤: TODO<br>
	 * @param bytes
	 * @return
	 */
	public static char[] getChars(byte[] bytes) {
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);

		return cb.array();
	}
}
