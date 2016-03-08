package com.meizu.simplify.encrypt.des;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.meizu.simplify.encrypt.base64.Base64Decoder;
import com.meizu.simplify.encrypt.base64.Base64Encoder;
import com.meizu.simplify.encrypt.base64.ByteHexUtil;

/**
 * 
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc:TODO ECB是其中一种字串分割方式，除了DES以外，其他加密方式也会使用这种分割方式的，而Java默认产生的DES算法就是用ECB方法，ECB不需要向量
 * ，当然也就不支持向量了 除了ECB，DES还支持CBC、CFB、OFB，而3DES只支持ECB和CBC两种
 * http://www.tropsoft.com/strongenc/des3.htm
 * CBC支持并且必须有向量，具体算法这里就不说了。合作商给的.net代码没有声明CBC模式，似乎是.net默认的方式就是CBC的 于是把模式改成CBC
 * Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年5月15日 下午2:47:11</p>
 * <p>Modified By:zll-</p>
 * <p>Modified Date:2015年5月15日 下午2:47:11</p>
 * @author <a href="mailto:zhangliangliang@meizu.com" title="邮箱地址">zll</a>
 * @version Version 0.1
 *
 */
public class DES {
	
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
	public static String DESAndBase64Encrypt(String encryptString,String encrptKey,String charset) throws UnsupportedEncodingException{
		byte[] key = encrptKey.getBytes(charset);
        byte[] iv = encrptKey.getBytes(charset);
        byte[] data = CBCEncrypt(encryptString.getBytes(charset), key, iv);
        return Base64Encoder.encode(data);
		
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
	public static String DESAndBase64Decrypt(String encryptString,String encrptKey,String charset) throws UnsupportedEncodingException{
		
		
		//String aaa= java.net.URLDecoder.decode(java.net.URLEncoder.encode(encryptString,charset),charset);
        byte[] resultArr = Base64Decoder.decodeToBytes(encryptString);
        
        
		byte[] key = encrptKey.getBytes(charset);
        byte[] iv = encrptKey.getBytes(charset);
//        byte[] data = CBCEncrypt(encryptString.getBytes(charset), key, iv);
        return new String(CBCDecrypt(resultArr, key, iv));
		
	}

	/**
	 * 
	 * 
	 * 方法用途: DES的CBC模式加密<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key
	 * @param iv
	 * @return
	 */
	public static byte[] CBCEncrypt(byte[] data, byte[] key, byte[] iv) {

		try {
			// 从原始密钥数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);

			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);

			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			// 若采用NoPadding模式，data长度必须是8的倍数
			// Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");

			// 用密匙初始化Cipher对象
			IvParameterSpec param = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, param);

			// 执行加密操作
			byte encryptedData[] = cipher.doFinal(data);

			return encryptedData;
		} catch (Exception e) {
			System.err.println("DES算法，加密数据出错!");
			e.printStackTrace();
		}

		return null;
	}
	/**
	 * 
	 * 
	 * 方法用途: DES的CBC解密<br>
	 * 操作步骤: TODO<br>
	 * @param data 
	 * @param key 
	 * @param iv 加密向量
	 * @return
	 */
	public static byte[] CBCDecrypt(byte[] data, byte[] key, byte[] iv) {
	       try {
	           // 从原始密匙数据创建一个DESKeySpec对象
	           DESKeySpec dks = new DESKeySpec(key);


	           // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
	           // 一个SecretKey对象
	           SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	           SecretKey secretKey = keyFactory.generateSecret(dks);


	           // using DES in CBC mode
	           Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	           // 若采用NoPadding模式，data长度必须是8的倍数
	           // Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");


	           // 用密匙初始化Cipher对象
	           IvParameterSpec param = new IvParameterSpec(iv);
	           cipher.init(Cipher.DECRYPT_MODE, secretKey, param);


	           // 正式执行解密操作
	           byte decryptedData[] = cipher.doFinal(data);


	           return decryptedData;
	       } catch (Exception e) {
	           System.err.println("DES算法，解密出错。");
	           e.printStackTrace();
	       }


	       return null;
	  }
	
	public static void main(String[] args) {
		try {
			String encryptString = DES.DESAndBase64Encrypt("meizu&123456", "meizuall", "utf-8");
			System.out.println(encryptString+"||||||||||||||");
			String decryptString = DES.DESAndBase64Decrypt(encryptString, "meizuall", "utf-8");
			System.out.println(decryptString+"////////////");
			
			String token=encrypt("meizu&123456","meizuall");
			String token64 = encrypt64("meizu&123456","meizuall");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static String encrypt(String str, String key) {
		try {
			if (str == null || str.length() < 1) return "";
			DESKeySpec keySpec = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			Cipher c1 = Cipher.getInstance("DES");
			c1.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] cipherByte = c1.doFinal(str.getBytes());
			return ByteHexUtil.bytes2Hex(cipherByte);
		} catch ( Exception e ) {
			return "";
		}
	}
	
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
			return Base64Encoder.encode(cipherByte);
		} catch ( Exception e ) {
			return "";
		}
	}
	

	public static String decrypt(String str, String key) {
		try {
			if (str == null || str.length() < 1) return "";
			DESKeySpec keySpec = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			Cipher c1 = Cipher.getInstance("DES");
			c1.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(c1.doFinal(ByteHexUtil.hex2byte(str)));
		} catch ( Exception e ) {
			e.printStackTrace();
			return "";
		}
	}

}