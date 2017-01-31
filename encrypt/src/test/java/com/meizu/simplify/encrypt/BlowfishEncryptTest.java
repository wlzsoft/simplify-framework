package com.meizu.simplify.encrypt;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.meizu.simplify.encrypt.symmetriccryptography.blowfish.BlowfishEncrypt;

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
public class BlowfishEncryptTest {
	@Test
	public void test() throws UnsupportedEncodingException {
		String source = "哈哈哈哈，也";
		byte[] key = "owiueroweuroweir".getBytes("utf-8");
		byte[] my = BlowfishEncrypt.encryptECB(source.getBytes("utf-8"), key);
		System.out.println(new String(BlowfishEncrypt.decryptECB(my, key),"utf-8"));
		
		
	}
}
