package com.meizu.simplify.encrypt;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.meizu.simplify.encrypt.base64.Base64Encrypt;
import com.meizu.simplify.encrypt.des.DESEncrypt;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 下午5:00:54</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月9日 下午5:00:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class DESEncryptTest {
	
	@Test
	public  void testEncryptAndBase64() {
		try {
			String encryptStr = DESEncrypt.encryptAndBase64("tttt","sdferese", "utf-8",true);
			System.out.println(encryptStr);
			byte[] b = DESEncrypt.encrypt("tttt".getBytes(),"sdferese".getBytes(),null);
			System.out.println(new String(Base64Encrypt.encode(b)));
			System.out.println(DESEncrypt.base64AndDecrypt(encryptStr, "sdferese", "utf-8",true));
			String encryptStr2 = DESEncrypt.encryptAndBase64("tttt","中文测试", "gbk",true);
			System.out.println(DESEncrypt.base64AndDecrypt(encryptStr2, "中文测试", "gbk",true));
			String encryptStr3 = DESEncrypt.encryptAndBase64("tttt","中文st", "utf-8",true);
			System.out.println(DESEncrypt.base64AndDecrypt(encryptStr3, "中文st", "utf-8",true));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testEncrypt() {
		byte[] b = DESEncrypt.encrypt("�ز�".getBytes(),"sdferese".getBytes(),null);
		System.out.println(new String(DESEncrypt.decrypt(b,"sdferese".getBytes(),null)));
		String source = "{\"text\":\"哈哈哈哈，也\"}";
		String re =DESEncrypt.encryptToHex(source, "sdferese","utf-8",false);
		System.out.println(re);
		System.out.println(DESEncrypt.hexToDecrypt(re, "sdferese", "utf-8", false));
	}
}
