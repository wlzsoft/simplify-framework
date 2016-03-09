package com.meizu.simplify.encrypt.des;

import java.util.Date;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.meizu.simplify.encrypt.ByteHexUtil;

/**
 * <p><b>Title:</b><i>BlowFish对称加密算法</i></p>
 * <p>Desc: 可用来加密64比特长度的字符串。32位处理器诞生后，Blowfish算法因其在加密速度上超越了DES而引起人们的关注。Blowfish算法具有加密速度快、紧凑、密钥长度可变、可免费使用等特点</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 下午5:10:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月9日 下午5:10:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class Blowfish {

	private SecretKey m_keyDES;

	private byte[] m_byteRand;


	public Blowfish() {
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


	public String getRand() {
		String strResult = new String(m_byteRand);
		return strResult;
	}


	public String encode(String strContent) {
		String strAlgorithm = "Blowfish";
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
		try {
			Cipher c1 = Cipher.getInstance(strAlgorithm);
			c1.init(Cipher.DECRYPT_MODE, m_keyDES);
			byte[] clearByte = c1.doFinal(ByteHexUtil.hex2Bytes(strContent));
			String strResult = new String(clearByte);
			return strResult;
		} catch ( Exception e1 ) {
			return null;
		}
	}
	
	
}
