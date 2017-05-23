package vip.simplify.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><b>Title:</b><i>Ajax 跨域访问注解设置--基于CORS(跨域资源共享,只支持get和post,移动端支持很好，ie6这种太老的浏览器无法支持)机制，区别于jsonp</i></p>
 * <p>Desc: 服务端设置允许跨域访问的域名:
 *                区分于jsonp跨域访问方式-jsonp访问方式不属于ajax访问方式，是阻塞性的请求
 *          常见的跨域方案有：JSONP、flash、ifame、xhr2,cors</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月20日 下午7:39:40</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月20日 下午7:39:40</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AjaxAccess {
	
	/**
	 * 
	 * 方法用途: 设置response头信息Access-Control-Allow-Origin，基于http1.1协议<br>
	 * 操作步骤: 设置运行跨域访问的域名地址，如果所有第三方域名都可以跨域访问，那么值为 符号星号<code> *</code> <br>
	 * @return
	 */
	String allowOrigin();
	
	/**
	 * 
	 * 方法用途: 设置reponse头信息Access-Control-Allow-Headers基于http1.1协议<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String allowHeaders() default "";
	
	/**
	 * 
	 * 方法用途: 设置reponse头信息Access-Control-Max-Age基于http1.1协议<br>
	 * 操作步骤: 使用 Access-Control-Max-Age 来缓存跨域访问中的请求 TODO<br>
	 * @return
	 */
	int maxAge() default Integer.MAX_VALUE;//3600
	
	/**
	 * 
	 * 方法用途: 设置reponse头信息Access-Control-Allow-Credentials基于http1.1协议<br>
	 * 操作步骤: 使用 Access-Control-Allow-Credentials 来控制跨域访问，支持跨域发送cookies
	 *           Ajax访问中js库，比如jquery需要设置 xhrFields:{withCredentials: true}和crossDomain:true这两个属性
	 *           jquery使用样例：$.ajax({url:"http://www.xxx.com/list.json",async:true,xhrFields:{withCredentials: true},crossDomain:true});
	 *           原生js使用方式：var xhr = new XMLHttpRequest();xhr.open("POST", "http://www.xxx.com/list.json", true);xhr.withCredentials = true;xhr.send();<br>
	 * @return
	 */
	boolean allowCredentials() default false;
	
	/**
	 * 
	 * 方法用途: 设置reponse头信息Access-Control-Allow-Methods基于http1.1协议<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	Methods[] allowMethods() default { Methods.Get, Methods.Post };
	
	public enum Methods {
		Get, Post, Put, Delete, Xmodify
	}
}