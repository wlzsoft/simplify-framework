package com.meizu.simplify.encrypt;

import com.meizu.simplify.encrypt.sign.md5.MD5Encrypt;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月10日 下午1:54:19</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月10日 下午1:54:19</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class PasswordEncrypt {
	/**
	 * 
	 * 方法用途: 密码验证<br>
	 * 操作步骤: TODO<br>
	 * @param plaintext
	 * @param ciphertext
	 * @return
	 */
	public static boolean passwordVerify(String plaintext, String ciphertext) {
		if (plaintext == null || ciphertext == null) {
			return false;
		}
		String decrypted = Decrypt.fieldDecrypt(ciphertext);
		String md5 = MD5Encrypt.hashMd5(plaintext);
		return md5.equals(decrypted);
	}
	
	/**
	 * 
	 * 方法用途: 数据表密码加密<br>
	 * 操作步骤: TODO<br>
	 * @param plaintext
	 * @return
	 */
	public static String passwordEncrypt(String plaintext) {
		if (plaintext == null) {
			return plaintext;
		}
		String md5 = MD5Encrypt.hashMd5(plaintext);
		return Encrypt.fieldEncrypt("password", md5);
	}
}
