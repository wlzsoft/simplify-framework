package com.meizu.simplify.encrypt.md5;

import java.io.UnsupportedEncodingException;

import com.meizu.simplify.encrypt.ByteHexUtil;
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
public class Md5Util {

	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param value 整型
	 * @return
	 */
	public static String md5(int value) {
		return md5(String.valueOf((value)));
	}
	
	/**
	 * 符合RFC 1321标准的MD5编码
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param src 字符串
	 * @return
	 */
	public static String md5(String src) {
		if (src == null || src.trim().equals("")) {
			return null;
		}
		try {
//			return getMD5(hashMd5(src.getBytes("UTF-8")));
			return ByteHexUtil.bytes2Hex(Sign.hashSign(src.getBytes("UTF-8"),"MD5"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public byte[] hashMd5Str (byte[] source) {
		return  ByteHexUtil.bytes2Hex(Sign.hashSign(source,"MD5")).getBytes();
	}
	
	

	
	
	
	
}
