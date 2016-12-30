package com.meizu;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpResponse implements HttpServletResponse{
	private String version = "HTTP/1.1";// 版本默认值
	private String statusCode;
	private String reason;// 原因短语

	private Map<String, String> responseHeader = new HashMap<String, String>();

	private char[] body;

	private PrintWriter bw;
	private SocketChannel sc;
	private String charset;
	private String contentType;
	
	public HttpResponse(Socket socket,SocketChannel sc) throws IOException {
		if(socket != null) {
			bw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
//			sc = socket.getChannel();
		}
		this.sc = sc;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Map<String, String> getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(Map<String, String> responseHeader) {
		this.responseHeader = responseHeader;
	}

	public char[] getBody() {
		return body;
	}

	public void setBody(char[] body) {
		this.body = body;
	}
	public void sendToClient() throws IOException {
		if(body != null ) {
			bw = getWriter();
			bw.write(getBody());
		}
		bw.flush();
		bw.close();
	}
	public void sendToClientByNio() throws IOException {
		if(body != null ) {
			ByteBuffer[] srcs = new ByteBuffer[4];
			String msg = (getVersion() + " " + getStatusCode() + " " + getReason() + "\r\n"
					+"Date: " + new Date() + "\r\n"
					+"Server: meizu-server-0.1\r\n"
					+"Accept-Ranges: bytes\r\n"
					+"Content-Length: " + getBody().length + "\r\n"
					+"Content-Type: text/html\r\n");
			byte[] bytes = msg.getBytes();
			ByteBuffer responseByte = ByteBuffer.wrap(bytes);
			/*ByteBuffer responseByte = ByteBuffer.allocate(bytes.length);
			responseByte.put(bytes);
			responseByte.flip();*/
			
			String responseHeadText="";
			Set<Entry<String,String>> entryHead = responseHeader.entrySet();
			for (Entry<String, String> entry : entryHead) {
				responseHeadText += entry.getKey()+": "+entry.getValue()+"\r\n";
			}
			ByteBuffer responseHeadByte = ByteBuffer.wrap(responseHeadText.getBytes());
			
			ByteBuffer cookieByte = ByteBuffer.wrap(("Set-Cookie: "	+ getResponseHeader().get("Set-Cookie") + "\r\n").getBytes());
			
			ByteBuffer responseBodyByte = ByteBuffer.wrap(("\r\n"+new String(body)).getBytes());
			
			srcs[0] = responseByte;
			srcs[1] = responseHeadByte;
			srcs[2] = cookieByte;
			srcs[3] = responseBodyByte;
			sc.write(srcs);
		}
		sc.close();
	}
	
	@Override
	public PrintWriter getWriter() throws IOException {
		StringBuilder sb = new StringBuilder(); 
		sb.append(getVersion() + " " + getStatusCode() + " "
				+ getReason() + "\r\n");
		sb.append("Date: " + new Date() + "\r\n");
		sb.append("Server: meizu-server-0.1\r\n");
		sb.append("Accept-Ranges: bytes\r\n");
		sb.append("Content-Length: " + getBody().length + "\r\n");//TODO 目前是http1.0状态，这个属性可有可无
		sb.append("Content-Type: text/html\r\n");
		Set<Entry<String,String>> entryHead = responseHeader.entrySet();
		for (Entry<String, String> entry : entryHead) {
			sb.append(entry.getKey()+": "+entry.getValue()+"\r\n");
		}
		sb.append("Set-Cookie: "
				+ getResponseHeader().get("Set-Cookie") + "\r\n");
		sb.append("\r\n");
		return bw.append(sb.toString());
	}
	
	@Override
	public void setCharacterEncoding(String charset) {
		this.charset = charset;
	}
	
	@Override
	public void setContentType(String type) {
		this.contentType = type;
	}

	@Override
	public String getCharacterEncoding() {
		return charset;
	}

	@Override
	public String getContentType() {
		return contentType;
	}
	
	@Override
	public void setHeader(String name, String value) {
		responseHeader.put(name, value);
	}

	@Override
	public String getHeader(String name) {
		return responseHeader.get(name);
	}
	
	
	@Override
	public void setStatus(int sc) {
		setStatusCode(String.valueOf(sc));
		
	}

	@Override
	public void setStatus(int sc, String sm) {
		setStatusCode(String.valueOf(sc));
		
	}

	@Override
	public int getStatus() {
		return Integer.parseInt(getStatusCode());
	}
	
	
	
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}


	

	@Override
	public void setContentLength(int len) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContentLengthLong(long len) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void setBufferSize(int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetBuffer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocale(Locale loc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCookie(Cookie cookie) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsHeader(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String encodeURL(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeRedirectURL(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeUrl(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeRedirectUrl(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendError(int sc, String msg) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendError(int sc) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDateHeader(String name, long date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDateHeader(String name, long date) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void addHeader(String name, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIntHeader(String name, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addIntHeader(String name, int value) {
		// TODO Auto-generated method stub
		
	}

	

	

	@Override
	public Collection<String> getHeaders(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
