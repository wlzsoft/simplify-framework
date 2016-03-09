package com.meizu.simplify.encrypt;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月8日 下午4:09:12</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月8日 下午4:09:12</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */

public class ByteHexUtil {
		
		public static final List<Character> HEX_CHAR_LIST;
		
		static {
			HEX_CHAR_LIST = new ArrayList<Character>();
			HEX_CHAR_LIST.add(new Character('0'));
			HEX_CHAR_LIST.add(new Character('1'));
			HEX_CHAR_LIST.add(new Character('2'));
			HEX_CHAR_LIST.add(new Character('3'));
			HEX_CHAR_LIST.add(new Character('4'));
			HEX_CHAR_LIST.add(new Character('5'));
			HEX_CHAR_LIST.add(new Character('6'));
			HEX_CHAR_LIST.add(new Character('7'));
			HEX_CHAR_LIST.add(new Character('8'));
			HEX_CHAR_LIST.add(new Character('9'));
			HEX_CHAR_LIST.add(new Character('a'));
			HEX_CHAR_LIST.add(new Character('b'));
			HEX_CHAR_LIST.add(new Character('c'));
			HEX_CHAR_LIST.add(new Character('d'));
			HEX_CHAR_LIST.add(new Character('e'));
			HEX_CHAR_LIST.add(new Character('f'));
//			HEX_CHAR_LIST.add(new Character('A'));
//			HEX_CHAR_LIST.add(new Character('B'));
//			HEX_CHAR_LIST.add(new Character('C'));
//			HEX_CHAR_LIST.add(new Character('D'));
//			HEX_CHAR_LIST.add(new Character('E'));
//			HEX_CHAR_LIST.add(new Character('F'));
		}
	private static byte hex2Byte(String source) {
		int high = HEX_CHAR_LIST.indexOf(new Character(source.charAt(0))) << 4;
		int low = HEX_CHAR_LIST.indexOf(new Character(source.charAt(1)));

		return (byte) (high + low);
	}
		
	
	public static byte[] hex2Bytes(String source) {
		try {
			byte[] bts = new byte[source.length() / 2];
			for ( int i = 0, j = 0; j < bts.length; j++ ) {
//				第一种方式
				String hexSub = source.substring(i, i + 2);
//				第二种方式
//				byte parseHex = Byte.parseByte(hexSub,16);
				int parseHex = Integer.parseInt(hexSub, 16);
				bts[j] = (byte) parseHex;
				i += 2;
				//第三种方式 推荐用法之一
//				bts[j] = hex2Byte(source.substring(i * 2, i * 2 + 2));
			}
			
//			第四种用法
			/*int len = source.length() / 2;
			byte[] bts = new byte[len];
			for (int i = 0; i < len; i++) {
				bts[i] = hex2Byte(source.substring(i * 2, i * 2 + 2));
			}*/
			
			return bts;
			
		} catch ( Exception e ) {
			return "".getBytes();
		}
	}
	
	
	/*
	 * byteHEX()，用来把一个byte类型的数转换成十六进制的ASCII表示，
	 * 　因为java中的byte的toString无法实现这一点，我们又没有C语言中的 sprintf(outbuf,"%02X",ib)
	 */
	private static String byteHEX(byte sourceByte) {
		// 用来将字节转换成 16 进制表示的字符
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A','B', 'C', 'D', 'E', 'F' };//可以抽取待外层循环，也就是bytes2Hex(byte[] bytes)方法的方法体的第一行，for语句之前，防止重复创建 
//		char[] hexDigits={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		char[] str = new char[2];
		str[0] = hexDigits[(sourceByte >>> 4) & 0X0F];
		str[1] = hexDigits[sourceByte & 0X0F];
		String s = new String(str);
		return s;
	}
	
	
	/**
	 * 
	 * 方法用途: 字节数组转成16进制字符串<br>
	 * 操作步骤: TODO<br>
	 * @param sourceBytes
	 * @return
	 */
	public static String bytes2Hex2(byte[] sourceBytes) {
		
		if (sourceBytes == null || sourceBytes.length <= 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();

		for (byte b : sourceBytes) {
			
			int v = b & 0xFF;
//			int v = b & 0xff;
			
			//1.第一种写法-jdk自带类实现方式
			if (v <= 0xF) {
				sb.append("0");//添加0的目的是什么
			}
			String hv = Integer.toHexString(v);//jdk自带类实现方式
			//end
		
			/*
			 //2.第二种写法-jdk自带类实现方式
			String hv = Integer.toHexString(v);
			//if (hv.length() < 2) {
			if (hv.length() == 1) {
				sb.append("0");
			}
			//end
			*/
//			第三种写法-自己实现方式
//			String hv = byteHEX((byte)v);//自己实现方式
//			end
			sb.append(hv);
		}
//		return sb.toUpperCase();
		return sb.toString();
	}
	
	/**
	 * 
	 * 方法用途: 字节数组转成16进制字符串<br>
	 * 操作步骤: 性能比bytes2Hex2优，后续合并在一起<br>
	 * @param sourceBytes
	 * @return
	 */
	public static String bytes2Hex(byte[] sourceBytes) {
		// 用来将字节转换成 16 进制表示的字符
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
//		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
			
			
		// 把密文转换成十六进制的字符串形式
		int j = sourceBytes.length;
//			int j = 16; // 用字节表示就是 16 个字节
        char str[] = new char[j * 2];// 每个字节用 16 进制表示的话，使用两个字符
		
		// 所以表示成 16 进制需要 32 个字符
		int k = 0; // 表示转换结果中对应的字符位置
		for (int i = 0; i < j; i++) { // 从第一个字节开始，对 MD5 的每一个字节
			// 转换成 16 进制字符的转换
			byte byte0 = sourceBytes[i]; // 取第 i 个字节
			str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
			// >>> 为逻辑右移，将符号位一起右移
			str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
		}
		return new String(str); // 换后的结果转换为字符串
//		return new String(str).toLowerCase();

	}
	
	
	
}
