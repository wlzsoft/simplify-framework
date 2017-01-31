package com.meizu.simplify.encrypt.symmetriccryptography.des;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import com.meizu.simplify.encrypt.symmetriccryptography.SymmetricEncrypt;

/**
 * 
 * <p><b>Title:</b><i>DES加密算法</i></p>
 * <p>Desc: 对称加密算法有：DES(Data Encryption Standard（数据加密标准）)，DESede(TripleDES)、AES、Blowfish、RC2、RC4(ARCFOUR) 等等算法,这些支持的密钥长度都不同，其中 Blowfish是目前性能最好的,也是最安全的
 *          DES算法:1.对称加密算法的加密分割模式(字符串分割方式,也有叫工作模式的)：支持ECB(DES-ECB 电子密码本模式),CBC(DES-CBC 加密分组链接模式),CTR(DES-CTR),OFB(DES-OFB 输出反馈模式),CFB(DES-CFB 加密反馈模式)等等
 *                  2.对称加密算法的填充模式：PKCS5Padding,PKCS7Padding,ZeroPadding,ISO10126,ansix923,NoPadding(加密数据长度必须是8的倍数)，等等
 *                  jdk的DES算法默认使用的ECB模式，它不需要向量，DES算法支持大部分的加密模式和填充模式
 *                  其中3DES只支持ECB和CBC两种模式
                    3.初始化向量（可选）：
                       CBC模式(工作模式)：CBC模式，必须有向量值，否则加密的数据不固定，导致加密的值无法解密(解密异常java.security.InvalidKeyException: Parameters missing)
                       IV(向量)：iv通过随机数（或伪随机）机制产生是一种比较常见的方法。iv的作用主要是用于产生密文的第一个block，为了提高安全性，
                                                                              使加密的数据更难被破解(用于避免明文中的重复字符在加密后还是重复的，减少加密后数据的规律)，iv通过随机方式产生是一种十分简便有效，
                                                                              原理就是在key基础上增加一个随机的混合字符串

注意：1.网络通讯中IPsec中采用了DES-CBC作为缺省的加密模式，其使用的IV是通讯包的时间戳
      2.DES也是一个分组加密算法
      3.保密性依赖于密钥
特点：分组比较短、密钥太短、密码生命周期短、运算速度快，但较AES慢,本地数据，安全级别低，适合大量数据
加密选型注意：DES现在已经不视为一种安全的加密算法，虽然除了用穷举搜索法对DES算法进行攻击外，还没有发现更有效的办法，但因为它使用的56位秘钥过短，而56位长的密钥的穷举空间为256,以现代计算能力，24小时内即可能被破解。也有一些分析报告提出了该算法的理论上的弱点，虽然实际情况未必出现。该标准在最近已经被高级加密标准（AES）所取代。
加密算法选型场景：被加密数据具有很短的时效性，不固定性，一次性数据，可以适用这个算法，不会有安全的风险
</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 上午11:41:13</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 上午11:41:13</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class DESEncrypt {
	
	

	//------------------------具体使用DES加密算法的ECB模式
	
	/**
	 * 
	 * 方法用途: 加密并base64编码ECB模式<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度8个字节，也就是64位
	 * @param charset
	 * @param algorithmPadding 设置填充模式
	 * @return
	 */
	public static String encryptAndBase64ECB(String data, String key, String charset,String algorithmPadding) {
		return SymmetricEncrypt.encryptAndBase64(data, key, charset, false, "DES", "ECB", algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: base64解码并解密ECB模式<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度至少8个字节以上，也就是64位或以上
	 * @param charset
	 * @param algorithmPadding 设置填充模式
	 * @return
	 */
	public static String base64AndDecryptECB(String data, String key, String charset,String algorithmPadding) {
		return SymmetricEncrypt.base64AndDecrypt(data, key, charset, false, "DES", "ECB", algorithmPadding);
	}

	public static String encryptToHexECB(String data, String key, String charset,String algorithmPadding) {
		return SymmetricEncrypt.encryptToHex(data, key, charset, false, "DES", "ECB", algorithmPadding);
	}

	public static String hexToDecryptECB(String data, String key, String charset,String algorithmPadding) {
		return SymmetricEncrypt.hexToDecrypt(data, key, charset, false, "DES", "ECB", algorithmPadding);
	}
	
	//------------------------具体使用DES加密算法的CBC模式
	
	/**
	 * 
	 * 方法用途: 加密并base64编码CBC模式<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度至少8个字节以上，也就是64位或以上
	 * @param charset
	 * @param algorithmPadding 设置填充模式
	 * @return
	 */
	public static String encryptAndBase64CBC(String data, String key, String charset,String algorithmPadding) {
		return SymmetricEncrypt.encryptAndBase64(data, key, charset, false, "DES", "CBC", algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: base64解码并解密CBC模式<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度至少8个字节以上，也就是64位或以上
	 * @param charset
	 * @param algorithmPadding 设置填充模式
	 * @return
	 */
	public static String base64AndDecryptCBC(String data, String key, String charset,String algorithmPadding) {
		return SymmetricEncrypt.base64AndDecrypt(data, key, charset, false, "DES", "CBC", algorithmPadding);
	}

	public static String encryptToHexCBC(String data, String key, String charset,String algorithmPadding) {
		return SymmetricEncrypt.encryptToHex(data, key, charset, false, "DES", "CBC", algorithmPadding);
	}

	public static String hexToDecryptCBC(String data, String key, String charset,String algorithmPadding) {
		return SymmetricEncrypt.hexToDecrypt(data, key, charset, false, "DES", "CBC", algorithmPadding);
	}
	
	//------------------------具体DES算法相关
	
	/**
	 * 
	 * 方法用途: 加密并base64编码<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度至少8个字节以上，也就是64位或以上
	 * @param charset 字符编码
	 * @param iv 向量
	 * @param algorithmMode 分割模式
	 * @param algorithmPadding 填充模式
	 * @return
	 */
	public static String encryptAndBase64(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
		return SymmetricEncrypt.encryptAndBase64(data, key, charset, iv, "DES", algorithmMode, algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: base64解码并解密<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度至少8个字节以上，也就是64位或以上
	 * @param charset 字符编码
	 * @param iv 向量
	 * @param algorithmMode 分割模式
	 * @param algorithmPadding 填充模式
	 * @return
	 */
	public static String base64AndDecrypt(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
		return SymmetricEncrypt.base64AndDecrypt(data, key, charset, iv, "DES", algorithmMode, algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: 加密并转换成16进制数据<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度至少8个字节以上，也就是64位或以上
	 * @param charset 字符编码
	 * @param iv 向量
	 * @param algorithmMode 分割模式
	 * @param algorithmPadding 填充模式
	 * @return
	 */
	public static String encryptToHex(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
		return SymmetricEncrypt.encryptToHex(data, key, charset, iv, "DES", algorithmMode, algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: 16进制数据解密<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度至少8个字节以上，也就是64位或以上
	 * @param charset 字符编码
	 * @param iv 向量
	 * @param algorithmMode 分割模式
	 * @param algorithmPadding 填充模式
	 * @return
	 */
	public static String hexToDecrypt(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
		return SymmetricEncrypt.hexToDecrypt(data, key, charset, iv, "DES", algorithmMode, algorithmPadding);
	}
	
	//-----------------------具体使用DES加密算法的ECB模式PKCS5Padding填充模式
	
	/**
	 * 
	 * 方法用途: 加密并base64编码ECB模式，默认填充方式PKCS5Padding<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度至少8个字节以上，也就是64位或以上
	 * @param charset
	 * @return
	 */
	public static String encryptAndBase64ECB(String data, String key, String charset) {
		return encryptAndBase64(data, key, charset,false, "ECB", "PKCS5Padding");
	}

	/**
	 * 
	 * 方法用途: base64解码并解密ECB模式，默认填充方式PKCS5Padding<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度至少8个字节以上，也就是64位或以上
	 * @param charset
	 * @return
	 */
	public static String base64AndDecryptECB(String data, String key, String charset) {
		return base64AndDecrypt(data, key, charset,false,  "ECB", "PKCS5Padding");
	}

	public static String encryptToHexECB(String data, String key, String charset) {
		return encryptToHex(data, key, charset,false,  "ECB", "PKCS5Padding");
	}

	public static String hexToDecryptECB(String data, String key, String charset) {
		return hexToDecrypt(data, key, charset,false, "ECB", "PKCS5Padding");
	}
	
	/**
	 * 
	 * 方法用途: 加密,ECB模式，默认填充方式PKCS5Padding<br>
	 * 操作步骤: 暂未缓存Cipher<br>
	 * @param data
	 * @param key 长度至少8个字节以上，也就是64位或以上
	 * @return
	 */
	public static byte[] encryptECB(byte[] data, byte[] key) {
		Cipher cipher = SymmetricEncrypt.getEncryptCipher(key, null, "DES", "ECB", "PKCS5Padding");
		//缓存以上操作,主要缓存cipher
		//开始加密
		byte[] cipherByte;
		try {
			cipherByte = cipher.doFinal(data);
			return  cipherByte;
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] decryptECB(byte[] data, byte[] key) {
		return SymmetricEncrypt.decrypt(data, key, null, "DES", "ECB", "PKCS5Padding");
	}
	
	/**
	 * 
	 * 方法用途: 加密并base64编码CBC模式，默认填充方式PKCS5Padding<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度至少8个字节以上，也就是64位或以上
	 * @param charset
	 * @return
	 */
	public static String encryptAndBase64CBC(String data, String key, String charset) {
		return SymmetricEncrypt.encryptAndBase64(data, key, charset, true, "DES", "CBC", "PKCS5Padding");
	}

	/**
	 * 
	 * 方法用途: base64解码并解密CBC模式，默认填充方式PKCS5Padding<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度至少8个字节以上，也就是64位或以上
	 * @param charset
	 * @return
	 */
	public static String base64AndDecryptCBC(String data, String key, String charset) {
		return SymmetricEncrypt.base64AndDecrypt(data, key, charset, true, "DES", "CBC", "PKCS5Padding");
	}

	/**
	 * 
	 * 方法用途: 加密,CBC模式，默认填充方式PKCS5Padding<br>
	 * 操作步骤: 暂未缓存Cipher<br>
	 * @param data
	 * @param key 长度至少8个字节以上，也就是64位或以上
	 * @param iv
	 * @return
	 */
	public static byte[] encryptCBC(byte[] data, byte[] key,byte[] iv) {
		Cipher cipher = SymmetricEncrypt.getEncryptCipher(key, iv, "DES", "CBC", "PKCS5Padding");
		//缓存以上操作,主要缓存cipher
		//开始加密
		byte[] cipherByte;
		try {
			cipherByte = cipher.doFinal(data);
			return  cipherByte;
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] decryptCBC(byte[] data, byte[] key,byte[] iv) {
		return SymmetricEncrypt.decrypt(data, key, iv, "DES", "CBC", "PKCS5Padding");
	}

	public static String encryptToHexCBC(String data, String key, String charset) {
		return SymmetricEncrypt.encryptToHex(data, key, charset, true, "DES", "CBC", "PKCS5Padding");
	}

	public static String hexToDecryptCBC(String data, String key, String charset) {
		return SymmetricEncrypt.hexToDecrypt(data, key, charset, true, "DES", "CBC", "PKCS5Padding");
	}
}
