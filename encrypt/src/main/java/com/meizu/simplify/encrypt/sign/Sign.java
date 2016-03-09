package com.meizu.simplify.encrypt.sign;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
  * <p><b>Title:</b><i>数据签名</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 上午11:40:00</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月9日 上午11:40:00</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */

public class Sign {
	/**
	 * 
	 * 方法用途: 数据签名<br>
	 * 操作步骤: TODO<br>
	 * @param source
	 * @param name hash签名算法名称 [MD5,SHA-1]
	 * @return MD5 的计算结果是一个 128 位的长整数
	 */
	public static byte[] hashSign(byte[] source,String name) {
		try {
            // 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest digest = MessageDigest.getInstance(name);//MD5 or SHA-1
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
