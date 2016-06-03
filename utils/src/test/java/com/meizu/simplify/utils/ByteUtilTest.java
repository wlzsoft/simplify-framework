package com.meizu.simplify.utils;

import org.junit.Test;

/**
  * <p><b>Title:</b><i>二进制数据压缩转换</i></p>
 * <p>Desc: 把字符串的16进制数据转成byte数据中的16进制数据</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年6月3日 下午5:48:00</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年6月3日 下午5:48:00</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class ByteUtilTest {
	
	@Test
	public void test(){
		String source = "02006024048120C09011196227000731470343016031000010009640060210000899999999481B1B07DD445C8CDB91E23F0A16977847B3FEBE257955E20E3130383030303232313038303032323531333732363236313536C7B787452FD446B50011010000010040";
		byte[] resultByte = new byte[source.length()/2];
		for(int i=0; i<resultByte.length; i++){
			try {
				int bb = Integer.parseInt(source.substring(i*2, i*2+2),16);
				resultByte[i] = (byte) bb;
//				System.out.println(bb);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(new String(resultByte));
	}
	@Test
	public void test2(){
		byte a = 0x31;
//		byte b = 0x2a;
		String aa = Integer.toHexString(a);
		System.out.println(aa);
		byte ddd = Byte.parseByte(aa,16);
		System.out.println(ddd);
		Integer ddd2 = Integer.parseInt("b6",16);
		System.out.println(ddd2);
	}
}
