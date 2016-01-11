package com.meizu.simplify.utils;

import org.junit.Assert;
import org.junit.Test;

import com.meizu.simplify.utils.demo.DemoService;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月11日 下午7:07:02</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月11日 下午7:07:02</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class ReflectionUtilsTest {
	@Test
	public void invokeMethod() {
		Object obj = new DemoService();
		Assert.assertEquals("lcy",ReflectionUtils.invokeMethod(obj, "demoMessage"));
	}
}
