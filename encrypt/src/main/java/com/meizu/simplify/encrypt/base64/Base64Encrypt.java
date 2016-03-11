package com.meizu.simplify.encrypt.base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 
 * <p><b>Title:</b><i>base64编码算法-正统算法</i></p>
 * <p>Desc: 基于RFC2045～RFC2049，上面有MIME的详细规范</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 上午11:40:53</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 上午11:40:53</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class Base64Encrypt {

	
	/**
	 * 注意：字符 '/'有问题的情况下，替换成'-'试试，把后面的'-'删除。
	 */
	public static final byte[] encodingTable = {
	(byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E',
	(byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J',
	(byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O',
	(byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T',
	(byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y',
	(byte) 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd',
	(byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i',
	(byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n',
	(byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's',
	(byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x',
	(byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2',
	(byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
	(byte) '8', (byte) '9', (byte) '+', (byte) '/', (byte) '-'
	};
	/*
     * set up the decoding table.
     */
	public static final byte[] decodingTable = new byte[128];
//	public static final byte[] decodingTable = new byte[''];
	static {
		//方式一
		for (int i = 0; i < 128; i++) {
			decodingTable[i] = (byte) -1;
		}
		for (int i = 'A'; i <= 'Z'; i++) {
			decodingTable[i] = (byte) (i - 'A');
		}
		for (int i = 'a'; i <= 'z'; i++) {
			decodingTable[i] = (byte) (i - 'a' + 26);
		}
		for (int i = '0'; i <= '9'; i++) {
			decodingTable[i] = (byte) (i - '0' + 52);
		}
		decodingTable['+'] = 62;
		decodingTable['/'] = 63;
		//方式二 TODO
	    /*for (int i = 0; i < 64; i++) {
	      decodingTable[Base64Encrypt.encodingTable[i]] = i;
	    }*/
	  }

	
    /**
     * 
     * 方法用途: Base64编码<br>
     * 操作步骤: TODO<br>
     * @param data
     * @return
     */
    public static byte[] encode(byte[]    data) {
        return encode(data,0);
    }
    
    /**
     * 
     * 方法用途: base64解码<br>
     * 操作步骤: TODO<br>
     * @param data
     * @return
     */
    public static byte[] decode(byte[]    data) {
        byte[]    bytes;
        byte b1;
		byte b2;
		byte b3;
		byte b4;
		//出问题时把下面这行注释掉试试
		data = discardNonBase64Bytes(data);//bug修复，后续添加的内容
        if (data[data.length - 2] == '=') {
            bytes = new byte[(((data.length / 4) - 1) * 3) + 1];
        } else if (data[data.length - 1] == '=') {
            bytes = new byte[(((data.length / 4) - 1) * 3) + 2];
        } else {
            bytes = new byte[((data.length / 4) * 3)];
        }

        for (int i = 0, j = 0; i < data.length - 4; i += 4, j += 3) {
            b1 = decodingTable[data[i]];
            b2 = decodingTable[data[i + 1]];
            b3 = decodingTable[data[i + 2]];
            b4 = decodingTable[data[i + 3]];

            bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));
            bytes[j + 2] = (byte) ((b3 << 6) | b4);
        }

        if (data[data.length - 2] == '=') {
            b1 = decodingTable[data[data.length - 4]];
            b2 = decodingTable[data[data.length - 3]];

            bytes[bytes.length - 1] = (byte) ((b1 << 2) | (b2 >> 4));
        } else if (data[data.length - 1] == '=') {
            b1 = decodingTable[data[data.length - 4]];
            b2 = decodingTable[data[data.length - 3]];
            b3 = decodingTable[data[data.length - 2]];

            bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));
        } else {
            b1 = decodingTable[data[data.length - 4]];
            b2 = decodingTable[data[data.length - 3]];
            b3 = decodingTable[data[data.length - 2]];
            b4 = decodingTable[data[data.length - 1]];

            bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));
            bytes[bytes.length - 1] = (byte) ((b3 << 6) | b4);
        }

        return bytes;
    }
    
    /**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: 和encode(byte[] bytes) 方法二选一<br>
	 * @param bytes unencoded.getBytes("8859_1") uses the ISO-8859-1 (Latin-1) encoding to convert the string to bytes
	 * @return
	 */
	public static String encodeTwo(byte[] bytes) {
	    ByteArrayOutputStream out = new ByteArrayOutputStream((int) (bytes.length * 1.37));
	    Base64StreamEncoder encodedOut = new Base64StreamEncoder(out);
	    try {
	      encodedOut.write(bytes);
	      encodedOut.close();

	      return out.toString("8859_1");
	    }
	    catch (IOException ignored) { return null; }
	  }
	/**
     * 
     * 方法用途: Base64编码-第二种算法<br>
     * 操作步骤: TODO<br>
     * @param data
     * @param offset
     * @return
     */
	public static byte[] encodeTwo(byte[] data, int offset) {//TODO 有bug，需要修复
				//可变可改，应该是方法参数
				int len = data.length;
				
				byte[] bytes;
				int realCount = data.length - offset;
				int modulus = realCount % 3;
				if (modulus == 0) {
					bytes = new byte[4 * realCount / 3];
				} else {
					bytes = new byte[4 * ((realCount / 3) + 1)];
				}
//				encode第二种算法

				int charCount = 0;
				int carryOver = 0;
				if ((offset | len | (data.length - (len + offset)) | (offset + len)) < 0)
					throw new IndexOutOfBoundsException();
				
				int j=0;
				for (int i = 0; i < len; i++) {
//					byte b = data[offset + i];// 类型是byte，会有问题，数据错乱，为什么呢
					int b = data[offset + i];
					// Take 24-bits from three octets, translate into four encoded chars
					// Break lines at 76 chars
					// If necessary, pad with 0 bits on the right at the end
					// Use = signs as padding at the end to ensure encodedLength % 4 ==
					// 0

					// Remove the sign bit,
					// thanks to Christian Schweingruber <chrigu@lorraine.ch>
					if (b < 0) {
						b += 256;
					}

					// First byte use first six bits, save last two bits
					if (charCount % 3 == 0) {
						int lookup = b >> 2;
						carryOver = b & 3; // last two bits
						bytes[j] = (encodingTable[lookup]);
						j++;
					}
					// Second byte use previous two bits and first four new bits,
					// save last four bits
					else if (charCount % 3 == 1) {
						int lookup = ((carryOver << 4) + (b >> 4)) & 63;
						carryOver = b & 15; // last four bits
						bytes[j] = (encodingTable[lookup]);
						j++;
					}
					// Third byte use previous four bits and first two new bits,
					// then use last six new bits
					else if (charCount % 3 == 2) {
						int lookup = ((carryOver << 2) + (b >> 6)) & 63;
						bytes[j] = (encodingTable[lookup]);
						j++;
						lookup = b & 63; // last six bits
						bytes[j] = (encodingTable[lookup]);
						j++;
						carryOver = 0;
					}
					charCount++;

					// Add newline every 76 output chars (that's 57 input chars)
					if (charCount % 57 == 0) {
						bytes[j] = ('\n');
						j++;
					}
				}

				// Handle leftover bytes
				if (charCount % 3 == 1) { // one leftover
					int lookup = (carryOver << 4) & 63;
					bytes[j] = (encodingTable[lookup]);
					j++;
					bytes[j] = ('=');
					j++;
					bytes[j] = ('=');
					j++;
				} else if (charCount % 3 == 2) { // two leftovers
					int lookup = (carryOver << 2) & 63;
					bytes[j] = (encodingTable[lookup]);
					j++;
					bytes[j] = ('=');
					j++;
				}
				return bytes;
	}
    /**
     * 
     * 方法用途: Base64编码-第一种算法<br>
     * 操作步骤: TODO<br>
     * @param data
     * @param offset
     * @return
     */
    public static byte[] encode(byte[] data, int offset) {
		byte[] bytes;
		int realCount = data.length - offset;
		int modulus = realCount % 3;
		if (modulus == 0) {
			bytes = new byte[4 * realCount / 3];
		} else {
			bytes = new byte[4 * ((realCount / 3) + 1)];
		}
//		第二种算法开始
		int dataLength = (data.length - modulus);
		int a1, a2, a3;
		for (int i = offset, j = 0; i < dataLength; i += 3, j += 4) {
			a1 = data[i] & 0xff;
			a2 = data[i + 1] & 0xff;
			a3 = data[i + 2] & 0xff;
			bytes[j] = encodingTable[(a1 >>> 2) & 0x3f];
			bytes[j + 1] = encodingTable[((a1 << 4) | (a2 >>> 4)) & 0x3f];
			bytes[j + 2] = encodingTable[((a2 << 2) | (a3 >>> 6)) & 0x3f];
			bytes[j + 3] = encodingTable[a3 & 0x3f];
		}
		
		/*
                * process the tail end.
                */
                int b1, b2, b3;
		int d1,d2;
		switch (modulus) {
		case 0: /* nothing left to do */
			break;
		case 1:
			d1 = data[data.length - 1] & 0xff;
			b1 = (d1 >>> 2) & 0x3f;
			b2 = (d1 << 4) & 0x3f;
			bytes[bytes.length - 4] = encodingTable[b1];
			bytes[bytes.length - 3] = encodingTable[b2];
			bytes[bytes.length - 2] = (byte) '=';
			bytes[bytes.length - 1] = (byte) '=';
			break;

		case 2:
			d1 = data[data.length - 2] & 0xff;
			d2 = data[data.length - 1] & 0xff;
			b1 = (d1 >>> 2) & 0x3f;
			b2 = ((d1 << 4) | (d2 >>> 4)) & 0x3f;
			b3 = (d2 << 2) & 0x3f;
			bytes[bytes.length - 4] = encodingTable[b1];
			bytes[bytes.length - 3] = encodingTable[b2];
			bytes[bytes.length - 2] = encodingTable[b3];
			bytes[bytes.length - 1] = (byte) '=';
			break;
		}
		return bytes;

	}

	private static byte[] discardNonBase64Bytes(byte[] data) {
		byte[] temp = new byte[data.length];
		int bytesCopied = 0;
		for (int i = 0; i < data.length; i++) {
			if (isValidBase64Byte(data[i])) {
				temp[bytesCopied++] = data[i];
			}
		}
		byte[] newData = new byte[bytesCopied];
		System.arraycopy(temp, 0, newData, 0, bytesCopied);
		return newData;
	}

	private static boolean isValidBase64Byte(byte b) {
		if (b == '=') {
			return true;
		} else if ((b < 0) || (b >= 128)) {
			return false;
		} else if (decodingTable[b] == -1) {
			return false;
		}
		return true;

	}
	
//	第二种方式

	  /**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: 和decode(byte[] bytes) 二选一<br>
	 * @param encoded
	 * @return
	 */
	public static byte[] decodeToBytes(String encoded) {
	    byte[] bytes = null;
	    try {
	      bytes = encoded.getBytes("8859_1");
	    }
	    catch (UnsupportedEncodingException ignored) { }

	    Base64StreamDecoder in = new Base64StreamDecoder(new ByteArrayInputStream(bytes));
	    
	    ByteArrayOutputStream out = new ByteArrayOutputStream((int) (bytes.length * 0.67));

	    try {
	      byte[] buf = new byte[4 * 1024];  // 4K buffer
	      int bytesRead;
	      while ((bytesRead = in.read(buf)) != -1) {
	        out.write(buf, 0, bytesRead);
	      }
	      out.close();
	      in.close();

	      return out.toByteArray();
	    }
	    catch (IOException ignored) { return null; }
	  }
	  

	

}