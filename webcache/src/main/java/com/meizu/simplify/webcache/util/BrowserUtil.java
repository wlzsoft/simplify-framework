package com.meizu.simplify.webcache.util;

import java.time.Clock;

import javax.servlet.http.HttpServletResponse;


/**
 * <p><b>Title:</b><i>浏览器相关工具</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 下午12:59:28</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 下午12:59:28</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class BrowserUtil {
	

	public static void disableBrowerCache(HttpServletResponse response) {
		//不允许浏览器端或缓存服务器缓存当前页面信息。
		response.setHeader("version","ros2.0");
        response.setHeader("Pragma","no-cache");//http1.0协议缓存设置
        response.setHeader("Cache-Control","no-cache, no-store,must-revalidate, max-age=0");//http1.1 协议设置  TODO 需要测试下 在nginx做代理的情况，缓存是否可用。重点针对must-revalidate参数
//        response.addHeader( "Cache-Control", "no-cache" );//浏览器和缓存服务器都不应该缓存页面信息
//        response.addHeader( "Cache-Control", "no-store" );//请求和响应的信息都不应该被存储在对方的磁盘系统中；    
//        response.addHeader( "Cache-Control", "must-revalidate" );//对于客户机的每次请求，代理服务器必须想服务器验证缓存是否过时；
//        response.setDateHeader("Expires", 0);   
        response.setDateHeader("Expires", -10);//TODO
        response.setHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
        
	}

	public static void enableBrowerCache(HttpServletResponse response,long time) {
        //Date date = new Date();    
        Clock clock = Clock.systemDefaultZone();
//        Instant instant = clock.instant();
//        Date date = Date.from(instant);
//        response.setHeader("Connection","keep-alive");
        //response.setHeader("Content-Encoding","gzip");
        response.setDateHeader("Last-Modified",clock.millis()+time*1000); //Last-Modified:页面的最后生成时间  TODO
        response.setDateHeader("Expires",clock.millis()+time*1000); //Expires:过时期限值 ，时限为time秒 TODO 
        response.setHeader("Cache-Control", "public"); //Cache-Control来控制页面的缓存与否,public:浏览器和缓存服务器都可以缓存页面信息；
        response.setHeader("Pragma", "Pragma"); //Pragma:设置页面是否缓存，为Pragma则缓存，no-cache则不缓存
//      response.setHeader("Content-Type", "text/html;charset=utf-8"); 
//      response.setIntHeader("Content-Length", 7228); 
		
	}
	
}
