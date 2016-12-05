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
 * <p>Desc: 1.加密模式：支持ECB(DES-ECB),CBC(DES-CBC),CTR(DES-CTR),OFB(DES-OFB),CFB(DES-CFB)等
	 *      2.填充模式：PKCS5Padding,PKCS7Padding,ZeroPadding,ISO10126,ansix923,NoPadding(加密数据长度必须是8的倍数)，等等</p>
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
	
	
//	protected SecretKey secretKey;
	
	/**
	 * 
	 * 方法用途: 获取原始安全密钥<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @return
	 */
	private static SecretKey getSecretKey(byte[] key) {
		try {
			DESKeySpec keySpec = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//			secretKey = keyFactory.generateSecret(keySpec);
//			return secretKey;
			return keyFactory.generateSecret(keySpec);
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
	 * @param encryptString 
	 * @param encrptKey 必须是8个字节，如果是ascii编码，那么都是8个字符，如果是中文等其他字符，那么在charset是utf-8，是2到3个汉字("中文st"这个key才合法)，如果是gbk，那么是4汉字("中文测试"这个key才合法)
	 * @param charset
	 * @param useIV
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptAndBase64(String encryptString,String encrptKey,String charset,boolean useIV) throws UnsupportedEncodingException{
		byte[] key = encrptKey.getBytes(charset);
		byte[] iv = null;
		if(useIV) {
			iv = encrptKey.getBytes(charset);
		}
		byte[] data = encrypt(encryptString.getBytes(charset), key, iv);
        return new String(Base64Encrypt.encode(data));
		
	}
	
	/**
	 * 
	 * 方法用途: Base64解码并使用DES解密<br>
	 * 操作步骤: 如果使用向量，那么key值作为IV向量值<br>
	 * @param encryptString
	 * @param encrptKey
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String base64AndDecrypt(String encryptString,String encrptKey,String charset,boolean useIV) throws UnsupportedEncodingException{
        byte[] resultArr = Base64Encrypt.decodeToBytes(encryptString);
		byte[] key = encrptKey.getBytes(charset);
		byte[] iv = null;
		if(useIV) {
			iv = encrptKey.getBytes(charset);
		}
        return new String(decrypt(resultArr, key, iv));
		
	}
	
	public static byte[] encrypt(String encryptString,String encrptKey,String charset,boolean useIV) {
		try {
			byte[] key = encrptKey.getBytes(charset);
			byte[] iv = null;
			if(useIV) {
				iv = encrptKey.getBytes(charset);
			}
			byte[] data = encrypt(encryptString.getBytes(charset), key, iv);
			return data;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decrypt(byte[] data, String encrptKey,String charset,boolean useIV) {
		try {
			byte[] key = encrptKey.getBytes(charset);
			byte[] iv = null;
			if(useIV) {
				iv = encrptKey.getBytes(charset);
			}
			byte[] cipherByte = decrypt(data,key,iv);
			return new String(cipherByte);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String encryptToHex(String encryptString, String encrptKey,String charset,boolean useIV) {
		byte[] cipherByte = encrypt(encryptString,encrptKey,charset,useIV);
		return ByteHexUtil.bytes2Hex(cipherByte);
	}
	
	public static String hexToDecrypt(String encryptString, String encrptKey,String charset,boolean useIV) {
		byte[] data = ByteHexUtil.hex2Bytes(encryptString);
		return decrypt(data, encrptKey, charset, useIV);
	}
	
	/**
	 * 
	 * 方法用途: URLEncoder编码并且DES加密<br>
	 * 操作步骤: 注意：可解决中文乱码问题(加密算法，unicode转码，URIEncode转码，都可以解决乱码问题)<br>
	 * @param str
	 * @param key
	 * @param charset
	 * @return
	 */
	public static String urlEncodeAndEncrypt(String encryptString, String encrptKey,String charset,boolean useIV) {
		try {
			if(!(null == charset || charset.length() < 1)){
				encryptString = URLEncoder.encode(encryptString, charset);
			}
			String result = encryptToHex(encryptString, encrptKey,charset,useIV);
			return result;
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	/**
	 * 
	 * 方法用途: DES解密并URLEncoder解码<br>
	 * 操作步骤: 注意：可解决中文乱码问题(加密算法，unicode转码，URIEncode转码，都可以解决乱码问题)<br>
	 * @param str
	 * @param key
	 * @param charset
	 * @return
	 */
	public static String decryptAndUrldecode(String str, String key,String charset,boolean useIV) {
		try {
			if (str == null || str.length() < 1) {
				return "";
			}
			String result = hexToDecrypt(str,key,charset,useIV);
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
	 * 方法用途: DES加密<br>
	 * 操作步骤: TODO <br>
	 * @param input
	 * @param key
	 * @param iv 加密向量-向量是为了提高安全性，使加密的数据更难被破解(用于明文中的重复字符，在加密后，还是重复，这样就有规律可循)
	 * @return
	 */
	public static byte[] encrypt(byte[] input,byte[] key, byte[] iv) {
		
//		if(secretKey==null){
//			secretKey = getSecretKey(key);
//		}
		try {
			Cipher cipher;
			if(iv != null) {//CBC模式，必须有向量值，否则加密的数据不固定，导致加密的值无法解密(解密异常java.security.InvalidKeyException: Parameters missing)
				cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
				IvParameterSpec param = new IvParameterSpec(iv);
				cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key),param);
			} else {//ECB模式不支持向量
				cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));
			}
			byte[] cipherByte = cipher.doFinal(input);
			return  cipherByte;
		} catch (InvalidAlgorithmParameterException e) {
			System.err.println("无效算法向量参数异常,使用向量的CBC模式会有这个异常");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.err.println("DES算法，加密数据出错!");
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
	 * 方法用途: DES解密<br>
	 * 操作步骤: TODO<br>
	 * @param input 
	 * @param key 
	 * @param iv 加密向量
	 * @return
	 */
	public static byte[] decrypt(byte[] input,byte[] key, byte[] iv) {
//		if(secretKey==null){
//			secretKey = getSecretKey(key);
//		}
		try {	
			Cipher cipher;
			if(iv != null) {//CBC模式，必须有向量值，否则加密的数据不固定，导致加密的值无法解密(解密异常java.security.InvalidKeyException: Parameters missing)
		        cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		        IvParameterSpec param = new IvParameterSpec(iv);
				cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key), param);
			} else {//ECB模式不支持向量
				cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
//				cipher.init(Cipher.DECRYPT_MODE,secretKey);
				cipher.init(Cipher.DECRYPT_MODE,getSecretKey(key));
			}
			// 执行解密操作
			return cipher.doFinal(input);
		} catch (InvalidAlgorithmParameterException e) {
			System.err.println("无效算法向量参数异常,使用向量的CBC模式会有这个异常");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.err.println("DES算法，解密出错。");
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

}
