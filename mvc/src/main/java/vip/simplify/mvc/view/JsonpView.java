package vip.simplify.mvc.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vip.simplify.config.PropertiesConfig;
import vip.simplify.mvc.model.Model;
import vip.simplify.util.JsonResolver;
import vip.simplify.utils.StringUtil;


/**
 * <p><b>Title:</b><i>json处理返回方式</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:26:20</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:26:20</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public  class  JsonpView {

	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param obj
	 * @param model
	 * @param domain 通过cookie获取的名字为domain，可以跨域的域名
	 * @throws ServletException
	 * @throws IOException
	 */
	public static <T extends Model> void exe(HttpServletRequest request, HttpServletResponse response, Object obj, T model, String domain, PropertiesConfig config, JsonResolver jsonResolver)
			throws ServletException, IOException {
		
		String message = jsonResolver.ObjectToString(obj);
		//可以通过请求头来限制不合理的请求(request head referer) TODO
		if (model.getScript() == 1) { //form提交到iframe后，自动执行方式,必须在父级域名下，才能调用iframe的js函数
			//注意：错误一：Uncaught DOMException: Blocked a frame with origin "http://xxxx.xx:8080" from accessing a cross-origin frame. 
			//      (出现概率高)第一个原因：以上错误在同域情况下报错的，是因为parent没有对应的方法定义，或是方法的参数对应不上
			//      第二个原因：由于跨域问题，或是document.domain没有设置
			//      第三个原因：document.domain 需要在两个互相访问的页面中的值是一致，并且是当前页的域名或是其基础域名，如果两个页面设置不一致，也会导致以上问题
			//                 同域情况下，可以不指定document.domain的值,如果有一方设置了值，就是值和原来一样，也会导致以上错误
			if(domain != null) {
				message = StringUtil.format("<script>document.domain='{0}';{1}({2});</script>", domain, model.getCallback(), message);
			} else {
				message = StringUtil.format("<script>{0}({1});</script>", model.getCallback(), message);
			}
//		} else if (model.getScript() == 2) {//通过js 的 document.write 来实现wiget
		} else {
			if(null==model.getCallback()||model.getCallback().equals("")){
				model.setCallback("jsonp");
			}
			//message = StringUtil.format("{0}({1})", model.getCallback(), message);
			message = StringUtil.format("<script>{0}({1});</script>", model.getCallback(), message);
		}
		response.setCharacterEncoding(config.getCharset());
		response.setContentType("text/html; charset=" + config.getCharset());
//		response.setContentType("text/javascript; charset=" + config.getCharset());
		response.getWriter().print(message);
	}
}
