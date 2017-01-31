package com.meizu.simplify.encrypt.symmetriccryptography.blowfish;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import com.meizu.simplify.encrypt.symmetriccryptography.SymmetricEncrypt;

/**
 * <p><b>Title:</b><i>BlowFish对称加密算法</i></p>
 * <p>Desc: Blowfish使用16到448位(bit)不同长度的密钥对数据进行16次加密,目前还没有针对blowfish的破解。32位处理器诞生后，Blowfish算法因其在加密速度上超越了DES而引起人们的关注。Blowfish算法具有加密速度快、紧凑、密钥长度可变、可免费使用等特点</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 下午5:10:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月9日 下午5:10:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class BlowfishEncrypt {

	//------------------------具体使用Blowfish加密算法的ECB模式
	
		/**
		 * 
		 * 方法用途: 加密并base64编码ECB模式<br>
		 * 操作步骤: TODO<br>
		 * @param data
		 * @param key 长度
		 * @param charset
		 * @param algorithmPadding 设置填充模式
		 * @return
		 */
		public static String encryptAndBase64ECB(String data, String key, String charset,String algorithmPadding) {
			return SymmetricEncrypt.encryptAndBase64(data, key, charset, false, "Blowfish", "ECB", algorithmPadding);
		}

		/**
		 * 
		 * 方法用途: base64解码并解密ECB模式<br>
		 * 操作步骤: TODO<br>
		 * @param data
		 * @param key 长度
		 * @param charset
		 * @param algorithmPadding 设置填充模式
		 * @return
		 */
		public static String base64AndDecryptECB(String data, String key, String charset,String algorithmPadding) {
			return SymmetricEncrypt.base64AndDecrypt(data, key, charset, false, "Blowfish", "ECB", algorithmPadding);
		}

		public static String encryptToHexECB(String data, String key, String charset,String algorithmPadding) {
			return SymmetricEncrypt.encryptToHex(data, key, charset, false, "Blowfish", "ECB", algorithmPadding);
		}

		public static String hexToDecryptECB(String data, String key, String charset,String algorithmPadding) {
			return SymmetricEncrypt.hexToDecrypt(data, key, charset, false, "Blowfish", "ECB", algorithmPadding);
		}
		
		//------------------------具体使用Blowfish加密算法的CBC模式
		
		/**
		 * 
		 * 方法用途: 加密并base64编码CBC模式<br>
		 * 操作步骤: TODO<br>
		 * @param data
		 * @param key 长度
		 * @param charset
		 * @param algorithmPadding 设置填充模式
		 * @return
		 */
		public static String encryptAndBase64CBC(String data, String key, String charset,String algorithmPadding) {
			return SymmetricEncrypt.encryptAndBase64(data, key, charset, false, "Blowfish", "CBC", algorithmPadding);
		}

		/**
		 * 
		 * 方法用途: base64解码并解密CBC模式<br>
		 * 操作步骤: TODO<br>
		 * @param data
		 * @param key 长度
		 * @param charset
		 * @param algorithmPadding 设置填充模式
		 * @return
		 */
		public static String base64AndDecryptCBC(String data, String key, String charset,String algorithmPadding) {
			return SymmetricEncrypt.base64AndDecrypt(data, key, charset, false, "Blowfish", "CBC", algorithmPadding);
		}

		public static String encryptToHexCBC(String data, String key, String charset,String algorithmPadding) {
			return SymmetricEncrypt.encryptToHex(data, key, charset, false, "Blowfish", "CBC", algorithmPadding);
		}

		public static String hexToDecryptCBC(String data, String key, String charset,String algorithmPadding) {
			return SymmetricEncrypt.hexToDecrypt(data, key, charset, false, "Blowfish", "CBC", algorithmPadding);
		}
		
		//------------------------具体Blowfish算法相关
		
		/**
		 * 
		 * 方法用途: 加密并base64编码<br>
		 * 操作步骤: TODO<br>
		 * @param data
		 * @param key 长度
		 * @param charset 字符编码
		 * @param iv 向量
		 * @param algorithmMode 分割模式
		 * @param algorithmPadding 填充模式
		 * @return
		 */
		public static String encryptAndBase64(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
			return SymmetricEncrypt.encryptAndBase64(data, key, charset, iv, "Blowfish", algorithmMode, algorithmPadding);
		}

		/**
		 * 
		 * 方法用途: base64解码并解密<br>
		 * 操作步骤: TODO<br>
		 * @param data
		 * @param key 长度
		 * @param charset 字符编码
		 * @param iv 向量
		 * @param algorithmMode 分割模式
		 * @param algorithmPadding 填充模式
		 * @return
		 */
		public static String base64AndDecrypt(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
			return SymmetricEncrypt.base64AndDecrypt(data, key, charset, iv, "Blowfish", algorithmMode, algorithmPadding);
		}

		/**
		 * 
		 * 方法用途: 加密并转换成16进制数据<br>
		 * 操作步骤: TODO<br>
		 * @param data
		 * @param key 长度
		 * @param charset 字符编码
		 * @param iv 向量
		 * @param algorithmMode 分割模式
		 * @param algorithmPadding 填充模式
		 * @return
		 */
		public static String encryptToHex(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
			return SymmetricEncrypt.encryptToHex(data, key, charset, iv, "Blowfish", algorithmMode, algorithmPadding);
		}

		/**
		 * 
		 * 方法用途: 16进制数据解密<br>
		 * 操作步骤: TODO<br>
		 * @param data
		 * @param key 长度
		 * @param charset 字符编码
		 * @param iv 向量
		 * @param algorithmMode 分割模式
		 * @param algorithmPadding 填充模式
		 * @return
		 */
		public static String hexToDecrypt(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
			return SymmetricEncrypt.hexToDecrypt(data, key, charset, iv, "Blowfish", algorithmMode, algorithmPadding);
		}
		
		//-----------------------具体使用Blowfish加密算法的ECB模式PKCS5Padding填充模式
		
		/**
		 * 
		 * 方法用途: 加密并base64编码ECB模式，默认填充方式PKCS5Padding<br>
		 * 操作步骤: TODO<br>
		 * @param data
		 * @param key 长度
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
		 * @param key 长度
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
		 * @param key 长度
		 * @return 返回加密后数据
		 */
		public static byte[] encryptECB(byte[] data, byte[] key) {
			Cipher cipher = SymmetricEncrypt.getEncryptCipher(key, null, "Blowfish", "ECB", "PKCS5Padding");
			//缓存以上操作,主要缓存cipher
			//开始加密
			byte[] cipherByte;
			try {
				cipherByte = cipher.doFinal(data);
				return  cipherByte;
			} catch (IllegalBlockSizeException | BadPaddingException e) {
				System.err.println("Blowfish加密时发生异常:"+e);
				e.printStackTrace();
			}
			return null;
		}
		
		public static byte[] decryptECB(byte[] data, byte[] key) {
			return SymmetricEncrypt.decrypt(data, key, null, "Blowfish", "ECB", "PKCS5Padding");
		}
		
		/**
		 * 
		 * 方法用途: 加密并base64编码CBC模式，默认填充方式PKCS5Padding<br>
		 * 操作步骤: TODO<br>
		 * @param data
		 * @param key 长度
		 * @param charset
		 * @return
		 */
		public static String encryptAndBase64CBC(String data, String key, String charset) {
			return SymmetricEncrypt.encryptAndBase64(data, key, charset, true, "Blowfish", "CBC", "PKCS5Padding");
		}

		/**
		 * 
		 * 方法用途: base64解码并解密CBC模式，默认填充方式PKCS5Padding<br>
		 * 操作步骤: TODO<br>
		 * @param data
		 * @param key 长度
		 * @param charset
		 * @return
		 */
		public static String base64AndDecryptCBC(String data, String key, String charset) {
			return SymmetricEncrypt.base64AndDecrypt(data, key, charset, true, "Blowfish", "CBC", "PKCS5Padding");
		}

		/**
		 * 
		 * 方法用途: 加密,CBC模式，默认填充方式PKCS5Padding<br>
		 * 操作步骤: 暂未缓存Cipher<br>
		 * @param data
		 * @param key 长度
		 * @param iv
		 * @return
		 */
		public static byte[] encryptCBC(byte[] data, byte[] key,byte[] iv) {
			Cipher cipher = SymmetricEncrypt.getEncryptCipher(key, iv, "Blowfish", "CBC", "PKCS5Padding");
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
			return SymmetricEncrypt.decrypt(data, key, iv, "Blowfish", "CBC", "PKCS5Padding");
		}

		public static String encryptToHexCBC(String data, String key, String charset) {
			return SymmetricEncrypt.encryptToHex(data, key, charset, true, "Blowfish", "CBC", "PKCS5Padding");
		}

		public static String hexToDecryptCBC(String data, String key, String charset) {
			return SymmetricEncrypt.hexToDecrypt(data, key, charset, true, "Blowfish", "CBC", "PKCS5Padding");
		}
	
}
