package com.meizu.simplify.encrypt.symmetriccryptography.aes;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import com.meizu.simplify.encrypt.symmetriccryptography.SymmetricBaseEncrypt;

/**
  * <p><b>Title:</b><i>AES加密算法(推荐使用的对称加密算法)</i></p>
 * <p>Desc: 高级加密标准（Advanced Encryption Standard，AES），又称Rijndael加密法，
 * 是美国联邦政府采用的一种区块加密标准。
 * 这个标准用来替代原先的DES，已经被多方分析且广为全世界所使用。
 * 经过五年的甄选流程，高级加密标准由美国国家标准与技术研究院（NIST）
 * 于2001年11月26日发布于FIPS PUB 197，并在2002年5月26日成为有效的标准。
 * 2006年，高级加密标准已然成为对称密钥加密中最流行的算法之一。
    AES的区块长度固定为128 位元，密钥长度则可以是128，192或256位元。
    特点：比DES和3DES(三重数据加密标准)更快，兼容设备，安全级别高，适合大量数据，由于AES已经被一些通用cpu直接硬件支持 AES instruction set
所以应用性能上有优势(前提cpu支持)
    </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月8日 下午4:04:27</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月8日 下午4:04:27</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class AESEncrypt {

	//------------------------具体使用AES加密算法的ECB模式
	
	/**
	 * 
	 * 方法用途: 加密并base64编码ECB模式<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度16个字节，也就是128位。或者是192,256位
	 * @param charset
	 * @param algorithmPadding 设置填充模式
	 * @return
	 */
	public static String encryptAndBase64ECB(String data, String key, String charset,String algorithmPadding) {
		return SymmetricBaseEncrypt.encryptAndBase64(data, key, charset, false, "AES", "ECB", algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: base64解码并解密ECB模式<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度16个字节，也就是128位。或者是192,256位
	 * @param charset
	 * @param algorithmPadding 设置填充模式
	 * @return
	 */
	public static String base64AndDecryptECB(String data, String key, String charset,String algorithmPadding) {
		return SymmetricBaseEncrypt.base64AndDecrypt(data, key, charset, false, "AES", "ECB", algorithmPadding);
	}

	public static String encryptToHexECB(String data, String key, String charset,String algorithmPadding) {
		return SymmetricBaseEncrypt.encryptToHex(data, key, charset, false, "AES", "ECB", algorithmPadding);
	}

	public static String hexToDecryptECB(String data, String key, String charset,String algorithmPadding) {
		return SymmetricBaseEncrypt.hexToDecrypt(data, key, charset, false, "AES", "ECB", algorithmPadding);
	}
	
	//------------------------具体使用AES加密算法的CBC模式
	
	/**
	 * 
	 * 方法用途: 加密并base64编码CBC模式<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度16个字节，也就是128位。或者是192,256位
	 * @param charset
	 * @param algorithmPadding 设置填充模式
	 * @return
	 */
	public static String encryptAndBase64CBC(String data, String key, String charset,String algorithmPadding) {
		return SymmetricBaseEncrypt.encryptAndBase64(data, key, charset, false, "AES", "CBC", algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: base64解码并解密CBC模式<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度16个字节，也就是128位。或者是192,256位
	 * @param charset
	 * @param algorithmPadding 设置填充模式
	 * @return
	 */
	public static String base64AndDecryptCBC(String data, String key, String charset,String algorithmPadding) {
		return SymmetricBaseEncrypt.base64AndDecrypt(data, key, charset, false, "AES", "CBC", algorithmPadding);
	}

	public static String encryptToHexCBC(String data, String key, String charset,String algorithmPadding) {
		return SymmetricBaseEncrypt.encryptToHex(data, key, charset, false, "AES", "CBC", algorithmPadding);
	}

	public static String hexToDecryptCBC(String data, String key, String charset,String algorithmPadding) {
		return SymmetricBaseEncrypt.hexToDecrypt(data, key, charset, false, "AES", "CBC", algorithmPadding);
	}
	
	//------------------------具体AES算法相关
	
	/**
	 * 
	 * 方法用途: 加密并base64编码<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度16个字节，也就是128位。或者是192,256位
	 * @param charset 字符编码
	 * @param iv 向量
	 * @param algorithmMode 分割模式
	 * @param algorithmPadding 填充模式
	 * @return
	 */
	public static String encryptAndBase64(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
		return SymmetricBaseEncrypt.encryptAndBase64(data, key, charset, iv, "AES", algorithmMode, algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: base64解码并解密<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度16个字节，也就是128位。或者是192,256位
	 * @param charset 字符编码
	 * @param iv 向量
	 * @param algorithmMode 分割模式
	 * @param algorithmPadding 填充模式
	 * @return
	 */
	public static String base64AndDecrypt(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
		return SymmetricBaseEncrypt.base64AndDecrypt(data, key, charset, iv, "AES", algorithmMode, algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: 加密并转换成16进制数据<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度16个字节，也就是128位。或者是192,256位
	 * @param charset 字符编码
	 * @param iv 向量
	 * @param algorithmMode 分割模式
	 * @param algorithmPadding 填充模式
	 * @return
	 */
	public static String encryptToHex(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
		return SymmetricBaseEncrypt.encryptToHex(data, key, charset, iv, "AES", algorithmMode, algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: 16进制数据解密<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度16个字节，也就是128位。或者是192,256位
	 * @param charset 字符编码
	 * @param iv 向量
	 * @param algorithmMode 分割模式
	 * @param algorithmPadding 填充模式
	 * @return
	 */
	public static String hexToDecrypt(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
		return SymmetricBaseEncrypt.hexToDecrypt(data, key, charset, iv, "AES", algorithmMode, algorithmPadding);
	}
	
	//-----------------------具体使用AES加密算法的ECB模式PKCS5Padding填充模式
	
	/**
	 * 
	 * 方法用途: 加密并base64编码ECB模式，默认填充方式PKCS5Padding<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度16个字节，也就是128位。或者是192,256位
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
	 * @param key 长度16个字节，也就是128位。或者是192,256位
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
	 * @param data 待加密的数据
	 * @param key 长度16个字节，也就是128位。或者是192,256位
	 * @return 返回加密后数据
	 */
	public static byte[] encryptECB(byte[] data, byte[] key) {
		Cipher cipher = SymmetricBaseEncrypt.getEncryptCipher(key, null, "AES", "ECB", "PKCS5Padding");
		//缓存以上操作,主要缓存cipher
		//开始加密
		byte[] cipherByte;
		try {
			cipherByte = cipher.doFinal(data);
			return  cipherByte;
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			System.err.println("AES加密时发生异常:"+e);
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] decryptECB(byte[] data, byte[] key) {
		return SymmetricBaseEncrypt.decrypt(data, key, null, "AES", "ECB", "PKCS5Padding");
	}
	
	/**
	 * 
	 * 方法用途: 加密并base64编码CBC模式，默认填充方式PKCS5Padding<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 长度16个字节，也就是128位。或者是192,256位
	 * @param charset
	 * @return
	 */
	public static String encryptAndBase64CBC(String data, String key, String charset) {
		return SymmetricBaseEncrypt.encryptAndBase64(data, key, charset, true, "AES", "CBC", "PKCS5Padding");
	}

	/**
	 * 
	 * 方法用途: base64解码并解密CBC模式，默认填充方式PKCS5Padding<br>
	 * 操作步骤: TODO<br>
	 * @param data 必须被base64编码过的数据
	 * @param key 长度16个字节，也就是128位。或者是192,256位
	 * @param charset
	 * @return
	 */
	public static String base64AndDecryptCBC(String data, String key, String charset) {
		return SymmetricBaseEncrypt.base64AndDecrypt(data, key, charset, true, "AES", "CBC", "PKCS5Padding");
	}

	/**
	 * 
	 * 方法用途: 加密,CBC模式，默认填充方式PKCS5Padding<br>
	 * 操作步骤: 暂未缓存Cipher<br>
	 * @param data
	 * @param key 长度16个字节，也就是128位。或者是192,256位
	 * @param iv
	 * @return
	 */
	public static byte[] encryptCBC(byte[] data, byte[] key,byte[] iv) {
		Cipher cipher = SymmetricBaseEncrypt.getEncryptCipher(key, iv, "AES", "CBC", "PKCS5Padding");
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
		return SymmetricBaseEncrypt.decrypt(data, key, iv, "AES", "CBC", "PKCS5Padding");
	}

	public static String encryptToHexCBC(String data, String key, String charset) {
		return SymmetricBaseEncrypt.encryptToHex(data, key, charset, true, "AES", "CBC", "PKCS5Padding");
	}

	public static String hexToDecryptCBC(String data, String key, String charset) {
		return SymmetricBaseEncrypt.hexToDecrypt(data, key, charset, true, "AES", "CBC", "PKCS5Padding");
	}

}
