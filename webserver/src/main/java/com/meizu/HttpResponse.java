package com.meizu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpResponse implements HttpServletResponse{
	private String version = "HTTP/1.1";// 版本默认值
	private String statusCode;
	private String reason;// 原因短语

	private Map<String, String> responseHeader = new HashMap<String, String>();

	private char[] body;

	private final PrintWriter bw;
	private String charset;
	private String contentType;
	
	public HttpResponse(Socket socket) throws IOException {
		bw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
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
			bw.write(getVersion() + " " + getStatusCode() + " "
					+ getReason() + "\r\n");
			bw.write("Date: " + new Date() + "\r\n");
			bw.write("Server: meizu-server-0.1\r\n");
			bw.write("Accept-Ranges: bytes\r\n");
			bw.write("Content-Length: " + getBody().length + "\r\n");
			bw.write("Content-Type: text/html\r\n");
			bw.write("Set-Cookie: "
					+ getResponseHeader().get("Set-Cookie") + "\r\n");// 把cookie写上去
			bw.write("\r\n");
			bw.write(getBody());
		}
		bw.flush();
		bw.close();
	}
	
	@Override
	public PrintWriter getWriter() throws IOException {
		bw.append(getVersion() + " " + getStatusCode() + " "
				+ getReason() + "\r\n");
		bw.append("Date: " + new Date() + "\r\n");
		bw.append("Server: meizu-server-0.1\r\n");
		bw.append("Accept-Ranges: bytes\r\n");
		bw.append("Content-Length: " + "getBody().length" + "\r\n");
		bw.append("Content-Type: text/html\r\n");
		bw.append("Set-Cookie: "
				+ getResponseHeader().get("Set-Cookie") + "\r\n");// 把cookie写上去
		bw.append("\r\n");
		return bw;
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
	public void setHeader(String name, String value) {
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
	public void setStatus(int sc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatus(int sc, String sm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getHeader(String name) {
		// TODO Auto-generated method stub
		return null;
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
