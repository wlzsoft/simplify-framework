package com.meizu.simplify.encrypt;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.meizu.simplify.encrypt.base64.Base64VariantEncrypt;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月10日 下午2:33:40</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月10日 下午2:33:40</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class FieldEncrypt {
final static Map<String, String> Prefix = new ConcurrentHashMap<String, String>();
	
	
	
	static String getPrefix(String fname) {
		if (fname == null || fname.length() < 2) {
			return "fi";
		}
		return Prefix.get(fname);
	}

	/**
	 * 
	 * 方法用途: 通过字段名称计算前缀<br>
	 * 操作步骤: TODO<br>
	 * @param fname
	 * @return
	 */
	static String calcPrefix(String fname) {
		int pre = 0;
		if (fname == null || fname.length() < 2) {
			pre = 'f' + 'i';
		} else if (Prefix.containsKey(fname)) {
			return Prefix.get(fname);
		} else {
			pre = fname.charAt(0) + fname.charAt(1);
		}
		char[] buf = new char[2];
		buf[0] = (char) Base64VariantEncrypt.encodingTable[(0xF0 & pre) >>> 4];
		buf[1] = (char) Base64VariantEncrypt.encodingTable[0x0F & pre];
		String prefix = new String(buf);
		if (!Prefix.containsKey(fname)) {
			Prefix.put(fname, prefix);
		}
		return prefix;
	}
	
	
	

	/**
	 * 
	 * 方法用途: 数据表字段加密<br>
	 * 操作步骤: TODO<br>
	 * @param name
	 * @param value
	 * @return
	 */
	public static String fieldEncrypt(String name, String value) {
		if (name == null || value == null)
			return null;

		byte[] bytes = value.getBytes();

		String prefix = calcPrefix(name);
		// print("前缀：" + prefix);
		byte[] conKey = Keys.calcConKey(prefix);
		// print("混淆秘钥：" + new String(conKey));
		// byte[] aesKey = NgKeys.calcAesKey(prefix);
		// print("加密秘钥：" + new String(aesKey));
		byte[] rc4Key = Keys.calcRc4Key(prefix);
		// print("ARC秘钥：" + new String(rc4Key));

		// print("明文：" + value);
		Encrypt.enConfusion(bytes, conKey);
		// print("混淆密文：" + new String(bytes));
		Encrypt.rc4crypt(bytes, rc4Key);
		// print("rc4密文：" + new String(bytes));
		return prefix + Base64VariantEncrypt.encode64String(bytes);
	}
}
