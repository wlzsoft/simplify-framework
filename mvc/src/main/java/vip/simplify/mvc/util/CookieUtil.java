package vip.simplify.mvc.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p><b>Title:</b><i>Cookie管理</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月25日 下午6:22:31</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月25日 下午6:22:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class CookieUtil {
	
	/**
	 * 方法用途: 写多个cookie<br>
	 * 实现步骤: 不管是否存在cookie重复写入<br>
	 * @param cookieMap cookie值MAP
	 * @param maxAge 超时时间
	 * @param response 响应对象
	 */
	public static void write(Map<String, String> cookieMap, int maxAge, HttpServletResponse response) {
		for (String key : cookieMap.keySet()) {
			write(key, cookieMap.get(key), maxAge, response);
		}
	}
	
	/**
	 * 方法用途: 写cookie<br>
	 * 实现步骤: 不管是否存在cookie重复写入<br>
	 * @param key 键
	 * @param value 值
	 * @param maxAge 超时时间
	 * @param response 响应对象
	 */
	public static void write(String key, String value, int maxAge, HttpServletResponse response) {
		write(key, value, maxAge, response, null);
	}
	
	/**
	 * 
	 * 方法用途: 写cookie<br>
	 * 操作步骤: 1.如果cookie已经存在，那么删除，2.写入cookie<br>
	 * @param key
	 * @param value
	 * @param maxAge
	 * @param response
	 * @param request
	 */
	public static void write(String key, String value, int maxAge, HttpServletResponse response, HttpServletRequest request) {
		write(key, value, maxAge, null, false, response, request);
	}
	
	/**
	 * 
	 * 方法用途: 写cookie<br>
	 * 操作步骤: 1.如果cookie已经存在，那么删除，2.写入cookie<br>
	 * @param name 键
	 * @param value 值
	 * @param maxAge 超时时间
	 * @param domain 限定域名
	 * @param response 响应对象
	 * @param request 请求对象
	 */
	public static void write(String name, String value, int maxAge, String domain, Boolean secure, HttpServletResponse response, HttpServletRequest request) {
		Cookie cookie = new Cookie(name, value);
		cookie.setHttpOnly(true);
		cookie.setSecure(secure);
		cookie.setVersion(1);
		cookie.setMaxAge(maxAge);
		if (domain != null) {
			cookie.setDomain(domain);
		}
		write(cookie, response, request);
	}
	
	/**
	 * 
	 * 方法用途: 写cookie<br>
	 * 操作步骤: 1.如果cookie已经存在，那么删除，2.写入cookie<br>
	 * @param name 键
	 * @param value 值
	 * @param domain 限定域名
	 * @param response 响应对象
	 * @param request 请求对象
	 */
	public static void write(String name, String value, String domain, Boolean secure, Boolean httpOnly, HttpServletResponse response, HttpServletRequest request) {
		Cookie cookie = new Cookie(name, value);
		try {
			cookie.setHttpOnly(httpOnly);
		} catch (NoSuchMethodError e) {
			e.printStackTrace();
		}
		cookie.setSecure(secure);
		cookie.setVersion(1);
		if (domain != null) {
			cookie.setDomain(domain);
		}
		write(cookie, response, request);
	}
	
	/**
	 * 
	 * 方法用途: 写cookie<br>
	 * 操作步骤: 1.如果cookie已经存在，那么删除，2.写入cookie<br>
	 * @param name 键
	 * @param value 值
	 * @param domain 限定域名
	 * @param response 响应对象
	 * @param request 请求对象
	 */
	public static void write(String name, String value, String domain, Boolean secure, HttpServletResponse response, HttpServletRequest request) {
		write(name, value, domain, secure, true, response, request);
	}
	
	/**
	 * 
	 * 方法用途: 写cookie<br>
	 * 操作步骤: 1.如果cookie已经存在，那么删除，2.写入cookie<br>
	 * @param cookie 
	 * @param response 响应对象
	 * @param request 请求对象
	 */
	public static void write(Cookie cookie, HttpServletResponse response, HttpServletRequest request) {
		if (request != null) {
			if (read(cookie.getName(), request) != null) {
				remove(cookie.getName(), cookie.getDomain(), request, response);
			}
		}
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	/**
	 * 方法用途: 读单独cookie值方法<br>
	 * 实现步骤: <br>
	 * @param key 键
	 * @param request 请求对象
	 * @return
	 */
	public static String read(String key, HttpServletRequest request) {
		Map<String, String> cookieMap = read(request);
		return cookieMap.get(key);
	}
	
	/**
	 * 方法用途: 读所有cookie方法<br>
	 * 实现步骤: <br>
	 * @param request 请求对象
	 * @return
	 */
	public static Map<String, String> read(HttpServletRequest request) {
		Map<String, String> cookieMap = new HashMap<String, String>();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie.getValue());
			}
		}
		return cookieMap;
	}
	
	/**
	 * 
	 * 方法用途: 删除cookie<br>
	 * @param key 键
	 * @param request 
	 * @param response 
	 */
	public static void remove(String key, String domain, HttpServletRequest request, HttpServletResponse response) {
		remove(key, null, domain, request, response);
	}
	
	/**
	 * 
	 * 方法用途: 删除cookie<br>
	 * @param key 键
	 * @param request 
	 * @param response 
	 */
	public static void remove(String key, HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return;
		}
		for (Cookie cookie : cookies) {
			if (key.equals(cookie.getName())) {
				cookie = new Cookie(cookie.getName(), null);
				// cookie.setMaxAge(-1);//不存储到磁盘上，浏览器关闭就失效。
				cookie.setMaxAge(0);// 不存储这个cookie，通知删除cookie，会马上生效
				// cookie.setPath("/");//不设置路径，默认删除当前路径的cookie
				// cookie.setDomain("meizu.com");// TODO
				response.addCookie(cookie);
			}
		}

	}
	
	/**
	 * 
	 * 方法用途: 删除cookie<br>
	 * 操作步骤: TODO<br>
	 * @param key  键
	 * @param path
	 * @param domain
	 * @param request
	 * @param response
	 */
	public static void remove(String key, String path, String domain, HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return;
		}
		for (Cookie cookie : cookies) {
			if (key.equals(cookie.getName())) {
				cookie = new Cookie(cookie.getName(), null);
				// cookie.setMaxAge(-1);//不存储到磁盘上，浏览器关闭就失效。
				cookie.setMaxAge(0);// 不存储这个cookie，通知删除cookie，会马上生效
				if (path != null) {
					cookie.setPath(path);// 不设置路径，默认删除当前路径的cookie
				}
				cookie.setDomain(domain);
				response.addCookie(cookie);
			}
		}
	}
	
}
