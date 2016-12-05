package com.meizu.simplify.encrypt.des;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.meizu.simplify.encrypt.ByteHexUtil;
import com.meizu.simplify.encrypt.base64.Base64Encrypt;

/**
 * 
 * <p><b>Title:</b><i>DES加密算法</i></p>
 * <p>Desc: 对称加密算法有：DES，DESede(TripleDES)、AES、Blowfish、RC2、RC4(ARCFOUR) 等等算法,这些支持的密钥长度都不同，其中 Blowfish是目前性能最好的,也是最安全的
 *          1.对称加密算法的加密分割模式(字符串分割方式)：支持ECB(DES-ECB),CBC(DES-CBC),CTR(DES-CTR),OFB(DES-OFB),CFB(DES-CFB)等等
 *          2.对称加密算法的填充模式：PKCS5Padding,PKCS7Padding,ZeroPadding,ISO10126,ansix923,NoPadding(加密数据长度必须是8的倍数)，等等
 *          jdk的DES算法默认使用的ECB模式，它不需要向量，DES算法支持大部分的加密模式和填充模式
 *          其中3DES只支持ECB和CBC两种模式
CBC模式：CBC模式，必须有向量值，否则加密的数据不固定，导致加密的值无法解密(解密异常java.security.InvalidKeyException: Parameters missing)
IV(向量)：iv通过随机数（或伪随机）机制产生是一种比较常见的方法。iv的作用主要是用于产生密文的第一个block，为了提高安全性，使加密的数据更难被破解(用于避免明文中的重复字符在加密后还是重复的，减少加密后数据的规律)，iv通过随机方式产生是一种十分简便有效，原理就是在key基础上增加一个随机的混合字符串
注意：网络通讯中IPsec中采用了DES-CBC作为缺省的加密模式，其使用的IV是通讯包的时间戳
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
	
	/**
	 * 
	 * 方法用途: 获取生成原始安全密钥<br>
	 * 操作步骤: 这个方法在对象第一次初始化时生成，避免重复生成，目前是每次调用重新生成 TODO<br>
	 * @param key
	 * @param algorithm
	 * @return
	 */
	private static SecretKey getSecretKey(byte[] key,String algorithm) {
		try {
//			第一种写法:更通用，只需要指定算法(algorithm)
//			SecretKey secretKey = new SecretKeySpec(key, algorithm);
//			第二种写法：需要创建具体的keySpec，使用麻烦
			DESKeySpec keySpec = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
//			第二种写法:end
//			第三种写法:
//			SecureRandom secureRandom = null;  
//	        if (seed != null) {  
//	            secureRandom = new SecureRandom(base64AndDecrypt(seed));  
//	        } else {  
//	            secureRandom = new SecureRandom();  
//	        }  
//	        KeyGenerator kg = KeyGenerator.getInstance(algorithm);  
//	        kg.init(secureRandom);  
//	        SecretKey secretKey = kg.generateKey();  
//			第三种写法:end
			return secretKey;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	
	/**
	 * 
	 * 方法用途: DES加密并使用Base64编码<br>
	 * 操作步骤: 如果使用向量，那么key值作为IV向量值<br>
	 * @param data 
	 * @param key 必须是8个字节，如果是ascii编码，那么都是8个字符，如果是中文等其他字符，那么在charset是utf-8，是2到3个汉字("中文st"这个key才合法)，如果是gbk，那么是4汉字("中文测试"这个key才合法)
	 * @param charset
	 * @param useIV
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptAndBase64(String data,String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		try {
			byte[] byteKey = key.getBytes(charset);
			byte[] iv = null;
			if(useIV) {
				iv = key.getBytes(charset);
			}
			byte[] byteData = encrypt(data.getBytes(charset), byteKey, iv,algorithm,algorithmMode,algorithmPadding);
	        return new String(Base64Encrypt.encode(byteData));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: Base64解码并使用DES解密<br>
	 * 操作步骤: 如果使用向量，那么key值作为IV向量值<br>
	 * @param data
	 * @param key
	 * @param charset
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String base64AndDecrypt(String data,String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
       try {
			byte[] resultArr = Base64Encrypt.decodeToBytes(data);
			byte[] byteKey = key.getBytes(charset);
			byte[] iv = null;
			if(useIV) {
				iv = key.getBytes(charset);
			}
	        return new String(decrypt(resultArr, byteKey, iv,algorithm,algorithmMode,algorithmPadding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: 加密<br>
	 * 操作步骤: 针对向量的处理<br>
	 * @param data
	 * @param key
	 * @param charset
	 * @param useIV
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static byte[] encrypt(String data,String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		try {
			byte[] byteKey = key.getBytes(charset);
			byte[] iv = null;
			if(useIV) {
				iv = key.getBytes(charset);
			}
			byte[] byteData = encrypt(data.getBytes(charset), byteKey, iv,algorithm,algorithmMode,algorithmPadding);
			return byteData;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: 解密<br>
	 * 操作步骤: 针对向量的处理<br>
	 * @param data
	 * @param key
	 * @param charset
	 * @param useIV
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static String decrypt(byte[] data, String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		try {
			byte[] byteKey = key.getBytes(charset);
			byte[] iv = null;
			if(useIV) {
				iv = key.getBytes(charset);
			}
			byte[] cipherByte = decrypt(data,byteKey,iv,algorithm,algorithmMode,algorithmPadding);
			return new String(cipherByte);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: 加密并转成16进制数据<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key
	 * @param charset
	 * @param useIV
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static String encryptToHex(String data, String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		byte[] cipherByte = encrypt(data,key,charset,useIV,algorithm,algorithmMode,algorithmPadding);
		return ByteHexUtil.bytes2Hex(cipherByte);
	}
	
	/**
	 * 
	 * 方法用途: 16进制数据解密<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key
	 * @param charset
	 * @param useIV
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static String hexToDecrypt(String data, String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		byte[] byteData = ByteHexUtil.hex2Bytes(data);
		return decrypt(byteData, key, charset, useIV,algorithm,algorithmMode,algorithmPadding);
	}
	
	/**
	 * 
	 * 方法用途: URLEncoder编码并且DES加密<br>
	 * 操作步骤: 注意：可解决中文乱码问题(加密算法，unicode转码，URIEncode转码，都可以解决乱码问题)<br>
	 * @param data
	 * @param key
	 * @param charset
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static String urlEncodeAndEncrypt(String data, String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		try {
			if(!(null == charset || charset.length() < 1)){
				data = URLEncoder.encode(data, charset);
			}
			String result = encryptToHex(data, key,charset,useIV,algorithm,algorithmMode,algorithmPadding);
			return result;
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	/**
	 * 
	 * 方法用途: DES解密并URLEncoder解码<br>
	 * 操作步骤: 注意：可解决中文乱码问题(加密算法，unicode转码，URIEncode转码，都可以解决乱码问题)<br>
	 * @param data
	 * @param key
	 * @param charset
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static String decryptAndUrldecode(String data, String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		try {
			if (data == null || data.length() < 1) {
				return "";
			}
			String result = hexToDecrypt(data,key,charset,useIV,algorithm,algorithmMode,algorithmPadding);
			if(!(null == charset || charset.length() < 1)){
				result = URLDecoder.decode(result, charset);
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 
	 * 方法用途: 加密<br>
	 * 操作步骤: TODO <br>
	 * @param data
	 * @param key
	 * @param iv 加密向量-向量是为了提高安全性，使加密的数据更难被破解(用于避免明文中的重复字符在加密后还是重复的，减少加密后数据的规律)
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static byte[] encrypt(byte[] data,byte[] key, byte[] iv,String algorithm,String algorithmMode,String algorithmPadding) {
		
		try {
			Cipher cipher;
			if(iv != null) {//CBC模式，必须有向量值，否则加密的数据不固定，导致加密的值无法解密(解密异常java.security.InvalidKeyException: Parameters missing)
				cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
				IvParameterSpec param = new IvParameterSpec(iv);
				cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key,algorithm),param);
			} else {//ECB模式不支持向量
				cipher = Cipher.getInstance(algorithm+"/"+algorithmMode+"/"+algorithmPadding);
				cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key,algorithm));
			}
			byte[] cipherByte = cipher.doFinal(data);
			return  cipherByte;
		} catch (InvalidAlgorithmParameterException e) {
			System.err.println("无效算法向量参数异常,使用向量的CBC模式会有这个异常");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.err.println(algorithm+"算法，加密数据出错!");
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: 解密<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key
	 * @param iv 加密向量
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static byte[] decrypt(byte[] data,byte[] key, byte[] iv,String algorithm,String algorithmMode,String algorithmPadding) {
//		if(secretKey==null){
//			secretKey = getSecretKey(key);
//		}
		try {	
			Cipher cipher;
			if(iv != null) {//CBC模式，必须有向量值，否则加密的数据不固定，导致加密的值无法解密(解密异常java.security.InvalidKeyException: Parameters missing)
		        cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		        IvParameterSpec param = new IvParameterSpec(iv);
				cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key,algorithm), param);
			} else {//ECB模式不支持向量
				cipher = Cipher.getInstance(algorithm+"/"+algorithmMode+"/"+algorithmPadding);
//				cipher.init(Cipher.DECRYPT_MODE,secretKey);
				cipher.init(Cipher.DECRYPT_MODE,getSecretKey(key,algorithm));
			}
			// 执行解密操作
			return cipher.doFinal(data);
		} catch (InvalidAlgorithmParameterException e) {
			System.err.println("无效算法向量参数异常,使用向量的CBC模式会有这个异常");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.err.println(algorithm+"算法，解密出错。");
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	//------------------------具体使用DES加密算法的ECB模式
	
	/**
	 * 
	 * 方法用途: 加密并base64编码ECB模式<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key
	 * @param charset
	 * @param algorithmPadding 设置填充模式
	 * @return
	 */
	public static String encryptAndBase64ECB(String data, String key, String charset,String algorithmPadding) {
		return encryptAndBase64(data, key, charset, false, "DES", "ECB", algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: base64解码并解密ECB模式<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key
	 * @param charset
	 * @param algorithmPadding 设置填充模式
	 * @return
	 */
	public static String base64AndDecryptECB(String data, String key, String charset,String algorithmPadding) {
		return base64AndDecrypt(data, key, charset, false, "DES", "ECB", algorithmPadding);
	}

	public static String encryptToHexECB(String data, String key, String charset,String algorithmPadding) {
		return encryptToHex(data, key, charset, false, "DES", "ECB", algorithmPadding);
	}

	public static String hexToDecryptECB(String data, String key, String charset,String algorithmPadding) {
		return hexToDecrypt(data, key, charset, false, "DES", "ECB", algorithmPadding);
	}
	
	//------------------------具体使用DES加密算法的CBC模式
	
	/**
	 * 
	 * 方法用途: 加密并base64编码CBC模式<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key
	 * @param charset
	 * @param algorithmPadding 设置填充模式
	 * @return
	 */
	public static String encryptAndBase64CBC(String data, String key, String charset,String algorithmPadding) {
		return encryptAndBase64(data, key, charset, false, "DES", "CBC", algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: base64解码并解密CBC模式<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key
	 * @param charset
	 * @param algorithmPadding 设置填充模式
	 * @return
	 */
	public static String base64AndDecryptCBC(String data, String key, String charset,String algorithmPadding) {
		return base64AndDecrypt(data, key, charset, false, "DES", "CBC", algorithmPadding);
	}

	public static String encryptToHexCBC(String data, String key, String charset,String algorithmPadding) {
		return encryptToHex(data, key, charset, false, "DES", "CBC", algorithmPadding);
	}

	public static String hexToDecryptCBC(String data, String key, String charset,String algorithmPadding) {
		return hexToDecrypt(data, key, charset, false, "DES", "CBC", algorithmPadding);
	}
	
	//------------------------具体DES算法相关
	
	/**
	 * 
	 * 方法用途: 加密并base64编码<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 
	 * @param charset 字符编码
	 * @param iv 向量
	 * @param algorithmMode 分割模式
	 * @param algorithmPadding 填充模式
	 * @return
	 */
	public static String encryptAndBase64(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
		return encryptAndBase64(data, key, charset, iv, "DES", algorithmMode, algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: base64解码并解密<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 
	 * @param charset 字符编码
	 * @param iv 向量
	 * @param algorithmMode 分割模式
	 * @param algorithmPadding 填充模式
	 * @return
	 */
	public static String base64AndDecrypt(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
		return base64AndDecrypt(data, key, charset, iv, "DES", algorithmMode, algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: 加密并转换成16进制数据<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 
	 * @param charset 字符编码
	 * @param iv 向量
	 * @param algorithmMode 分割模式
	 * @param algorithmPadding 填充模式
	 * @return
	 */
	public static String encryptToHex(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
		return encryptToHex(data, key, charset, iv, "DES", algorithmMode, algorithmPadding);
	}

	/**
	 * 
	 * 方法用途: 16进制数据解密<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key 
	 * @param charset 字符编码
	 * @param iv 向量
	 * @param algorithmMode 分割模式
	 * @param algorithmPadding 填充模式
	 * @return
	 */
	public static String hexToDecrypt(String data, String key, String charset,boolean iv,String algorithmMode,String algorithmPadding) {
		return hexToDecrypt(data, key, charset, iv, "DES", algorithmMode, algorithmPadding);
	}
	
	//-----------------------具体使用DES加密算法的ECB模式PKCS5Padding填充模式
	
	/**
	 * 
	 * 方法用途: 加密并base64编码ECB模式，默认填充方式PKCS5Padding<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key
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
	 * @param key
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
	
	public static byte[] encryptECB(byte[] input, byte[] key) {
		return encrypt(input, key, null, "DES", "ECB", "PKCS5Padding");
	}
	
	public static byte[] decryptECB(byte[] data, byte[] key) {
		return decrypt(data, key, null, "DES", "ECB", "PKCS5Padding");
	}
	
	/**
	 * 
	 * 方法用途: 加密并base64编码CBC模式，默认填充方式PKCS5Padding<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key
	 * @param charset
	 * @return
	 */
	public static String encryptAndBase64CBC(String data, String key, String charset) {
		return encryptAndBase64(data, key, charset, true, "DES", "CBC", "PKCS5Padding");
	}

	/**
	 * 
	 * 方法用途: base64解码并解密CBC模式，默认填充方式PKCS5Padding<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key
	 * @param charset
	 * @return
	 */
	public static String base64AndDecryptCBC(String data, String key, String charset) {
		return base64AndDecrypt(data, key, charset, true, "DES", "CBC", "PKCS5Padding");
	}

	public static byte[] encryptCBC(byte[] data, byte[] key) {
		return encrypt(data, key, key, "DES", "CBC", "PKCS5Padding");
	}
	
	public static byte[] decryptCBC(byte[] data, byte[] key) {
		return decrypt(data, key, key, "DES", "CBC", "PKCS5Padding");
	}

	public static String encryptToHexCBC(String data, String key, String charset) {
		return encryptToHex(data, key, charset, true, "DES", "CBC", "PKCS5Padding");
	}

	public static String hexToDecryptCBC(String data, String key, String charset) {
		return hexToDecrypt(data, key, charset, true, "DES", "CBC", "PKCS5Padding");
	}

}
