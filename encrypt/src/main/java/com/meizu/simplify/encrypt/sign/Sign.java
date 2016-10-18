package com.meizu.simplify.encrypt.sign;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.meizu.simplify.encrypt.ByteHexUtil;

/**
  * <p><b>Title:</b><i>数据签名</i></p>
 * <p>Desc: 签名算法，又称呼摘要算法，哈希算法(hash算法) 
      数字签名:
          对某个数据块的签名,就是计算数据块的Hash值,然后使用私钥对hash值进行加密,结果就叫数字签名
          Hash值就是数据块的数字指纹
     签名验证:
          数据接收者拿到原始数据块与数字签名后,接受者也会使用相同的Hash算法得到Hash值,然后使用公钥解密
          得到原始的数据指纹,比较2个值,就可以判定数据块签名之后有没有被篡改
      Hash算法常见的有:
          MD5,SHA,哈希算法也类似摘要算法,是一个单向的散列函数,它解决在某一特定时间内,无法查找经Hash操作后生成特定
          特定HASH值的原信息块
          哈希算法输入一个长度不固定的信息块,返回一个固定长度的结果</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 上午11:40:00</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月9日 上午11:40:00</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
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
	
	/**
	 * 符合RFC 1321标准的MD5编码
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param src 字符串
	 * @return
	 */
	public static String sign(String src,String name) {
		if (src == null || src.trim().equals("")) {
			return null;
		}
		try {
			return ByteHexUtil.bytes2Hex(Sign.hashSign(src.getBytes("UTF-8"),name));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
