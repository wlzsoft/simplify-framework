package com.meizu.simplify.encrypt.sign.md5;

import java.nio.charset.Charset;

import com.meizu.simplify.encrypt.Base16;
import com.meizu.simplify.encrypt.sign.Sign;

/**
 * <p><b>Title:</b><i>Md5加密工具类-依赖jdk的类的实现</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 下午2:21:43</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 下午2:21:43</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class MD5Encrypt {
	final static Charset CHARSET = Charset.forName("UTF-8");
	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param source 整型
	 * @return
	 */
	public static String sign(int source) {
		return sign(String.valueOf((source)));
	}
	
	/**
	 * 符合RFC 1321标准的MD5编码
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param source 字符串
	 * @return
	 */
	public static String sign(String source) {
		return Sign.sign(source, "MD5");
	}
	
	public static byte[] sign(byte[] source) {
		return Sign.sign(source, "MD5");
	}
	
	
	public static String hashMd5(String plaintext) {
		if (plaintext == null || plaintext.isEmpty())
			return "";
		byte[] data = plaintext.getBytes(CHARSET);
		byte[] hash = Sign.hashSign(data,"MD5");
		return Base16.encode16ToString(hash);
	}
	
	
	
	
}
