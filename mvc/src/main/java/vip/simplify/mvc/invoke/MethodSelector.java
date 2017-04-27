package vip.simplify.mvc.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.mvc.controller.IBaseController;
import vip.simplify.mvc.model.Model;
import vip.simplify.utils.CollectionUtil;

/**
  * <p><b>Title:</b><i>动态方法选择器</i></p>
 * <p>Desc: 基于反射机制实现，性能校差,用于debug模式，方便开发阶段测试</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月26日 上午11:35:50</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年4月26日 上午11:35:50</p>
 * @author <a href="mailto:luchuangye@meizu.com">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class MethodSelector implements IMethodSelector{
	
	@Override
	public <T extends Model> Object invoke(HttpServletRequest request,HttpServletResponse response, T t,IBaseController<?> obj,String requestMethodName, Object[] parameValue) throws IllegalAccessException, InvocationTargetException {
		parameValue[0] = request;
		parameValue[1] = response;
		parameValue[2] = t;
		Class<?> clazz = obj.getClass();
		Method[] methods = clazz.getMethods();
		Method method = CollectionUtil.getItem(methods,requestMethodName, (m,w) -> requestMethodName.equals(m.getName()));
		if (method == null) {
			throw new IllegalArgumentException("无法找到指定类:["+clazz+"] 的方法 :[" + requestMethodName + "]"); 
		}
		if (method.getParameterTypes().length < 3) { //考虑model问题，后续可以做更灵活调整
			throw new IllegalArgumentException("类:["+clazz+"] 的方法 :[" + requestMethodName + "]的参数的长度不能小于3" ); 
		}
		Object result = method.invoke(obj,parameValue);
		return result;
	}
}
