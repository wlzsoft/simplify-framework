package com.meizu.demo.system;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.config.info.Message;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.mvc.controller.BaseController;
import com.meizu.simplify.mvc.controller.IMethodSelector;
import com.meizu.simplify.mvc.model.Model;
/**
 * <p><b>Title:</b><i>动态方法选择器</i></p>
 * <p>Desc: 基于反射机制实现，性能校差,用于debug模式，方便开发阶段测试</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年04月28日 14:05:57</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年04月28日 14:05:57</p>
 * @author <a href="mailto:luchuangye@meizu.com">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class GenMethodSelector implements IMethodSelector{
	
	@Resource private com.meizu.demo.mvc.controller.TestController  testcontroller;
	
	@Override
	public <T extends Model> Object invoke(HttpServletRequest request,HttpServletResponse response, T t,BaseController<?>  obj,String doCmd, Object[] parameValue) throws IllegalAccessException, InvocationTargetException {
		Object result = null;
		String clazzName = obj.getClass().getSimpleName();
		try {
			switch(clazzName+":"+doCmd) {
				case "TestController:doTestWebsocketNotice":
					result = testcontroller.doTestWebsocketNotice(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doRestAjaxJson":
					result = testcontroller.doRestAjaxJson(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:ajaxjsonptest":
					result = testcontroller.ajaxjsonptest(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doVoidJson":
					testcontroller.doVoidJson(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:adoTestSelect2":
					result = testcontroller.adoTestSelect2(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doTestRedirect":
					result = testcontroller.doTestRedirect(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doTestVelocity":
					result = testcontroller.doTestVelocity(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doTestBeetl":
					result = testcontroller.doTestBeetl(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doTestWebsocket":
					result = testcontroller.doTestWebsocket(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doTestVoid":
					result = testcontroller.doTestVoid(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:adoTestSelect3":
					result = testcontroller.adoTestSelect3(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doDemo":
					result = testcontroller.doDemo(request, response ,(com.meizu.demo.mvc.model.TestModel)t,(java.lang.Integer)parameValue[3],(java.lang.String)parameValue[4],(java.lang.String)parameValue[5]);
					break;
				case "TestController:doTestHttl":
					result = testcontroller.doTestHttl(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doRestJson":
					result = testcontroller.doRestJson(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doTestVelocity2":
					result = testcontroller.doTestVelocity2(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doTestMessage":
					result = testcontroller.doTestMessage(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doTestSelect":
					result = testcontroller.doTestSelect(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doTestRedirect2":
					result = testcontroller.doTestRedirect2(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doTest":
					result = testcontroller.doTest(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doTestVelocity3":
					result = testcontroller.doTestVelocity3(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
				case "TestController:doTestJson":
					result = testcontroller.doTestJson(request, response ,(com.meizu.demo.mvc.model.TestModel)t);
					break;
			}
		} catch (Exception e) {
			Message.error(e.getMessage());
		}
		return result;
	}
}
