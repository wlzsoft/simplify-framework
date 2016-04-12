package com.meizu.simplify.encrypt.des;

import java.net.URLDecoder;
import java.net.URLEncoder;
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

import com.meizu.simplify.encrypt.ByteHexUtil;

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
	
	
	
	
	
	protected SecretKey secretKey;
	
	
	
	
	protected SecretKey getSecretKey(byte[] key) {
		try {
			DESKeySpec keySpec = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			secretKey = keyFactory.generateSecret(keySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	    return secretKey;
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
			
			byte[] cipherByte = new DESEncrypt().encode(str.getBytes(),key.getBytes());
			
			String result = new String(cipherByte);
			return result;
		} catch (Exception e) {
			return "";
		}
	}
	
	public byte[] encode(byte[] input,byte[] key) {
//		if(secretKey==null){
//			secretKey = getSecretKey(key);
//		}
		try {
			Cipher c1 = Cipher.getInstance("DES");
			c1.init(Cipher.ENCRYPT_MODE, getSecretKey(key));
			byte[] cipherByte = c1.doFinal(input);
//			return  cipherByte;
			return  ByteHexUtil.bytes2Hex(cipherByte).getBytes();
		} catch (NoSuchAlgorithmException e) {
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
	
	
	
	public byte[] decode(byte[] input,byte[] key) {
//		if(secretKey==null){
//			secretKey = getSecretKey(key);
//		}
		try {			
			
			Cipher c1 = Cipher.getInstance("DES");
//			c1.init(Cipher.DECRYPT_MODE,secretKey);
			c1.init(Cipher.DECRYPT_MODE,getSecretKey(key));
			return c1.doFinal(ByteHexUtil.hex2Bytes(new String(input)));
		} catch (NoSuchAlgorithmException e) {
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
	
	
	public static String decrypt(String str, String key) {
		try {
			if (str == null || str.length() < 1) return "";
			String result = new String(new DESEncrypt().decode(str.getBytes(),key.getBytes()));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
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
			String result = decrypt(str, key);
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
