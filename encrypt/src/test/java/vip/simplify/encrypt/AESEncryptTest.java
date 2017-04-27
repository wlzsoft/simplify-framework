package vip.simplify.encrypt;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import vip.simplify.encrypt.symmetriccryptography.aes.AESEncrypt;

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
public class AESEncryptTest {
	@Test
	public void testECB() throws UnsupportedEncodingException {
		String data = "哈哈哈哈，也";
		byte[] key = "owiueroweuroweir".getBytes("utf-8");
		byte[] encryptStr = AESEncrypt.encryptECB(data.getBytes("utf-8"), key);
		System.out.println(new String(AESEncrypt.decryptECB(encryptStr, key),"utf-8"));
		
	}
	@Test
	public void testBase64AndECB() throws UnsupportedEncodingException {
		String data = "哈哈哈哈，也";
		String key = "owiueroweuroweir";
		String encryptStr = AESEncrypt.encryptAndBase64CBC(data, key,"utf-8");
		System.out.println(AESEncrypt.base64AndDecryptCBC(encryptStr, key,"utf-8"));
		
	}
}
