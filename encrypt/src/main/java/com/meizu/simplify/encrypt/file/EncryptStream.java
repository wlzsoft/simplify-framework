package com.meizu.simplify.encrypt.file;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.meizu.simplify.encrypt.Encrypt;
import com.meizu.simplify.encrypt.Keys;
/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年9月17日 下午9:18:35</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年9月17日 下午9:18:35</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
final class EncryptStream extends FilterOutputStream {
	private byte[] conkeys = null;
	private byte[] rc4Keys = null;
	private int page = 0;

	private byte[] buf = new byte[512];
	private int pos = 0;

	public EncryptStream(OutputStream in, byte[] key) {
		super(in);
		this.conkeys = Keys.calcConKey("ff", key);
		this.rc4Keys = Keys.calcRc4Key("ff", key);
	}

	protected EncryptStream(OutputStream out) {
		super(out);
	}

	@Override
	public void write(byte b[], int off, int len) throws IOException {
		for (int i = 0; i < len; ++i, ++off, ++pos) {
			if (pos == 512) {
				writeToFile(buf, 0, 512);
				pos = 0;
			}
			buf[pos] = b[off];
		}
	}

	@Override
	public void flush() throws IOException {
		if (pos != 0) {
			writeToFile(buf, 0, pos);
			pos = 0;
		}
	}

	private void writeToFile(byte b[], int off, int len) throws IOException {
		if (len > 0) {
			if (b.length == len) {
				if (page % 3 == 0) {
					Encrypt.enConfusion(b, conkeys);
				}
				if (page == 0) {
					Encrypt.rc4crypt(b, rc4Keys);
				}
			} else {
				byte[] data = new byte[len];
				System.arraycopy(b, off, data, 0, len);
				Encrypt.enConfusion(data, conkeys);
				Encrypt.rc4crypt(data, rc4Keys);
				System.arraycopy(data, 0, b, off, len);
			}
		}
		++page;
		out.write(b, off, len);
	}
}
