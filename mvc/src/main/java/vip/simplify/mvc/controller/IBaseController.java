package vip.simplify.mvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vip.simplify.ioc.BeanFactory;
import vip.simplify.mvc.model.Model;

/**
  * <p><b>Title:</b><i>请求处理器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年6月29日 下午5:44:59</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年6月29日 下午5:44:59</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IBaseController<T extends Model> {
//	@Inject//注入静态变量代替BeanFactory.getBean
//	public static DelegateController<?> baseController = null;
	
	/**
	 * 方法用途: 开始处理请求<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @return
	 */
	public Object exec(HttpServletRequest request,HttpServletResponse response);
	
	@SuppressWarnings("unchecked")
	public default void process(HttpServletRequest request, HttpServletResponse response,String requestUrl,String requestMethodName,boolean isStatic,String[] urlparams) {
		DelegateController<T> baseController = BeanFactory.getBean(DelegateController.class);//这种写法有性能消耗,待优化,Controller.get会走这个逻辑 TODO
		baseController.process(request, response, requestUrl, requestMethodName,isStatic, urlparams,this);
		//((DelegateController<T>)baseController).process(request, response, requestUrl, requestMethodName,isStatic, urlparams,this);
	}
	
	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param cmd 注意cmd是controller方法的名称，而不是@RequestMap注解的path值, TODO 后续统一
	 * @param model
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public default boolean checkPermission(HttpServletRequest request, HttpServletResponse response, String cmd, String requestUrl, T model) throws ServletException, IOException {
		return true;
	}
}
