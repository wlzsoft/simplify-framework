package com.meizu.simplify.mvc.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.utils.CollectionUtil;

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
	public Object invoke(String doCmd, Object[] parameValue) throws IllegalAccessException, InvocationTargetException {
		//代码生成区域start
		Method[] methods = this.getClass().getMethods();
		Method method = CollectionUtil.getItem(methods,doCmd, (m,w) -> doCmd.equals(m.getName()));
		if (method == null) {
			throw new IllegalArgumentException("无法找到指定类:["+this.getClass()+"] 的方法 :[" + doCmd + "]"); 
		}
		if (method.getParameterTypes().length < 3) { //考虑model问题，后续可以做更灵活调整
			throw new IllegalArgumentException("类:["+this.getClass()+"] 的方法 :[" + doCmd + "]的参数的长度不能小于3" ); 
		}
		Object obj = method.invoke(this,parameValue);
		return obj;
	}
}
