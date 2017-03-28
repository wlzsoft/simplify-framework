package com.meizu.simplify.webserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.utils.DateUtil;
import com.meizu.simplify.utils.enums.DateFormatEnum;

import io.netty.channel.ChannelHandlerContext;

public class HttpResponseImpl implements HttpServletResponse{
	private String version = "HTTP/1.1";// 版本默认值
	private String statusCode;
	private String reason;// 原因短语

	private Map<String, String> responseHeader = new HashMap<String, String>();

	private char[] body;

	private String charset;
	private String contentType;
	private ServletOutputStreamImpl outputStream;
	public HttpResponseImpl(ChannelHandlerContext ctx) throws IOException {
        outputStream = new ServletOutputStreamImpl(ctx,this);
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
	
	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriterImpl(outputStream);
	}
	
	/**
	 * 设置netty的字符编码
	 */
	@Override
	public void setCharacterEncoding(String charset) {
		this.charset = charset;
	}
	
	/**
	 * 设置netty的页面内容格式类型
	 */
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
	
	/**
	 * 设置netty的http头信息
	 */
	@Override
	public void setHeader(String name, String value) {
		responseHeader.put(name, value);
	}

	@Override
	public String getHeader(String name) {
		return responseHeader.get(name);
	}
	
	/**
	 * 设置netty的http状态码
	 */
	@Override
	public void setStatus(int sc) {
		setStatusCode(String.valueOf(sc));
		
	}

	/**
	 * 设置netty的http状态码和状态信息
	 */
	@Override
	public void setStatus(int sc, String sm) {
		setStatusCode(String.valueOf(sc));
		
	}

	@Override
	public int getStatus() {
		return Integer.parseInt(getStatusCode());
	}
	
	/**
	 * netty返回操作的outputstream
	 */
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return outputStream;
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
		setStatus(sc);
		outputStream.print(msg);
	}

	@Override
	public void sendError(int sc) throws IOException {
		sendError(sc, "错误");
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void setDateHeader(String name, long date) {
		this.responseHeader.put(name, DateUtil.format(date, DateFormatEnum.YEAR_TO_MILLISECOND));
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
