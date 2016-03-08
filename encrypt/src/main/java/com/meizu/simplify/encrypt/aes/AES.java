package com.meizu.simplify.encrypt.aes;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.meizu.simplify.encrypt.base64.Base64;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月8日 下午4:04:27</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月8日 下午4:04:27</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */

public class AES {
	
		/**
		 * 
		 * 方法用途: AES加密<br>
		 * 操作步骤: TODO<br>
		 * @param content 待加密的内容
		 * @param password 密码
		 * @return 返回加密后的内容
		 */
		public static String aesEncrypt(String content, String password) {
			try {
				byte[] raw = password.getBytes("UTF-8");
				SecretKeySpec key = new SecretKeySpec(raw, "AES");
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, key);
				byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
				Base64 encoder = new Base64();
				return encoder.encodeToString(encrypted);
			} catch (Exception e) {
				throw new RuntimeException("AES加密时发生异常。", e);
			}
		}
	
		/**
		 * 
		 * 方法用途: AES解密<br>
		 * 操作步骤: TODO<br>
		 * @param content 待解密的内容
		 * @param password 密码
		 * @return 返回解密后的内容
		 */
		public static String aesDecrypt(String content, String password) {
			try {
				byte[] raw = password.getBytes("UTF-8");
				SecretKeySpec key = new SecretKeySpec(raw, "AES");
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, key);
				byte[] encrypted = new Base64().decode(content);
				byte[] original = cipher.doFinal(encrypted);
				return new String(original, "UTF-8");
			} catch (Exception e) {
				throw new RuntimeException("AES解密时发生异常。", e);
			}
		}
}
