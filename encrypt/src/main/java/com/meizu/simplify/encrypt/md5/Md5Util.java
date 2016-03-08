package com.meizu.simplify.encrypt.md5;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.meizu.simplify.encrypt.base64.ByteHexUtil;

/**
 * <p><b>Title:</b><i>Md5加密工具类</i></p>
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
			return getMD5(src.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	public byte[] hashMd5Str (byte[] source) {
		return  ByteHexUtil.bytes2Hex(hashMd5(source)).getBytes();
	}
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

	/**
	 * 
	 * 方法用途:md5加密 <br>
	 * @param s 加密字符串
	 * @return md5加密后字符串
	 */
	private static String getMD5(byte[] source) {
		// 用来将字节转换成 16 进制表示的字符
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
//		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		try {
			
			byte[] tmp = Md5Util.hashMd5(source); // MD5 的计算结果是一个 128 位的长整数
			
			// 把密文转换成十六进制的字符串形式
			int j = tmp.length;
//			int j = 16; // 用字节表示就是 16 个字节
            char str[] = new char[j * 2];// 每个字节用 16 进制表示的话，使用两个字符
			
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < j; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			return new String(str); // 换后的结果转换为字符串
//			return new String(str).toLowerCase();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
