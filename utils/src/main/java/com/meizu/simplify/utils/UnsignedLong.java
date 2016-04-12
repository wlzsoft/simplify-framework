package com.meizu.simplify.utils;

/**
 * <p><b>Title:</b><i>模拟c/c++的无符号整型的实现--Long型</i></p>
 * <p>Desc: 目前java中的所有数据类型，都是有符号位的</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月18日 下午1:57:07</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月18日 下午1:57:07</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class UnsignedLong {
	public static long getUnSignedLong(long l) {
		return getLong(longToDword(l), 0);
	}

	/**
	 * 
	 * 方法用途: 将long型数据转换为C/C++的无符号Dword整数的字节数组<br>
	 * 操作步骤: TODO<br>
	 * @param value
	 * @return
	 */
	private static byte[] longToDword(long value) {

		byte[] data = new byte[4];

		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) (value >> (8 * i));
		}

		return data;
	}

	/**
	 * 
	 * 方法用途: 将C/C++的无符号 DWORD类型转换为java的long型<br>
	 * 操作步骤: TODO<br>
	 * @param buf
	 * @param index
	 * @return
	 */
	private static long getLong(byte buf[], int index) {

		int firstByte = (0x000000FF & ((int) buf[index]));
		int secondByte = (0x000000FF & ((int) buf[index + 1]));
		int thirdByte = (0x000000FF & ((int) buf[index + 2]));
		int fourthByte = (0x000000FF & ((int) buf[index + 3]));

		long unsignedLong = ((long) (firstByte | secondByte << 8 | thirdByte << 16 | fourthByte << 24)) & 0xFFFFFFFFL;

		return unsignedLong;
	}
}