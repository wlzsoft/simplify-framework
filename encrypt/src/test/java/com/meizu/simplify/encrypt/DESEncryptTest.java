package com.meizu.simplify.encrypt;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

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
	
	public static void main(String[] args) {
		try {
			System.out.println(DESEncrypt.DESAndBase64Encrypt("tttt","sdferese", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] b = DESEncrypt.encrypt("tttt".getBytes(),"sdferese".getBytes(),null);
		System.out.println(new String(b));
	}
	@Test
	public void test() {
		byte[] b = DESEncrypt.encrypt("�ز�".getBytes(),"sdferese".getBytes(),null);
		System.out.println(new String(b));
		System.out.println(new String(DESEncrypt.decrypt(b,"sdferese".getBytes(),null)));
		
		String source = "{\"text\":\"哈哈哈哈，也\"}";
//		String source = "�ز�";
		String re =DESEncrypt.encrypt(source, "sdferese");
		System.out.println(re);
		System.out.println(DESEncrypt.decrypt(re,  "sdferese","UTF-8"));
	}
	
	public static void DES(String[] args) {
		try {
			String encryptString = DESEncrypt.DESAndBase64Encrypt("meizu&123456", "meizuall", "utf-8");
			System.out.println(encryptString+"||||||||||||||");
			String decryptString = DESEncrypt.DESAndBase64Decrypt(encryptString, "meizuall", "utf-8");
			System.out.println(decryptString+"////////////");
			
//			String token=encrypt("meizu&123456","meizuall");
			String token64 = DESEncrypt.encrypt64("meizu&123456","meizuall");
			token64 = DESEncrypt.encrypt64("meizu&123456","meizuall");
			System.out.println(token64);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
