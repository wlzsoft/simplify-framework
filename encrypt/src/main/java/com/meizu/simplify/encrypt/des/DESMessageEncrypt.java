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
import com.meizu.simplify.encrypt.base64.Base64Encoder;

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
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class DESMessageEncrypt {
	public byte[] encode(byte[] input) {
		if(secretKey==null){
			secretKey = getSecretKey("meizuall".getBytes());
		}
		try {
			Cipher c1 = Cipher.getInstance("DES");
			c1.init(Cipher.ENCRYPT_MODE, getSecretKey("meizuall".getBytes()));
			byte[] codes =  c1.doFinal(input);
			return  ByteHexUtil.bytes2Hex(codes).getBytes();
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
	
	
	
	
	protected SecretKey secretKey;
	
	public byte[] decode(byte[] input) {
		if(secretKey==null){
			secretKey = getSecretKey("meizuall".getBytes());
		}
		try {			
			Cipher c1 = Cipher.getInstance("DES");
			c1.init(Cipher.DECRYPT_MODE,secretKey);
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
	
	public byte[] decode(byte[] input,byte[] key) {
		try {			
			Cipher c1 = Cipher.getInstance("DES");
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
	
	public byte[] encode(byte[] input,byte[] key) {
		
		try {
			Cipher c1 = Cipher.getInstance("DES");
			c1.init(Cipher.ENCRYPT_MODE, getSecretKey(key));
			byte[] codes =  c1.doFinal(input);
			return  ByteHexUtil.bytes2Hex(codes).getBytes();
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
	 
	
	protected SecretKey getSecretKey(byte[] key) {
		try {
			SecretKeyFactory keygen = SecretKeyFactory.getInstance("DES");
			DESKeySpec keySpec = new DESKeySpec(key);				
			secretKey= keygen.generateSecret(keySpec);
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
			if(str == null || str.length() < 1) return "";
			byte[] cipherByte = new DESMessageEncrypt().encode(str.getBytes(),key.getBytes());
			String result = ByteHexUtil.bytes2Hex(cipherByte);
			if(!(null == charset || charset.length() < 1)){
				result = URLEncoder.encode(result, charset);
			}
			return result;
		} catch (Exception e) {
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
			if(str == null || str.length() < 1) return "";
			String result = new String(new DESMessageEncrypt().decode(str.getBytes(),key.getBytes()));
			if(!(null == charset || charset.length() < 1)){
				result = URLDecoder.decode(result, charset);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	public static String encrypt(String str, String key) {
		return encrypt(str,key,null);
	}

	public static String decrypt(String str, String key) {
		return decrypt(str, key,null);
	}

	public static void main(String[] args) {
//		System.out.println(new DESMessageEncrypt()
//		.encode("123456789012"));
//
//		System.out.println(new DESMessageEncrypt()
//		.encode("�ز�"));
		
//		System.out.println(new DESMessageEncrypt()
//		.decode("e0e9f8daabe4f77db4298411b075e38e742dcc96c2085da6e0130bf0b0f1dd7c23aa70c3d1bd4438b941790bbc0fcf7406587467eade1be9b4df0afa22f5ffb9".toLowerCase()));
		
		String token=DES.encrypt("meizu&123456","meizuall");
		String token64 = DES.encrypt64("meizu&123456","meizuall");
		System.out.println(new String(new DESMessageEncrypt().encode("meizu&123456".getBytes()))+"kkkk");
		//String token64 = DESMessageEncrypt.encrypt64("meizuall","meizu&123456");
//		 BASE64Encoder base64Encoder = new BASE64Encoder();
//         strMi = base64Encoder.encode(byteMi);
		//a2ee5e1d00de3fc5320a95beaf15b692
		
		System.out.println(token);//a2ee5e1d00de3fc5320a95beaf15b692
		System.out.println(Base64Encoder.encode(token));//YTJlZTVlMWQwMGRlM2ZjNTMyMGE5NWJlYWYxNWI2OTI=
		System.out.println(token64);//ou5eHQDeP8UyCpW+rxW2kg==
		
		
		
		String str="6cb2606b4382afd1659a4b4031f65a2c6ef727f10c03de27f4ed22e84d8ef49a8ca9af6dd4f68fc2a9c1f322be1043efd50b4070bb33a84c1c4433fdfea98cb913f7f76703e7c5ded1c494c2cc288c4bd055d335bf2a0a779135eb694b5da73e6499b1cd74d2b3e957eb793aabc23b1ca7949c3eb28abfce76ce7c7e4120d8ecbd74b61603637e817a7fce656a32f81ecea8a1ac96c701c6";
		System.out.println(decrypt(str, "402880E6"));
//		System.out.println(decrypt("cde01d1bc4311736", "sDx5show"));
		//System.out.println(hex2byte("a13qswdswqe").length);
	}
}
