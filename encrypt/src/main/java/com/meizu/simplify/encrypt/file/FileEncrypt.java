package com.meizu.simplify.encrypt.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import com.meizu.simplify.encrypt.Keys;
/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年9月17日 下午9:18:46</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年9月17日 下午9:18:46</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class FileEncrypt {

	public static void streamEncrypt(InputStream in, OutputStream out, byte[] keys) throws IOException {
		if (in == null || out == null) {
			throw new IOException("流为空");
		}

		if (keys == null) {
			// out.write(new byte[] {0});
			keys = Keys.defaultFileKey();
		} else {
			// out.write(new byte[] {1});
		}
		EncryptStream cos = new EncryptStream(out, keys);
		GZIPOutputStream gzos = new GZIPOutputStream(cos);
		byte[] buffer = new byte[512];
		int bytesRead;
		try {
			while ((bytesRead = in.read(buffer)) != -1) {
				gzos.write(buffer, 0, bytesRead);
			}
			gzos.finish();
			gzos.flush();
			gzos.close();
			cos.close();
		} catch (IOException e) {
			throw e;
		}
	}
}
