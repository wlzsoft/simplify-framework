package com.meizu.simplify.encrypt;

import java.nio.charset.Charset;

import com.meizu.simplify.encrypt.base64.Base64VariantEncrypt;
import com.meizu.simplify.encrypt.sign.md5.MD5Encrypt;
/**
 * <p><b>Title:</b><i>解密</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年9月17日 下午9:17:38</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年9月17日 下午9:17:38</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class Decrypt {
	final static Charset CHARSET = Charset.forName("UTF-8");

	// private static void print(String s)
	// {
	// System.out.printf("%1$tF%1$tT: %2$s\r\n", new java.util.Date(), s);
	// }

	/**
	 * 
	 * 方法用途: 数据表字段解密<br>
	 * 操作步骤: TODO<br>
	 * @param ciphertext
	 * @return
	 */
	public static String fieldDecrypt(String ciphertext) {
		if (ciphertext == null || ciphertext.length() < 3)
			return null;

		String cipherted = ciphertext.substring(2, ciphertext.length());
		byte[] bytes = Base64VariantEncrypt.decode64(cipherted);
		// print("密文：" + new String(bytes));

		String prefix = ciphertext.substring(0, 2);
		// print("前缀：" + prefix);
		byte[] conKey = Keys.calcConKey(prefix);
		// print("混淆秘钥：" + new String(conKey));
		// byte[] aesKey = NgKeys.calcAesKey(prefix);
		// print("加密秘钥：" + new String(aesKey));
		byte[] rc4Key = Keys.calcRc4Key(prefix);
		// print("ARC秘钥：" + new String(rc4Key));

		rc4crypt(bytes, rc4Key);
		// print("rc4明文：" + new String(bytes));
		deConfusion(bytes, conKey);
		// print("混淆明文：" + new String(bytes));
		return new String(bytes, CHARSET);
	}

	/**
	 * 
	 * 方法用途: 密码验证<br>
	 * 操作步骤: TODO<br>
	 * @param plaintext
	 * @param ciphertext
	 * @return
	 */
	public static boolean passwordVerify(String plaintext, String ciphertext) {
		if (plaintext == null || ciphertext == null)
			return false;
		String decrypted = fieldDecrypt(ciphertext);
		String md5 = MD5Encrypt.hashMd5(plaintext);
		return md5.equals(decrypted);
	}

	/**
	 * 
	 * 方法用途: 分组置换解密<br>
	 * 操作步骤: TODO<br>
	 * @param des
	 * @param keys
	 */
	public static void deConfusion(byte[] des, byte[] keys) {
		int keyLength = keys.length;
		int n = 0;
		for (n = 0; n < des.length; n += keyLength) {
			for (int i = 0; i < keyLength; ++i) {
				if ((n + i) < des.length) {
					des[n + i] = (byte) (des[n + i] - keyLength);
					des[n + i] = (byte) (des[n + i] ^ keys[i]);
					// if(des[n+i]==0)
					// {
					// des[n+i] = keys[i];
					// }
				} else {
					break;
				}
			}
		}
	}

	public static void rc4crypt(byte[] src, byte[] keys) {
		int x = 0;
		int y = 0;
		byte key[] = new byte[keys.length];
		System.arraycopy(keys, 0, key, 0, keys.length);
		int xorIndex;
		for (int i = 0; i < src.length; ++i) {
			x = (x + 1) & 0xff;
			y = ((key[x] & 0xff) + y) & 0xff;
			byte tmp = key[x];
			key[x] = key[y];
			key[y] = tmp;
			xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
			src[i] = (byte) (src[i] ^ key[xorIndex]);
		}
	}

	static int[] xorInt = Keys.numKey();

	/**
	 * 
	 * 方法用途: 数字解密<br>
	 * 操作步骤: TODO<br>
	 * @param num
	 * @return
	 */
	public static String numDecrypt(String num) {
		if (num == null || num.length() < 4)
			return num;
		// 校验码的十位数和各位数
		int first = num.charAt(0);
		int second = num.charAt(1);
		String midValue = "";
		if ((num.length() % 2) == 1) {
			midValue += num.charAt(num.length() - 1);
		}
		num = num.substring(2, num.length());

		int[] temp = new int[num.length() / 2];
		int[] tempresult = new int[num.length()];

		int differ = first > second ? (first - second) : (second - first);
		for (int i = 0; i < num.length() / 2; i++) {
			temp[i] = Integer.valueOf(num.substring(i * 2, (i * 2) + 2));

			temp[i] = temp[i] ^ xorInt[((differ) + i) % 4];

			tempresult[i] = temp[i] / 10;
			tempresult[tempresult.length - i - 1] = temp[i] % 10;
		}

		if (midValue.length() == 1) {
			tempresult[tempresult.length / 2] = Integer.valueOf(midValue);
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tempresult.length; i++) {
			sb.append(tempresult[i]);
		}
		sb.append((char) first);
		sb.append((char) second);
		return sb.toString();
	}

}
