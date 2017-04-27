package vip.simplify.server;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode; 

/**
 * <p><b>Title:</b><i>使用nio的实现http的response数据流</i></p>
 * <p>Desc: 1.通过gather和scatter方式，底层操作系统使用dma访问，2.使用操作系统虚拟内存的映射方式</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年7月12日 上午11:14:59</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年7月12日 上午11:14:59</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class MappedHttpTest { 
	public static void main (String [] arg) throws Exception { 
		ByteBuffer [] gather = { null, null, null }; 
		String contentType = "unknown/unknown"; 
		long contentLength = -1; 
		try { 
			String file = "E:/workspace-git/simplify-framework/webserver/src/main/resources/webapp/index.html"; 
			ByteBuffer header = ByteBuffer.wrap (("HTTP/1.0 200 OK\r\nServer: simpify server\r\n").getBytes ("US-ASCII")); 
			FileInputStream fis = new FileInputStream (file); 
			FileChannel fc = fis.getChannel(); 
			MappedByteBuffer filedata = fc.map (MapMode.READ_ONLY, 0, fc.size( )); 
			gather[0] = header;
			gather[2] = filedata; 
			contentLength = fc.size( ); 
			contentType = URLConnection.guessContentTypeFromName (file); 
			fis.close();
		} catch (IOException e) { 
			ByteBuffer buf = ByteBuffer.allocate (128); 
			String msg = "Could not open file: " + e + "\r\n"; 
			buf.put (msg.getBytes("US-ASCII")); 
			buf.flip(); 
			contentLength = msg.length( ); 
			contentType = "text/plain"; 
			gather[0] = ByteBuffer.wrap (("HTTP/1.0 404 Not Found\r\nServer: simpify server\r\n").getBytes("US-ASCII")); 
			gather[2] = buf; 
		} 
		StringBuffer sb = new StringBuffer(); 
		sb.append ("Content-Length: " + contentLength).append ("\r\n").append ("Content-Type: ").append (contentType); 
		sb.append ("\r\n\r\n"); 
		ByteBuffer dynhdrs = ByteBuffer.allocate (128); 
		dynhdrs.put (sb.toString().getBytes("US-ASCII")); 
		dynhdrs.flip(); 
		gather[1] = dynhdrs;
		FileOutputStream fos = new FileOutputStream ("MappedHttp.out"); 
		FileChannel out = fos.getChannel( ); 
		while (out.write (gather) > 0) { 
			System.out.println("length:"+gather.length);
		} 
		fos.close();
		out.close( );
	} 
} 