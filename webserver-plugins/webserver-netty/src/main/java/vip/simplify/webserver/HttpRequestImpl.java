package vip.simplify.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import io.netty.handler.codec.http.HttpRequest;

public class HttpRequestImpl implements HttpServletRequest{
	
	
	private String method;// POST 或 GET
	private String requestURI;
	private String version;// 请求版本
	private final Map<String, String> cookies = new HashMap<>();
	private Map<String, String> requestHeader = new HashMap<>();
	private final Map<String, String> parameters = new HashMap<>();//存储post和get请求的参数值 
	private final Map<String, Object> attributes = new HashMap<>();
	private char[] body;

	private HttpSessionImplWrapper session;

	// 解析请求行
	public void parseRequestLine(String line)  {
		String[] strs = line.split(" ");
		if (strs.length != 3) {
			throw new RuntimeException("无效的请求行");
		}
		method = strs[0].trim();
		requestURI = strs[1].trim();
		version = strs[2].trim();
		//throw new RuntimeException("无效的请求行，必须包含请求方法，请求地址和请求的协议");

		//额外的，如果是url中带有参数，需要做处理，一般是由get携带，当然的method也可以携带参数在url中
		String[] requestUrlArr = requestURI.split("\\?");
		if(requestUrlArr.length>1) {
			requestURI = requestUrlArr[0];
			String[] parameters = requestUrlArr[1].split("&");
			for (String str : parameters) {
				String[] datas = str.split("=");
				getParameters().put(datas[0], datas[1]);
			}
		}
		
		
	}
	
	// 解析请求行
	public void parseRequestLine(HttpRequest request)  {
		method = request.method().name();
		requestURI = request.uri();
		version = request.protocolVersion().text();
		//throw new RuntimeException("无效的请求行，必须包含请求方法，请求地址和请求的协议");
		
		//额外的，如果是url中带有参数，需要做处理，一般是由get携带，当然的method也可以携带参数在url中
		String[] requestUrlArr = requestURI.split("\\?");
		if(requestUrlArr.length>1) {
			requestURI = requestUrlArr[0];
			String[] parameters = requestUrlArr[1].split("&");
			for (String str : parameters) {
				String[] datas = str.split("=");
				getParameters().put(datas[0], datas[1]);
			}
		}
		
		
	}

	/**
	 * 
	 * 方法用途: 读入开始行<br>
	 * 操作步骤: TODO<br>
	 * @param line
	 */
	public void parseRequestHeader(String line) {
		String[] strs = line.split(":", 2);// 只匹配一次冒号，从左到右
		if (strs.length == 2) {
			if (strs[0].trim().equals("Cookie")) {
				String[] cookies = strs[1].split(";");
				for (String str : cookies) {
					String[] values = str.split("=");
					if (values.length == 2) {
						this.cookies.put(values[0].trim(), values[1].trim());// 判断是否有cookies，如果有分割的方式就不一样，注意去掉空格
					}
				}
			} else {
				requestHeader.put(strs[0].trim(), strs[1].trim());
			}

		}
	}

	public String getParameter(String name) {

		return parameters.get(name);

	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, String> getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(Map<String, String> requestHeader) {
		this.requestHeader = requestHeader;
	}

	public char[] getBody() {
		return body;
	}

	public void setBody(char[] body) {
		this.body = body;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}



	public void setSession(HttpSessionImplWrapper session) {
		this.session = session;
	}
	

	public Map<String, String> getCookiesMap() {
		return cookies;
	}
	
	@Override
	public Cookie[] getCookies() {
		List<Cookie> list = new ArrayList<>();
		for(Entry<String, String> cookieEntry: cookies.entrySet()) {
			Cookie cookie = new Cookie(cookieEntry.getKey(), cookieEntry.getValue());
			list.add(cookie);
		}
		return list.toArray(new Cookie[list.size()]);
	}
	@Override
	public HttpSession getSession(boolean create) {
		
		return null;
	}

	@Override
	public HttpSession getSession() {
		return session;
	}
	
	@Override
	public void setAttribute(String name, Object o) {
		attributes.put(name, o);
	}
	
	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}
	

	@Override
	public Enumeration<String> getAttributeNames() {
		return new Enumeration<String>() {
			private Iterator<Entry<String,Object>> set = attributes.entrySet().iterator();
			@Override
			public boolean hasMoreElements() {
				return set.hasNext();
			}

			@Override
			public String nextElement() {
				return set.next().getKey();
			}};
	}

	@Override
	public String getHeader(String name) {
		return requestHeader.get(name);
	}
	
	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getContentLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getContentLengthLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getParameterValues(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProtocol() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScheme() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void removeAttribute(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<Locale> getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRealPath(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLocalPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
			throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AsyncContext getAsyncContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public long getDateHeader(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	

	@Override
	public Enumeration<String> getHeaders(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIntHeader(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContextPath() {
		return "";
	}

	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUserInRole(String role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String changeSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void login(String username, String password) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logout() throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Part getPart(String name) throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}
		

}
