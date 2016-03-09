package com.meizu.simplify.encrypt;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import com.meizu.simplify.encrypt.base64.Base64Decoder;
import com.meizu.simplify.encrypt.base64.Base64Encoder;


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
public class Base64UtilTest {
	public static void base64Decoder(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: java Base64Decoder fileToDecode");
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

	public static void base64Encoder(String[] args) throws Exception {

		// BASE64Encoder base64Encoder = new BASE64Encoder();
		// strMi = base64Encoder.encode(byteMi);
		// a2ee5e1d00de3fc5320a95beaf15b692

		// System.out.println(token);//a2ee5e1d00de3fc5320a95beaf15b692
		// System.out.println(Base64Encoder.encode(token));//YTJlZTVlMWQwMGRlM2ZjNTMyMGE5NWJlYWYxNWI2OTI=

		if (args.length != 1) {
			System.err.println("Usage: java com.dudu.upload.Base64Encoder fileToEncode");
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
}
