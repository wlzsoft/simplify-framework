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
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
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
	
	private static SecretKey getSecretKey(byte[] key) {
		try {
			// 从原始密钥数据创建DESKeySpec对象
			DESKeySpec keySpec = new DESKeySpec(key);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			// 一个SecretKey对象
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
	 * 方法用途: 待验证，是否可用<br>
	 * 操作步骤: TODO<br>
	 * @param str
	 * @param key
	 * @return
	 */
	public static String encrypt64(String str, String key) {
		try {
			if (str == null || str.length() < 1) return "";
			DESKeySpec keySpec = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			Cipher c1 = Cipher.getInstance("DES");
			c1.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] cipherByte = c1.doFinal(str.getBytes());
			//System.out.println(bytesToHexString(cipherByte)+"test1");
			//String sssString = new String(cipherByte);
			//System.out.println(sssString+"|sdfsdf");
//			System.out.println(Base64Encoder.encode(cipherByte));
			return new String(Base64Encrypt.encode(cipherByte));
		} catch ( Exception e ) {
			return "";
		}
	}
	
	
	/**
	 * 
	 * 
	 * 方法用途: DES加密Base64编码,key值作为IV向量值<br>
	 * 操作步骤: TODO<br>
	 * @param encryptString 
	 * @param encrptKey
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptAndBase64(String encryptString,String encrptKey,String charset) throws UnsupportedEncodingException{
		byte[] key = encrptKey.getBytes(charset);
        byte[] iv = encrptKey.getBytes(charset);
        byte[] data = encrypt(encryptString.getBytes(charset), key, iv);
        return new String(Base64Encrypt.encode(data));
		
	}
	/**
	 * 
	 * 
	 * 方法用途: DES解密Base64编码,key值作为IV向量值<br>
	 * 操作步骤: TODO<br>
	 * @param encryptString
	 * @param encrptKey
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decryptAndBase64(String encryptString,String encrptKey,String charset) throws UnsupportedEncodingException{
		
		//String aaa= java.net.URLDecoder.decode(java.net.URLEncoder.encode(encryptString,charset),charset);
        byte[] resultArr = Base64Encrypt.decodeToBytes(encryptString);
		byte[] key = encrptKey.getBytes(charset);
        byte[] iv = encrptKey.getBytes(charset);
//        byte[] data = CBCEncrypt(encryptString.getBytes(charset), key, iv);
        return new String(decrypt(resultArr, key, iv));
		
	}

	
	/**
	 * 
	 * 方法用途: DES 加密<br>
	 * 操作步骤: 注意：可解决中文乱码问题(加密算法，unicode转码，URIEncode转码，都可以解决乱码问题)<br>
	 * @param str
	 * @param key
	 * @param charset
	 * @return
	 */
	public static String encrypt(String str, String key,String charset) {
		try {
			String result = encrypt(str, key);
			if(!(null == charset || charset.length() < 1)){
				result = URLEncoder.encode(result, charset);
			}
			return result;
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String encrypt(String str, String key) {
		try {
			if (str == null || str.length() < 1) return "";
			
			byte[] cipherByte = DESEncrypt.encrypt(str.getBytes(),key.getBytes(),null);
			cipherByte = ByteHexUtil.bytes2Hex(cipherByte).getBytes();
			String result = new String(cipherByte);
			return result;
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 
	 * 方法用途: DES加密<br>
	 * 操作步骤: 1.加密模式：支持ECB(DES-ECB),CBC(DES-CBC),CTR(DES-CTR),OFB(DES-OFB),CFB(DES-CFB)等
	 *          2.填充模式：PKCS5Padding,PKCS7Padding,ZeroPadding,ISO10126,ansix923,NoPadding(设置这个选项表示不填充)等等<br>
	 * @param input
	 * @param key
	 * @param iv 加密向量
	 * @return
	 */
	public static byte[] encrypt(byte[] input,byte[] key, byte[] iv) {
		
//		if(secretKey==null){
//			secretKey = getSecretKey(key);
//		}
		try {
			// Cipher对象实际完成加密操作
			Cipher cipher;
			if(iv != null) {
				cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
				// 若采用NoPadding模式，data长度必须是8的倍数
				// cipher = Cipher.getInstance("DES/CBC/NoPadding");
				IvParameterSpec param = new IvParameterSpec(iv);
				// 用密匙初始化Cipher对象
				try {
					cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key),param);
				} catch (InvalidAlgorithmParameterException e) {
					e.printStackTrace();
				}
			} else {
				cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));
			}
			byte[] cipherByte = cipher.doFinal(input);
			return  cipherByte;
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
			if(iv != null) {
				// using DES in CBC mode
		        cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		        // 若采用NoPadding模式，data长度必须是8的倍数
		        // Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
		        // 用密匙初始化Cipher对象
		        IvParameterSpec param = new IvParameterSpec(iv);
		        try {
					cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key), param);
				} catch (InvalidAlgorithmParameterException e) {
					e.printStackTrace();
				}
			} else {
				cipher = Cipher.getInstance("DES");
//				cipher.init(Cipher.DECRYPT_MODE,secretKey);
				cipher.init(Cipher.DECRYPT_MODE,getSecretKey(key));
			}
			// 正式执行解密操作
			return cipher.doFinal(input);
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
	
	
	
	
	/**
	 * 
	 * 方法用途: DES 加密<br>
	 * 操作步骤: 注意：可解决中文乱码问题(加密算法，unicode转码，URIEncode转码，都可以解决乱码问题)<br>
	 * @param str
	 * @param key
	 * @param charset
	 * @return
	 */
	public static String decrypt(String str, String key,String charset) {
		try {
			if (str == null || str.length() < 1) {
				return "";
			}
			String result = new String(DESEncrypt.decrypt(ByteHexUtil.hex2Bytes(str),key.getBytes(),null),charset);
			if(!(null == charset || charset.length() < 1)){
				result = URLDecoder.decode(result, charset);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	

}
