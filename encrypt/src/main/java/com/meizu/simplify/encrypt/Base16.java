package com.meizu.simplify.encrypt;

// Base16和Base64Codec是由Codec拆分而来
public class Base16 {
	final static byte[] DIGITS = new byte[64];

	// 以下是password签名加密相关
	static {
		for (int i = 0; i < 10; i++) {
			DIGITS[i] = (byte) ('0' + i);
		}
		for (int i = 10; i < 36; i++) {
			DIGITS[i] = (byte) ('a' + i - 10);
		}
		for (int i = 36; i < 62; i++) {
			DIGITS[i] = (byte) ('A' + i - 36);
		}
		DIGITS[62] = '_';
		DIGITS[63] = '-';
	}

	public static String encode16ToString(byte[] bytes) {
		if (bytes == null || bytes.length == 0)
			return null;
		char[] encodedChars = encode16(bytes);
		return new String(encodedChars);
	}

	private static char[] encode16(byte[] data) {
		if (data == null || data.length == 0)
			return new char[0];
		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = (char) DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = (char) DIGITS[0x0F & data[i]];
		}
		return out;
	}

	static byte[] decode16(String hex) {
		if (hex == null || hex.isEmpty())
			return new byte[0];
		char[] data = hex.toCharArray();
		int len = data.length;

		if ((len & 0x01) != 0) {
			throw new IllegalArgumentException("Odd number of characters.");
		}

		byte[] out = new byte[len >> 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = Character.digit(data[j], 16) << 4;
			j++;
			f = f | Character.digit(data[j], 16);
			j++;
			out[i] = (byte) (f & 0xFF);
		}
		return out;
	}

}
