package com.meizu.simplify.encrypt.des;

import java.util.Date;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.meizu.simplify.encrypt.ByteHexUtil;

/**
 * <p><b>Title:</b><i>字符串加密算法-随机加密算法</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 下午3:41:21</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月9日 下午3:41:21</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class RandEncrypt {

	private SecretKey m_keyDES;

	private byte[] m_byteRand;


	public RandEncrypt() {
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

	private byte[] randEncode(byte[] bySrc) {
		byte[] byResult = bySrc;
		for ( int n = 0; n < bySrc.length; n++ ) {
			byResult[n] += m_byteRand[m_byteRand.length - 1];
		}

		return byResult;
	}

	private String randDecode(String strContent) {
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


	public String encode(String strContent) {
		int nEncodeMethod = 3;
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
				String strResult = ByteHexUtil.bytes2Hex(randEncode(strContent.getBytes()));
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


	public String decode(String strContent) {
		String strAlgorithm = "Blowfish";
		int nEncodeMethod = 3;
		try {
			switch( nEncodeMethod ) {
				case 1: {
					strAlgorithm = "Blowfish";
					break;
				}
				case 3: {
					return randDecode(strContent);
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
		RandEncrypt dec = new RandEncrypt();
		String source = "哈哈哈哈，也";
		String my = dec.encode(source);
		System.out.println(my);
		System.out.println(dec.decode(my));
		
		
	}
}
