package com.meizu.simplify.encrypt.des;

import java.util.Date;
import java.util.Random;

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
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class RandEncrypt {

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

	public String randDecode(String strContent) {
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

	public String encode(String strContent) {
		String strRandKey = new String(m_byteRand);
		String strResult = ByteHexUtil.bytes2Hex(randEncode(strContent.getBytes()));
		String strKey = ByteHexUtil.bytes2Hex(strRandKey.getBytes());
		return (strResult + strKey);
	}
	
}
