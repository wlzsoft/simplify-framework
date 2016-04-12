package com.meizu.simplify.encrypt.aes;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.meizu.simplify.encrypt.base64.Base64Encrypt;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
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
	
		/**
		 * 
		 * 方法用途: AES加密<br>
		 * 操作步骤: TODO<br>
		 * @param source 待加密的内容
		 * @param key 秘钥
		 * @return 返回加密后的内容
		 */
		public static String encrypt(String source, String key) {
			try {
				byte[] raw = key.getBytes("UTF-8");
				SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, keySpec);
				byte[] encrypted = cipher.doFinal(source.getBytes("UTF-8"));
				return new String(Base64Encrypt.encode(encrypted));
			} catch (Exception e) {
				throw new RuntimeException("AES加密时发生异常。", e);
			}
		}
	
		/**
		 * 
		 * 方法用途: AES解密<br>
		 * 操作步骤: TODO<br>
		 * @param source 待解密的内容
		 * @param key 秘钥
		 * @return 返回解密后的内容
		 */
		public static String decrypt(String source, String key) {
			try {
				byte[] raw = key.getBytes("UTF-8");
				SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, keySpec);
				byte[] encrypted = Base64Encrypt.decode(source.getBytes());
				byte[] original = cipher.doFinal(encrypted);
				return new String(original, "UTF-8");
			} catch (Exception e) {
				throw new RuntimeException("AES解密时发生异常。", e);
			}
		}
}
