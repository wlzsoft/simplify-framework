package vip.simplify.encrypt.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import vip.simplify.encrypt.Keys;
/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年9月17日 下午9:18:41</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年9月17日 下午9:18:41</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class FileDecrypt {
	public static void streamDecrypt(InputStream in, OutputStream out, byte[] keys) throws IOException {
		if (in == null || out == null) {
			throw new IOException("流为空");
		}

		byte[] bom = new byte[1];
		// in.read(bom);
		if (keys == null) {
			if (bom[0] == 0) {
				keys = Keys.defaultFileKey();
			} else {
				throw new IOException("需要密码");
			}
		}

		DecryptStream cis = new DecryptStream(in, keys);
		GZIPInputStream gzis = new GZIPInputStream(cis);
		byte[] buffer = new byte[512];
		int bytesRead;
		try {
			while ((bytesRead = gzis.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			gzis.close();
			cis.close();
		} catch (IOException e) {
			throw e;
		}
	}
}
