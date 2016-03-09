package com.meizu.simplify.encrypt;

import org.junit.Test;

import com.meizu.simplify.encrypt.des.CoverMessageEncrypt;
import com.meizu.simplify.encrypt.des.RandEncrypt;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 下午5:09:06</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月9日 下午5:09:06</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class CoverEncryptTest {
	
	@Test
	public void test() {
		 CoverMessageEncrypt encrypt = new CoverMessageEncrypt();
		 
		 String str = new String(encrypt.encode("2212".getBytes()));
		 System.out.println(str);
		 System.out.println(encrypt.decode(str));
	}
}
