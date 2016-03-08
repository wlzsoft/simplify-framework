package com.meizu.simplify.encrypt.des;

/*
 * @function: 字符串加密算法，目前系统中，仅用到了第三种加密算法——随机加密算法
 * @用法：
 * 1、定义encdec对象
 * 2、调用setSrc，设置需要加密的字符串
 * 3、调用setEncryptMethod，设置加密算法，这里设置为3
 * 4、调用Encode得到加密结果
 * 5、测试代码见文件末尾main函数，打开注释即可测试
 */
import java.util.*;
import javax.crypto.*;

public class DxEncrypt {
	private String m_strSrc = new String();

	private int m_nEncryptMethod;

	private SecretKey m_keyDES;

	private byte[] m_byteRand;

	private static final List<Character> HEX_CHAR_LIST;
	static {
		HEX_CHAR_LIST = new ArrayList<Character>();
		HEX_CHAR_LIST.add(new Character('0'));
		HEX_CHAR_LIST.add(new Character('1'));
		HEX_CHAR_LIST.add(new Character('2'));
		HEX_CHAR_LIST.add(new Character('3'));
		HEX_CHAR_LIST.add(new Character('4'));
		HEX_CHAR_LIST.add(new Character('5'));
		HEX_CHAR_LIST.add(new Character('6'));
		HEX_CHAR_LIST.add(new Character('7'));
		HEX_CHAR_LIST.add(new Character('8'));
		HEX_CHAR_LIST.add(new Character('9'));
		HEX_CHAR_LIST.add(new Character('A'));
		HEX_CHAR_LIST.add(new Character('B'));
		HEX_CHAR_LIST.add(new Character('C'));
		HEX_CHAR_LIST.add(new Character('D'));
		HEX_CHAR_LIST.add(new Character('E'));
		HEX_CHAR_LIST.add(new Character('F'));
	}

	public DxEncrypt() {
		Date dt = new Date();
		Random rand = new Random(dt.getTime());
		m_byteRand = new byte[8];
		int n = 0;
		while ( n % 10 == 0 ) {
			n = rand.nextInt();
		}
		if (n < 0) {
			n = -n;
		}
		String strRand = String.valueOf(n);
		while ( strRand.length() < 8 ) {
			strRand = "0" + strRand;
		}
		for ( int i = 0; i < 8; i++ ) {
			m_byteRand[i] = strRand.getBytes()[i];
		}
	}

	public DxEncrypt(int nEncryptMethod) {
		this();
		setEncryptMethod(nEncryptMethod);
	}

	private String getSrc() {
		return m_strSrc;
	}

	public void setSrc(String strSrc) {
		m_strSrc = strSrc;
	}

	private int getEncryptMethod() {
		return m_nEncryptMethod;
	}

	public void setEncryptMethod(int nEncryptMethod) {
		m_nEncryptMethod = nEncryptMethod;
	}

	private String Bytes2Hex(byte[] b) {
		String hs = "";
		String strtmp = "";
		for ( int n = 0; n < b.length; n++ ) {
			strtmp = (java.lang.Integer.toHexString(b[n] & 0xFF));
			if (strtmp.length() == 1) {
				hs = hs + "0" + strtmp;
			} else {
				hs = hs + strtmp;
			}
		}
		return hs.toUpperCase();
	}

	private byte Hex2Byte(String s) {
		int high = HEX_CHAR_LIST.indexOf(new Character(s.charAt(0))) << 4;
		int low = HEX_CHAR_LIST.indexOf(new Character(s.charAt(1)));

		return (byte) (high + low);
	}

	private byte[] Hex2Bytes(String hex) {
		int len = hex.length() / 2;
		byte[] rtn = new byte[len];

		for ( int i = 0; i < len; i++ ) {
			rtn[i] = Hex2Byte(hex.substring(i * 2, i * 2 + 2));
		}
		return rtn;
	}

	private byte[] RandEncode(byte[] bySrc) {
		byte[] byResult = bySrc;
		for ( int n = 0; n < bySrc.length; n++ ) {
			byResult[n] += m_byteRand[m_byteRand.length - 1];
		}

		return byResult;
	}

	private String RandDecode(String strContent) {
		int nConLen = strContent.length();
		byte[] byKey = Hex2Bytes(strContent.substring(nConLen - 16, nConLen));
		int nKeyLen = byKey.length;
		byte[] byteResult = Hex2Bytes(strContent.substring(0, nConLen - 16));
		int nResultLen = byteResult.length;
		for ( int n = 0; n < nResultLen; n++ ) {
			byteResult[n] -= byKey[nKeyLen - 1];
		}
		return new String(byteResult);
	}

	public String getRand() {
		String strResult = new String(m_byteRand);
		return strResult;
	}

	public String Encode(String str) {
		this.m_strSrc = str;
		return Encode();
	}

	public String Encode() {
		String strContent = getSrc();
		int nEncodeMethod = getEncryptMethod();
		String strAlgorithm = "Blowfish";
		switch( nEncodeMethod ) {
			case 1: {
				strAlgorithm = "Blowfish";
				break;
			}
			case 2: {
				strAlgorithm = "DES";
				break;
			}
			case 3: {
				String strRandKey = new String(m_byteRand);
				String strResult = Bytes2Hex(RandEncode(strContent.getBytes()));
				String strKey = Bytes2Hex(strRandKey.getBytes());
				return (strResult + strKey);
			}
			default:
				break;
		}
		try {
			KeyGenerator keygen = KeyGenerator.getInstance(strAlgorithm);
			m_keyDES = keygen.generateKey();

			Cipher c1 = Cipher.getInstance(strAlgorithm);
			c1.init(Cipher.ENCRYPT_MODE, m_keyDES);
			byte[] cipherByte = c1.doFinal(strContent.getBytes());

			return Bytes2Hex(cipherByte);
		} catch ( java.security.NoSuchAlgorithmException e1 ) {
			e1.printStackTrace();
		} catch ( javax.crypto.NoSuchPaddingException e2 ) {
			e2.printStackTrace();
		} catch ( java.lang.Exception e3 ) {
			e3.printStackTrace();
		}
		return strContent;
	}

	public String getEncodeKey() {
		return Bytes2Hex(m_keyDES.toString().getBytes());
	}

	public String Decode(String strContent) {
		String strAlgorithm = "Blowfish";
		try {
			switch( getEncryptMethod() ) {
				case 1: {
					strAlgorithm = "Blowfish";
					break;
				}
				case 2: {
					strAlgorithm = "DES";
					break;
				}
				case 3: {
					return RandDecode(strContent);
				}
				default:
					break;
			}
			Cipher c1 = Cipher.getInstance(strAlgorithm);
			c1.init(Cipher.DECRYPT_MODE, m_keyDES);
			byte[] clearByte = c1.doFinal(Hex2Bytes(strContent));
			String strResult = new String(clearByte);
			return strResult;
		} catch ( Exception e1 ) {
			return null;
		}
	}
}
