package com.meizu.simplify.encrypt;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.meizu.simplify.encrypt.base64.Base64Codec;
/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年9月17日 下午9:18:51</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年9月17日 下午9:18:51</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public final class Keys {
	//NO(手机产品编码 )和业务相关，比如手机产品编码 默认规定8个字节，可以根据业务修改长度
	final static byte[] NO = new byte[8];
	//key(秘钥)和业务相关，默认规定14位，可以根据业务修改长度
	final static byte[] key = new byte[14];

	final static Map<String, String> Prefix = new ConcurrentHashMap<String, String>();
	final static Map<String, byte[]> ConKey = new ConcurrentHashMap<String, byte[]>();
	final static Map<String, byte[]> AesKey = new ConcurrentHashMap<String, byte[]>();
	final static Map<String, byte[]> Rc4Key = new ConcurrentHashMap<String, byte[]>();

	public static void setNo(String no) {
		if (no == null || no.isEmpty())
			return;
		no = Encrypt.numEncrypt(no);
		byte[] bytes = no.getBytes();
		System.arraycopy(bytes, 0, NO, 0, bytes.length);
	}

	public static void setKey(String keystr) {
		if (keystr == null || keystr.isEmpty())
			return;
		byte[] bytes = keystr.getBytes();
		int keyLength = bytes.length;
		if (keyLength >= 14) {
			System.arraycopy(bytes, 0, key, 0, 14);
		} else {
			System.arraycopy(bytes, 0, key, 14 - keyLength, keyLength);
		}
	}

	static String getPrefix(String fname) {
		if (fname == null || fname.length() < 2)
			return "fi";
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
		buf[0] = (char) Base64Codec.encodingTable[(0xF0 & pre) >>> 4];
		buf[1] = (char) Base64Codec.encodingTable[0x0F & pre];
		String prefix = new String(buf);
		if (!Prefix.containsKey(fname)) {
			Prefix.put(fname, prefix);
		}
		return prefix;
	}

	/**
	 * 
	 * 方法用途: 组合192位加密秘钥<br>
	 * 操作步骤: TODO<br>
	 * @param prefix
	 * @return
	 */
	static byte[] calcAesKey(String prefix) {
		if (AesKey.containsKey(prefix)) {
			return AesKey.get(prefix);
		}
		byte[] bytes = new byte[24];
		bytes[0] = (byte) prefix.charAt(0);
		bytes[1] = (byte) prefix.charAt(1);
		byte[] cinemaNX = new byte[8];
		for (int i = 0; i < 4; ++i) {
			cinemaNX[i] = (byte) (NO[7 - i] ^ bytes[0]);
			cinemaNX[++i] = (byte) (NO[7 - i] ^ bytes[1]);
		}
		System.arraycopy(cinemaNX, 0, bytes, 2, 8);
		System.arraycopy(key, 0, bytes, 10, 14);
		AesKey.put(prefix, bytes);
		return bytes;
	}

	static byte[] calcAesKey(String prefix, byte[] key) {
		if (key == null) {
			return calcAesKey(prefix);
		}
		byte[] bytes = new byte[24];
		bytes[0] = (byte) prefix.charAt(0);
		bytes[1] = (byte) prefix.charAt(1);
		byte[] cinemaNX = new byte[8];
		for (int i = 0; i < 4; ++i) {
			cinemaNX[i] = (byte) (NO[7 - i] ^ bytes[0]);
			cinemaNX[++i] = (byte) (NO[7 - i] ^ bytes[1]);
		}
		System.arraycopy(cinemaNX, 0, bytes, 2, 8);
		System.arraycopy(key, 0, bytes, 10, key.length);
		return bytes;
	}

	/**
	 * 
	 * 方法用途: 组合192位混淆秘钥<br>
	 * 操作步骤: TODO<br>
	 * @param prefix
	 * @return
	 */
	static byte[] calcConKey(String prefix) {
		if (ConKey.containsKey(prefix)) {
			return ConKey.get(prefix);
		}
		byte[] bytes = new byte[24];
		bytes[0] = (byte) prefix.charAt(0);
		bytes[1] = (byte) prefix.charAt(1);
		System.arraycopy(NO, 0, bytes, 2, 8);
		System.arraycopy(key, 0, bytes, 10, 14);
		ConKey.put(prefix, bytes);
		return bytes;
	}

	static byte[] calcConKey(String prefix, byte[] key) {
		if (key == null) {
			return calcConKey(prefix);
		}
		byte[] bytes = new byte[24];
		bytes[0] = (byte) prefix.charAt(0);
		bytes[1] = (byte) prefix.charAt(1);
		System.arraycopy(NO, 0, bytes, 2, 8);
		System.arraycopy(key, 0, bytes, 10, key.length);
		return bytes;
	}

	/**
	 * 
	 * 方法用途: 组合256位加密秘钥<br>
	 * 操作步骤: TODO<br>
	 * @param prefix
	 * @return
	 */
	static byte[] calcRc4Key(String prefix) {
		if (Rc4Key.containsKey(prefix)) {
			return Rc4Key.get(prefix);
		}
		byte[] bytes = calcAesKey(prefix);

		byte state[] = new byte[256];

		for (int i = 0; i < 256; i++) {
			state[i] = (byte) i;
		}
		int index1 = 0;
		int index2 = 0;
		for (int i = 0; i < 256; i++) {
			index2 = ((bytes[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
			byte tmp = state[i];
			state[i] = state[index2];
			state[index2] = tmp;
			index1 = (index1 + 1) % 24;
		}

		Rc4Key.put(prefix, state);
		return state;
	}

	static byte[] calcRc4Key(String prefix, byte[] key) {
		if (key == null) {
			return calcRc4Key(prefix);
		}
		byte[] bytes = calcAesKey(prefix, key);

		byte state[] = new byte[256];

		for (int i = 0; i < 256; i++) {
			state[i] = (byte) i;
		}
		int index1 = 0;
		int index2 = 0;
		for (int i = 0; i < 256; i++) {
			index2 = ((bytes[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
			byte tmp = state[i];
			state[i] = state[index2];
			state[index2] = tmp;
			index1 = (index1 + 1) % 24;
		}

		return state;
	}

	/**
	 * 
	 * 方法用途: 文件加密默认密码ros!&^*$sse<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static byte[] defaultFileKey() {
		return new byte[] { 114,111,115,33,38,94,42,36,115,115,101 };
	}

	/**
	 * 
	 * 方法用途: 数字加密密钥<br>
	 * 操作步骤: TODO<br>
	 * @return 异或表,(0--3的值异或，可以确保值2位数的值异或后，不会超过99)
	 */
	static int[] numKey() {
		return new int[] { 0, 3, 2, 1 };
	}
}
