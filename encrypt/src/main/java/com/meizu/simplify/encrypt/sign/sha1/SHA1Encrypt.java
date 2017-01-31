package com.meizu.simplify.encrypt.sign.sha1;

import com.meizu.simplify.encrypt.sign.Sign;

/**
 * <p><b>Title:</b><i>Md5加密工具类-依赖jdk的类的实现</i></p>
 * <p>Desc: 不同于MD5，支持 公钥后处理回传
 *          在1993年，安全散列算法（SHA）由美国国家标准和技术协会(NIST)提出，并作为联邦信息处理标准（FIPS PUB 180）公布；1995年又发布了一个修订版FIPS PUB 180-1，通常称之为SHA-1。SHA-1是基于MD4算法的，并且它的设计在很大程度上是模仿MD4的。现在已成为公认的最安全的散列算法之一，并被广泛使用</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 下午2:21:43</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 下午2:21:43</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class SHA1Encrypt {

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
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param source 字符串
	 * @return
	 */
	public static String sign(String source) {
		return Sign.sign(source, "SHA-1");
	}
	
	public static byte[] sign(byte[] source) {
		return Sign.hashSign(source, "SHA-1");
	}
	
	

	
	
	
	
}
