package com.meizu.simplify.encrypt;

import org.junit.Test;

import com.meizu.simplify.encrypt.des.DESMessageEncrypt;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 下午5:00:54</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月9日 下午5:00:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class DesUtilTest {
	@Test
	public void test() {
		byte[] b = new DESMessageEncrypt().encode("�ز�".getBytes(),"sdferese".getBytes());
		System.out.println(new String(b));
		System.out.println(new String(new DESMessageEncrypt().decode(b,"sdferese".getBytes())));
		
		String source = "{\"text\":\"哈哈哈哈，也\"}";
//		String source = "�ز�";
		String re =DESMessageEncrypt.encrypt(source, "sdferese");
		System.out.println(re);
		System.out.println(DESMessageEncrypt.decrypt(re,  "sdferese"));
	}
}
