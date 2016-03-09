package com.meizu.simplify.encrypt.des;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
/*
 * @function: 字符串加密算法，目前系统中，仅用到了第三种加密算法——随机加密算法
 * @用法：
 * 1、定义encdec对象
 * 2、调用setSrc，设置需要加密的字符串
 * 3、调用setEncryptMethod，设置加密算法，这里设置为3
 * 4、调用Encode得到加密结果
 * 5、测试代码见文件末尾main函数，打开注释即可测试
 */
import java.util.Date;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.meizu.simplify.encrypt.ByteHexUtil;

public class DxEncrypt {
	private String m_strSrc = new String();

	private int m_nEncryptMethod;

	private SecretKey m_keyDES;

	private byte[] m_byteRand;


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



	private byte[] RandEncode(byte[] bySrc) {
		byte[] byResult = bySrc;
		for ( int n = 0; n < bySrc.length; n++ ) {
			byResult[n] += m_byteRand[m_byteRand.length - 1];
		}

		return byResult;
	}

	private String RandDecode(String strContent) {
		int nConLen = strContent.length();
		byte[] byKey = ByteHexUtil.hex2Bytes(strContent.substring(nConLen - 16, nConLen));
		int nKeyLen = byKey.length;
		byte[] byteResult = ByteHexUtil.hex2Bytes(strContent.substring(0, nConLen - 16));
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
				String strResult = ByteHexUtil.bytes2Hex(RandEncode(strContent.getBytes()));
				String strKey = ByteHexUtil.bytes2Hex(strRandKey.getBytes());
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

			return ByteHexUtil.bytes2Hex(cipherByte);
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
		return ByteHexUtil.bytes2Hex(m_keyDES.toString().getBytes());
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
			byte[] clearByte = c1.doFinal(ByteHexUtil.hex2Bytes(strContent));
			String strResult = new String(clearByte);
			return strResult;
		} catch ( Exception e1 ) {
			return null;
		}
	}
	
	public static void main(String[] args) {
		DxEncrypt dec = new DxEncrypt(2);
		String my = dec.Encode("哈哈哈哈，也");
		System.out.println(my);
		System.out.println(dec.Decode(my));
//		String re = DESMessageEncrypt.encrypt(my, "sdferese");
//		System.out.println(re);
//		System.out.println(DESMessageEncrypt.decrypt(re,  "sdferese"));
		
	}
}
