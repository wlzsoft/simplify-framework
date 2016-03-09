package com.meizu.simplify.encrypt.md5;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.meizu.simplify.encrypt.ByteHexUtil;

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
			return ByteHexUtil.bytes2Hex(hashMd5(src.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public byte[] hashMd5Str (byte[] source) {
		return  ByteHexUtil.bytes2Hex(hashMd5(source)).getBytes();
	}
	
	

	
	
	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param source
	 * @return MD5 的计算结果是一个 128 位的长整数
	 */
	public static byte[] hashMd5(byte[] source) {
		try {
            // 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.reset();//是否需要重置 TODO
			// 使用指定的字节更新摘要
			digest.update(source);
			return digest.digest();//  获得密文:MD5 的计算结果是一个 128 位的长整数，
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
