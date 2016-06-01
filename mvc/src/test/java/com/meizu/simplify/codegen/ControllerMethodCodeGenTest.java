package com.meizu.simplify.codegen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.meizu.simplify.template.BeetlTemplate;
import com.meizu.simplify.template.ITemplate;

/**
  * <p><b>Title:</b><i>controller的方法地址匹配映射代码生成类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月26日 下午5:30:26</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年4月26日 下午5:30:26</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class ControllerMethodCodeGenTest {
	@Test
	public void test() throws IOException {
		ITemplate template = new BeetlTemplate();
		Map<String, Object> parameters = new HashMap<>();
		List<Map<String,String>> controllerTagList = new ArrayList<>();
		Map<String,String> map = new HashMap<>();
		map.put("clazz", "com.meizu.test.Test");
		map.put("value", "test1");
		controllerTagList.add(map);
		Map<String,String> map2 = new HashMap<>();
		map2.put("clazz", "com.meizu.test.Test");
		map2.put("value", "test2");
		controllerTagList.add(map2);
		parameters.put("controllerTagList",controllerTagList);
		String javafileinfo = template.render(parameters, "GenMethodSelector.java", "/codegen/",".template");
//		System.out.println(javafileinfo);
		Assert.assertNotEquals(javafileinfo, "");
	}
}
