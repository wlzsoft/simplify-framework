package com.meizu.simplify.mvc.invoke;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.ioc.annotation.DefaultBean;
import com.meizu.simplify.mvc.controller.IBaseController;
import com.meizu.simplify.mvc.model.Model;

/**
  * <p><b>Title:</b><i>controller方法选择器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月26日 上午11:37:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年4月26日 上午11:37:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@DefaultBean
public interface IMethodSelector {
	/**
	 * 方法用途: controller方法选择处理<br>
	 * 操作步骤: 用于请求地址的路由<br>
	 * @param request
	 * @param response
	 * @param t
	 * @param obj
	 * @param requestMethodName
	 * @param parameValue
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public <T extends Model> Object invoke(HttpServletRequest request,HttpServletResponse response, T t,IBaseController<?> obj,String requestMethodName, Object[] parameValue) throws IllegalAccessException, InvocationTargetException;
}
