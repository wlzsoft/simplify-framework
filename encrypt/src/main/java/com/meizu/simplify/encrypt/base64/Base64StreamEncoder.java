package com.meizu.simplify.encrypt.base64;

import java.io.*;

/**
 * <p><b>Title:</b><i>base64编码算法-正统算法</i></p>
 * <p>Desc: 可用于编码字符串和字节流数据。base64算法的详细说明可以查看 RFC 1521 规范 的5.2小节  
 * <p>
 * 1.编码字符串(string)的用法:
 * <blockquote><pre>
 * String unencoded = "3lisoiwseis";
 * String encoded = Base64Encoder.encode(unencoded);
 * </pre></blockquote>
 * 2.编码字节流(streams)的用法:
 * <blockquote><pre>
 * OutputStream out = new Base64Encoder(System.out);
 * </pre></blockquote></p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月10日 上午11:29:43</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月10日 上午11:29:43</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version 1.2, 2002/11/01, added encode(byte[]) method to better handle
 *                           binary data (thanks to Sean Graham)
 * @version 1.1, 2000/11/17, fixed bug with sign bit for char values
 * @version 1.0, 2000/06/11
 * @version 0.1
 *
 */
public class Base64StreamEncoder extends FilterOutputStream {

	OutputStream out;
	public Base64StreamEncoder(OutputStream out) {
		super(out);
		this.out = out;
	}


	public void write(byte[] buf) throws IOException {
		byte[] bytes = Base64Encrypt.encodeTwo(buf, 0);
		out.write(bytes);
		try (OutputStream ostream = out) {
			out.flush();
		}
	}

}
