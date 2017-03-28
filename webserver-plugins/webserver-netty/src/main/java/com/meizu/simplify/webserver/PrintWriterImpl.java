package com.meizu.simplify.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2017年3月28日 下午12:37:24</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2017年3月28日 下午12:37:24</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class PrintWriterImpl extends PrintWriter{

	private ServletOutputStream os;
	
	public PrintWriterImpl(OutputStream out) {
		super(out);
		this.os = (ServletOutputStream) out;
	}

	@Override
	public void print(String s) {
		super.print(s);
		try {
			os.print(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
