package com.meizu.simplify.encrypt;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import org.junit.Test;

import com.meizu.simplify.encrypt.base64.Base64Decoder;
import com.meizu.simplify.encrypt.base64.Base64Encoder;
import com.meizu.simplify.encrypt.base64.Base64Encrypt;
import com.meizu.simplify.encrypt.base64.Base64VariantEncrypt;


/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 下午5:07:13</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月9日 下午5:07:13</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class Base64EncryptTest {
	
	@Test
	public void base64Decoder() throws Exception {
		
		String[] args = new String[] {"C:/Users/Administrator/Desktop/test.txt"};
		if (args.length != 1) {
			System.err.println("Usage: java com.meizu.simplify.encrypt.base64.Base64Decoder fileToDecode");
			return;
		}

		Base64Decoder decoder = null;
		try {
			decoder = new Base64Decoder(new BufferedInputStream(new FileInputStream(args[0])));
			byte[] buf = new byte[4 * 1024]; // 4K buffer
			int bytesRead;
			while ((bytesRead = decoder.read(buf)) != -1) {
				System.out.write(buf, 0, bytesRead);
			}
		} finally {
			if (decoder != null)
				decoder.close();
		}
	}
	@Test
	public void base64Encoder() throws Exception {

		// BASE64Encoder base64Encoder = new BASE64Encoder();
		// strMi = base64Encoder.encode(byteMi);
		// a2ee5e1d00de3fc5320a95beaf15b692

		// System.out.println(token);//a2ee5e1d00de3fc5320a95beaf15b692
		// System.out.println(Base64Encoder.encode(token));//YTJlZTVlMWQwMGRlM2ZjNTMyMGE5NWJlYWYxNWI2OTI=

		String[] args = new String[] {"C:/Users/Administrator/Desktop/test.txt"};
		if (args.length != 1) {
			System.err.println("Usage: java com.meizu.simplify.encrypt.base64.Base64Encoder fileToEncode");
			return;
		}

		Base64Encoder encoder = null;
		BufferedInputStream in = null;
		try {
			encoder = new Base64Encoder(System.out);
			in = new BufferedInputStream(new FileInputStream(args[0]));

			byte[] buf = new byte[4 * 1024]; // 4K buffer
			int bytesRead;
			while ((bytesRead = in.read(buf)) != -1) {
				encoder.write(buf, 0, bytesRead);
			}
		} finally {
			if (in != null)
				in.close();
			if (encoder != null)
				encoder.close();
		}
	}
	@Test
	public void test() {
//		UUID uuid = UUID.randomUUID();
//		System.out.println(Base64Codec.encode64String(uuid.toString().getBytes()));
//		String str = "sejjjjjjjjjjjjsfirsjfsldjfoisejflsidjfsio";
		String str = "色搜到发";
		String source = Base64VariantEncrypt.encode64String(str.getBytes());
		System.out.println(source);
		System.out.println(new String(Base64VariantEncrypt.decode64(source)));
		String source2 = new String(Base64Encrypt.encode(str.getBytes()));
		System.out.println(source2);
		System.out.println(new String(Base64Encrypt.decode(source2.getBytes())));
		System.out.println(new String(Base64Encoder.encode(str.getBytes())));
	}
	@Test
	public void test2() {
		byte[] base64Alphabet2 = {
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
//		变种的编码表
		byte[] base64Alphabet = new byte[255];
		for (int i = 0; i < 255; ++i) {
			base64Alphabet[i] = (byte) -1;
		}
		for (int i = '0'; i <= '9'; ++i) {
			base64Alphabet[i] = (byte) (i - '0');
		}
		for (int i = 'a'; i <= 'z'; ++i) {
			base64Alphabet[i] = (byte) (i - 'a' + 10);
		}
		for (int i = 'A'; i <= 'Z'; ++i) {
			base64Alphabet[i] = (byte) (i - 'A' + 36);
		}
		base64Alphabet['_'] = 62;
		base64Alphabet['-'] = 63;
		for(byte b : base64Alphabet) {
			char c = (char) b;
			System.out.println(c);
		}
		System.out.println("==========================");
		for(byte b : base64Alphabet2) {
			char c = (char) b;
			System.out.println(c);
		}
	}
}
