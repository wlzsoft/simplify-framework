package vip.simplify.mvc.controller;

import vip.simplify.mvc.resolver.ControllerAnnotationResolver;

/**
  * <p><b>Title:</b><i>请求控制器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年6月29日 下午5:41:44</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年6月29日 下午5:41:44</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class Controller {
	
	/**
	 * 方法用途: 启用一个请求映射，以接收浏览器请求<br>
	 * 操作步骤: TODO<br>
	 * @param requestMapUrl
	 * @param handler
	 */
	public static <T> void get(String requestMapUrl,IBaseController<?> handler){
		//设置映射关系，目的是开启请求地址映射
		ControllerAnnotationResolver.addRequestInfo("exec",false, "vip.simplify", handler, requestMapUrl);
	}
}
