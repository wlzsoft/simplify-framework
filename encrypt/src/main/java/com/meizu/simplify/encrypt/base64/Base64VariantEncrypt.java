package com.meizu.simplify.encrypt.base64;

import java.nio.charset.Charset;


/**
 * <p><b>Title:</b><i>base64编码算法，自定义变种，更好了编码表和base64解码表</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月4日 上午10:12:23</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月4日 上午10:12:23</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class Base64VariantEncrypt {
	public final static byte[] encodingTable = new byte[64];
//	final static byte[] base64Alphabet = new byte[255];
	final static byte[] decodingTable = new byte[255];
	//注意：如果文件编码是ansi，那么如果文件中有中文文字，那么加密后，中文部分解密会乱码
	final static Charset CHARSET = Charset.forName("UTF-8");

	static {
		for (int i = 0; i < 10; i++) {
			encodingTable[i] = (byte) ('0' + i);
		}
		for (int i = 10; i < 36; i++) {
			encodingTable[i] = (byte) ('a' + i - 10);
		}
		for (int i = 36; i < 62; i++) {
			encodingTable[i] = (byte) ('A' + i - 36);
		}
		encodingTable[62] = '_';
		encodingTable[63] = '-';

		for (int i = 0; i < 255; ++i) {
			decodingTable[i] = (byte) -1;
		}
		for (int i = '0'; i <= '9'; ++i) {
			decodingTable[i] = (byte) (i - '0');
		}
		for (int i = 'a'; i <= 'z'; ++i) {
			decodingTable[i] = (byte) (i - 'a' + 10);
		}
		for (int i = 'A'; i <= 'Z'; ++i) {
			decodingTable[i] = (byte) (i - 'A' + 36);
		}
		decodingTable['_'] = 62;
		decodingTable['-'] = 63;
	}

	public static String encode64String(byte[] binaryData) {
		if (binaryData == null || binaryData.length == 0)
			return "";
		byte[] encoded = encode64(binaryData);
		return new String(encoded, CHARSET);
	}

	private static byte[] encode64(byte[] data) {
		long binaryDataLength = data.length;
		long lengthDataBits = binaryDataLength * 8;
		long fewerThan24bits = lengthDataBits % 24;
		long tripletCount = lengthDataBits / 24;
		long encodedDataLengthLong;

		if (fewerThan24bits != 0) {
			// data not divisible by 24 bit
			encodedDataLengthLong = (tripletCount + 1) * 4;
		} else {
			// 16 or 8 bit
			encodedDataLengthLong = tripletCount * 4;
		}

		if (encodedDataLengthLong > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
					"Input array too big, output array would be bigger than Integer.MAX_VALUE=" + Integer.MAX_VALUE);
		}
		int encodedDataLength = (int) encodedDataLengthLong;
		byte[] bytes = new byte[encodedDataLength];

		byte k, l, b1, b2, b3;

		int encodedIndex = 0;
		int dataIndex;
		int i;

		// log.debug("number of triplets = " + numberTriplets);
		for (i = 0; i < tripletCount; i++) {
			dataIndex = i * 3;
			b1 = data[dataIndex];
			b2 = data[dataIndex + 1];
			b3 = data[dataIndex + 2];

			// log.debug("b1= " + b1 +", b2= " + b2 + ", b3= " + b3);

			l = (byte) (b2 & 0x0f);
			k = (byte) (b1 & 0x03);

			byte val1 = ((b1 & 0xffffff80) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
			byte val2 = ((b2 & 0xffffff80) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
			byte val3 = ((b3 & 0xffffff80) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

			bytes[encodedIndex] = encodingTable[val1];
			// log.debug( "val2 = " + val2 );
			// log.debug( "k4 = " + (k<<4) );
			// log.debug( "vak = " + (val2 | (k<<4)) );
			bytes[encodedIndex + 1] = encodingTable[val2 | (k << 4)];
			bytes[encodedIndex + 2] = encodingTable[(l << 2) | val3];
			bytes[encodedIndex + 3] = encodingTable[b3 & 0x3f];

			encodedIndex += 4;
		}

		// form integral number of 6-bit groups
		dataIndex = i * 3;

		endEncode(data, fewerThan24bits, bytes, encodedIndex, dataIndex);
		return bytes;
	}

	private static void endEncode(byte[] data, long fewerThan24bits, byte[] bytes, int encodedIndex,
			int dataIndex) {
		byte k;
		byte l;
		byte b1;
		byte b2;
		if (fewerThan24bits == 8) {
			b1 = data[dataIndex];
			k = (byte) (b1 & 0x03);
			// log.debug("b1=" + b1);
			// log.debug("b1<<2 = " + (b1>>2) );
			byte val1 = ((b1 & 0xffffff80) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
			bytes[encodedIndex] = encodingTable[val1];
			bytes[encodedIndex + 1] = encodingTable[k << 4];
			bytes[encodedIndex + 2] = '.';
			bytes[encodedIndex + 3] = '.';
		} else if (fewerThan24bits == 0x10) {

			b1 = data[dataIndex];
			b2 = data[dataIndex + 1];
			l = (byte) (b2 & 0x0f);
			k = (byte) (b1 & 0x03);

			byte val1 = ((b1 & 0xffffff80) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
			byte val2 = ((b2 & 0xffffff80) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);

			bytes[encodedIndex] = encodingTable[val1];
			bytes[encodedIndex + 1] = encodingTable[val2 | (k << 4)];
			bytes[encodedIndex + 2] = encodingTable[l << 2];
			bytes[encodedIndex + 3] = '.';
		}
	}

	public static byte[] decode64(String base64) {
		if (base64 == null || base64.isEmpty())
			return new byte[0];
		byte[] encodeBytes = base64.getBytes(CHARSET);
		return decode64(encodeBytes);
	}

	private static byte[] decode64(byte[] base64Data) {
		// RFC 2045 requires that we discard ALL non-Base64 characters
		// base64Data = discardNonBase64(base64Data);

		// handle the edge case, so we don't have to worry about it later
		if (base64Data == null || base64Data.length == 0) {
			return new byte[0];
		}

		int numberQuadruple = base64Data.length / 4;
		byte decodedData[];
		byte b1, b2, b3, b4, marker0, marker1;

		// Throw away anything not in base64Data

		int encodedIndex = 0;
		int dataIndex;
		{
			// this sizes the output array properly - rlw
			int lastData = base64Data.length;
			// ignore the '=' padding
			while (base64Data[lastData - 1] == '.') {
				if (--lastData == 0) {
					return new byte[0];
				}
			}
			decodedData = new byte[lastData - numberQuadruple];
		}

		for (int i = 0; i < numberQuadruple; i++) {
			dataIndex = i * 4;
			marker0 = base64Data[dataIndex + 2];
			marker1 = base64Data[dataIndex + 3];

			b1 = decodingTable[base64Data[dataIndex]];
			b2 = decodingTable[base64Data[dataIndex + 1]];

			if (marker0 != '.' && marker1 != '.') {
				// No PAD e.g 3cQl
				b3 = decodingTable[marker0];
				b4 = decodingTable[marker1];

				decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
				decodedData[encodedIndex + 1] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
				decodedData[encodedIndex + 2] = (byte) (b3 << 6 | b4);
			} else if (marker0 == '.') {
				// Two PAD e.g. 3c[Pad][Pad]
				decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
			} else {
				// One PAD e.g. 3cQ[Pad]
				b3 = decodingTable[marker0];
				decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
				decodedData[encodedIndex + 1] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
			}
			encodedIndex += 3;
		}
		return decodedData;
	}
	
}
