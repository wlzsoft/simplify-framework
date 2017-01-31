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
		String source = "哈哈哈哈，也";
		String my = dec.encryptECB(source);
		System.out.println(my);
		System.out.println(dec.decryptECB(my));
	}
	
	@Test
	public void ECBForSecretKey() {
		BlowfishVariantEncrypt dec = new BlowfishVariantEncrypt();
		String source = "哈哈哈哈，也";
		String my = dec.encryptECBForSecretKey(source);
		System.out.println(my);
		System.out.println(dec.decryptECBForSecretKey(my));
	}
}
