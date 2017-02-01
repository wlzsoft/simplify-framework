package com.meizu.simplify.encrypt;

import org.junit.Test;

import com.meizu.simplify.encrypt.symmetriccryptography.blowfish.BlowfishVariantEncrypt;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 下午5:09:45</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月9日 下午5:09:45</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class BlowfishVariantEncryptTest {
	@Test
	public void ECB() {
		BlowfishVariantEncrypt dec = new BlowfishVariantEncrypt();
		String data = "哈哈哈哈，也";
		String encryptStr = dec.encryptECB(data);
		System.out.println(encryptStr);
		System.out.println(dec.decryptECB(encryptStr));
	}
	
	@Test
	public void ECBForSecretKey() {
		BlowfishVariantEncrypt dec = new BlowfishVariantEncrypt();
		String data = "哈哈哈哈，也";
		String encryptStr = dec.encryptECBForSecretKey(data);
		System.out.println(encryptStr);
		System.out.println(dec.decryptECBForSecretKey(encryptStr));
	}
}
