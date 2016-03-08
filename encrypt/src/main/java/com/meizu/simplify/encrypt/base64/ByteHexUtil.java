package com.meizu.simplify.encrypt.base64;

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
	//des结束，后续des解析，需要确认是哪部分的=======================
		
		
		
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
		}
		
	/**
	 * hex string to bytes
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] hex2Bytes(String input) {
		int len = input.length() / 2;
		byte[] rtn = new byte[len];
		
		for (int i = 0; i < len; i++) {
			rtn[i] = hex2Byte(input.substring(i * 2, i * 2 + 2));
		}
		return rtn;
	}
	private static byte hex2Byte(String s) {
		int high = HEX_CHAR_LIST.indexOf(new Character(s.charAt(0))) << 4;
		int low = HEX_CHAR_LIST.indexOf(new Character(s.charAt(1)));
		
		return (byte) (high + low);
	}
	
	/**
	 * bytes to hex string
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2Hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			
			int i = b & 0xFF;
			
			//1.第一种写法
			if (i <= 0xF) {
				sb.append("0");
			}
			String tmp = Integer.toHexString(i);
		
			/*
			 //2.第二种写法
			 String tmp = Integer.toHexString(i);
			if (tmp.length() == 1) {
				sb.append("0");
			}*/
			
			sb.append(tmp);
		}
		
			
		return sb.toString();
	}

	public static byte[] hex2byte(String hexStr) {
		try {
			byte[] bts = new byte[hexStr.length() / 2];
			for ( int i = 0, j = 0; j < bts.length; j++ ) {
				String hexSub = hexStr.substring(i, i + 2);
//				byte parseHex = Byte.parseByte(hexSub,16);
				int parseHex = Integer.parseInt(hexSub, 16);
				bts[j] = (byte) parseHex;
				i += 2;
			}
			return bts;
			
		} catch ( Exception e ) {
			return "".getBytes();
		}
	}
		
	public static String bytesToHexString(byte[] src){  
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	}  
	
}
