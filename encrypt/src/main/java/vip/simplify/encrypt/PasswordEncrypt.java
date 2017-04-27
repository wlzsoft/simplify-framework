package vip.simplify.encrypt;

import vip.simplify.encrypt.sign.md5.MD5Encrypt;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月10日 下午1:54:19</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月10日 下午1:54:19</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class PasswordEncrypt {
	/**
	 * 
	 * 方法用途: 密码验证<br>
	 * 操作步骤: TODO<br>
	 * @param password 待验证密码
	 * @param passwordEncrtyed 已加密过的密码
	 * @return
	 */
	public static boolean passwordVerify(String password, String passwordEncrtyed) {
		if (password == null || passwordEncrtyed == null) {
			return false;
		}
		String decrypted = Decrypt.fieldDecrypt(passwordEncrtyed);
		String md5 = MD5Encrypt.hashMd5(password);
		return md5.equals(decrypted);
	}
	
	/**
	 * 
	 * 方法用途: 数据表密码加密<br>
	 * 操作步骤: TODO<br>
	 * @param password 待加密密码
	 * @return
	 */
	public static String passwordEncrypt(String password) {
		if (password == null) {
			return password;
		}
		String md5 = MD5Encrypt.hashMd5(password);
		return FieldEncrypt.fieldEncrypt("password", md5);
	}
}
