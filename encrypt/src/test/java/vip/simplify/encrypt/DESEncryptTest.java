package vip.simplify.encrypt;

import org.junit.Test;

import vip.simplify.encrypt.base64.Base64Encrypt;
import vip.simplify.encrypt.symmetriccryptography.des.DESEncrypt;

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
	public  void testEncryptAndBase64ECB() {
		String encryptStr = DESEncrypt.encryptAndBase64ECB("tttt","sdferese", "utf-8");
		System.out.println(encryptStr);
		byte[] b = DESEncrypt.encryptECB("tttt".getBytes(),"sdferese".getBytes());
		System.out.println(new String(Base64Encrypt.encode(b)));
		System.out.println(DESEncrypt.base64AndDecryptECB(encryptStr, "sdferese", "utf-8"));
		String encryptStr2 = DESEncrypt.encryptAndBase64ECB("tttt","中文测试", "gbk");
		System.out.println(DESEncrypt.base64AndDecryptECB(encryptStr2, "中文测试", "gbk"));
		String encryptStr3 = DESEncrypt.encryptAndBase64ECB("tttt","中文st", "utf-8");
		System.out.println(DESEncrypt.base64AndDecryptECB(encryptStr3, "中文st", "utf-8"));
	}
	@Test
	public void testEncryptECB() {
		byte[] b = DESEncrypt.encryptECB("�ز�".getBytes(),"sdferese".getBytes());
		System.out.println(new String(DESEncrypt.decryptECB(b,"sdferese".getBytes())));
		String data = "{\"text\":\"哈哈哈哈，也\"}";
		String result =DESEncrypt.encryptToHexECB(data, "sdferese","utf-8");
		System.out.println(result);
		System.out.println(DESEncrypt.hexToDecryptECB(result, "sdferese", "utf-8"));
	}
	
	@Test
	public  void testEncryptAndBase64CBC() {
		String encryptStr = DESEncrypt.encryptAndBase64CBC("tttt","sdferese", "utf-8");
		System.out.println(encryptStr);
		byte[] b = DESEncrypt.encryptCBC("tttt".getBytes(),"sdferese".getBytes(),"sdferese".getBytes());
		System.out.println(new String(Base64Encrypt.encode(b)));
		System.out.println(DESEncrypt.base64AndDecryptCBC(encryptStr, "sdferese", "utf-8"));
		String encryptStr2 = DESEncrypt.encryptAndBase64CBC("tttt","中文测试", "gbk");
		System.out.println(DESEncrypt.base64AndDecryptCBC(encryptStr2, "中文测试", "gbk"));
		String encryptStr3 = DESEncrypt.encryptAndBase64CBC("tttt","中文st", "utf-8");
		System.out.println(DESEncrypt.base64AndDecryptCBC(encryptStr3, "中文st", "utf-8"));
	}
	@Test
	public void testEncryptCBC() {
		byte[] b = DESEncrypt.encryptCBC("�ز�".getBytes(),"sdferese".getBytes(),"sdferese".getBytes());
		System.out.println(new String(DESEncrypt.decryptCBC(b,"sdferese".getBytes(),"sdferese".getBytes())));
		String data = "{\"text\":\"哈哈哈哈，也\"}";
		String result =DESEncrypt.encryptToHexCBC(data, "sdferese","utf-8");
		System.out.println(result);
		System.out.println(DESEncrypt.hexToDecryptCBC(result, "sdferese", "utf-8"));
	}
}
