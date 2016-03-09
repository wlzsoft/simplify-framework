package com.meizu.simplify.encrypt;

import java.lang.reflect.Array;

import org.junit.Test;

import com.meizu.simplify.encrypt.sign.md5.MD5;
import com.meizu.simplify.encrypt.sign.md5.Md5Encrypt;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月12日 下午12:39:28</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月12日 下午12:39:28</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class MD5EncryptTest {
	@Test
	public  void md5() {
		System.out.println(Md5Encrypt.sign(1));
		System.out.println(Md5Encrypt.sign("api_key=1a90a2bf034049f39d5c41d040b0ff54call_id=1253782990268format=XMLid=2method=share.publishsession_key=2.8531c1a354d387b07a4984ae50fabd4c.3600.1253790000-261912373share_date={\"link\":\"http://mininurse.renren.com\",\"pic\":\"\",\"title\":\"小护士\",\"sumary\":\"\",\"comment\":\"gool\"}type=6uid=261912373v=1.094601c5cddab4da0b7bf81f68d50c2d7"));
		System.out.println(Md5Encrypt.sign(Md5Encrypt.sign("1"+"1"+"cubs361")+"sales.cubs").substring(3, 23));
	}
	@Test
	public void md5Custom1() {
		//第一个测试案例
		MD5 m = new MD5();
		System.out.println("MD5 Test suite:");
		System.out.println("MD5(\"super\"):" + m.md5("偷"));
		System.out.println("MD5(\"super\"):" + m.md5("海"));
		System.out.println("MD5(\"abc\"):" + m.md5("abc"));
		System.out.println("MD5(\"message digest\"):"+ m.md5("message digest"));
		System.out.println("MD5(\"abcdefghijklmnopqrstuvwxyz\"):"
							+ m.md5("abcdefghijklmnopqrstuvwxyz"));
		System.out.println("MD5(\"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\"):"
							+ m.md5("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"));
				
		System.out.println("MD5 Test suite:");
		System.out.println("MD5(\"super\"):" + Md5Encrypt.sign("偷"));
		System.out.println("MD5(\"super\"):" + Md5Encrypt.sign("海"));
		System.out.println("MD5(\"abc\"):" + Md5Encrypt.sign("abc"));
		
		//注意：中文情况下 MD5.getMD5String 和Md5Util.md5 签名结果不一致
				
				
	}
	@Test
	public void md5Custom2() {
		//第二测试案例
		
		/*MD5 md5 = new MD5();
		String md5str = md5.getMD5ofStr("123456");
		Random random = new Random();
		byte[] chars = new byte[10];
		for(int i = 0; i < 10; i++) {
			int index = random.nextInt(md5str.length());
			chars[i] = (byte) md5str.charAt(index);
		}
		System.out.println(new String(chars));*/
	}
}
