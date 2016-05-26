package com.meizu.simplify.encrypt;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import com.meizu.simplify.encrypt.base64.Base64StreamDecoder;
import com.meizu.simplify.encrypt.base64.Base64StreamEncoder;
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
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class Base64EncryptTest {
	
	@Test
	public void base64Decoder() throws Exception {
		
		String[] args = new String[] {"C:/Users/Administrator/Desktop/test2.txt"};
		if (args.length != 1) {
			System.err.println("Usage: java com.meizu.simplify.encrypt.base64.Base64Decoder fileToDecode");
			return;
		}
//		InputStream is = new FileInputStream(args[0]);
		Base64StreamDecoder decoder = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			decoder = new Base64StreamDecoder(new BufferedInputStream(this.getClass().getResourceAsStream("/test2.txt")));
			byte[] buf = new byte[4 * 1024]; // 4K buffer
			int bytesRead;
			while ((bytesRead = decoder.read(buf)) != -1) {
				baos.write(buf, 0, bytesRead);
			}
		} finally {
			if (decoder != null) {
				decoder.close();
			}
		}
		String result = new String(baos.toByteArray());
		Assert.assertEquals(result, "哈哈。谢谢了");
	}
	@Test
	public void base64Encoder() throws Exception {

		String[] args = new String[] {"C:/Users/Administrator/Desktop/test.txt"};
		if (args.length != 1) {
			System.err.println("Usage: java com.meizu.simplify.encrypt.base64.Base64Encoder fileToEncode");
			return;
		}

		Base64StreamEncoder encoder = null;
		BufferedInputStream in = null;
//		InputStream is = new FileInputStream(args[0])
		InputStream is = this.getClass().getResourceAsStream("/test.txt");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			encoder = new Base64StreamEncoder(baos);
			in = new BufferedInputStream(is);

			byte[] buf = new byte[4 * 1024]; // 4K buffer
			int bytesRead;
			while ((bytesRead = in.read(buf)) != -1) {
				encoder.write(buf, 0, bytesRead);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (encoder != null) {
				encoder.close();
			}
		}
		String result = new String(baos.toByteArray());
		Assert.assertEquals(result, "5ZOI5ZOI44CC6LCi6LCi5LqG");
	}
	@Test
	public void test() {
//		UUID uuid = UUID.randomUUID();
//		System.out.println(Base64Codec.encode64String(uuid.toString().getBytes()));
//		String str = "色搜到发";
//		String str = "色搜到发sejjjjjjjjjjjjsfirsjfsldjf";
		String str = "色搜到发sejjjjjjjjjjjjsfirsjfsldjfoisejflsidjfsiosfsefftecxexcdsde";
		String source = Base64VariantEncrypt.encode64String(str.getBytes());
		System.out.println(source);
		System.out.println(new String(Base64VariantEncrypt.decode64(source)));
		String source2 = new String(Base64Encrypt.encode(str.getBytes()));
		System.out.println(source2);
		System.out.println(new String(Base64Encrypt.decode(source2.getBytes())));
		System.out.println(new String(Base64Encrypt.decodeToBytes(source2)));
		System.out.println(new String(Base64Encrypt.encode(str.getBytes())));
		System.out.println(new String(Base64Encrypt.encodeTwo(str.getBytes())));
		System.out.println(new String(Base64Encrypt.encodeTwo(str.getBytes(),0)));
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
