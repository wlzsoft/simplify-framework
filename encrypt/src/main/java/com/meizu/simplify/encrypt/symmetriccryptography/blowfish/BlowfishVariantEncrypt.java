package com.meizu.simplify.encrypt.symmetriccryptography.blowfish;

import java.util.Date;
import java.util.Random;

import javax.crypto.SecretKey;

import com.meizu.simplify.encrypt.symmetriccryptography.SymmetricBaseEncrypt;

/**
 * <p><b>Title:</b><i>随机加密算法</i></p>
 * <p>Desc: 基于BlowFish对称加密算法的随机加密算法</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 下午5:10:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月9日 下午5:10:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class BlowfishVariantEncrypt {

	private String strResult;
	
	private SecretKey secretKey;

	public BlowfishVariantEncrypt() {
		Date dt = new Date();
		Random rand = new Random(dt.getTime());
		byte[] byteRand = new byte[8];
		int n = 0;
		while (n % 10 == 0) {
			n = rand.nextInt();
		}
		if (n < 0) {
			n = -n;
		}
		String strRand = String.valueOf(n);
		while (strRand.length() < 8) {
			strRand = "0" + strRand;
		}
		for (int i = 0; i < 8; i++) {
			byteRand[i] = strRand.getBytes()[i];
		}
		strResult = new String(byteRand);
		secretKey = SymmetricBaseEncrypt.getSecretKey(null, "Blowfish");
	}

	public  String encryptECB(String data) {
		String cipher = SymmetricBaseEncrypt.encryptAndBase64(data, strResult, "utf-8", false, "Blowfish", "ECB", "PKCS5Padding",false);
		return cipher;
	}
	
	public  String decryptECB(String data) {
		String cipher = SymmetricBaseEncrypt.base64AndDecrypt(data, strResult, "utf-8", false, "Blowfish", "ECB", "PKCS5Padding",false);
		return cipher;
	}
	
//	第二种方式
	
	
	public  String encryptECBForSecretKey(String data) {
		String cipher = SymmetricBaseEncrypt.encryptAndBase64(data, secretKey, "utf-8", false, "Blowfish", "ECB", "PKCS5Padding");
		return cipher;
	}
	
	public  String decryptECBForSecretKey(String data) {
		String cipher = SymmetricBaseEncrypt.base64AndDecrypt(data, secretKey, "utf-8", false, "Blowfish", "ECB", "PKCS5Padding");
		return cipher;
	}
	
}
