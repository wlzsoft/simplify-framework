package com.meizu.demo.system;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.demo.mvc.controller.TestController;
import com.meizu.demo.mvc.model.TestModel;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.mvc.controller.BaseController;
import com.meizu.simplify.mvc.controller.IMethodSelector;

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
public class GenMethodSelector implements IMethodSelector{
	
	@Override
	public Object invoke(BaseController<?>  obj,String doCmd, Object[] parameValue) throws IllegalAccessException, InvocationTargetException {
		Object result = null;
		switch(doCmd) {
			case "doTestJson":
				TestController tc = (TestController)obj;
				result = tc.doTestJson((HttpServletRequest)parameValue[0], (HttpServletResponse)parameValue[1], (TestModel)parameValue[2]);
				break;
		}
		/*Class<?> clazz = obj.getClass();
		Method[] methods = clazz.getMethods();
		Method method = CollectionUtil.getItem(methods,doCmd, (m,w) -> doCmd.equals(m.getName()));
		Object result = method.invoke(obj,parameValue);*/
		return result;
	}
}
