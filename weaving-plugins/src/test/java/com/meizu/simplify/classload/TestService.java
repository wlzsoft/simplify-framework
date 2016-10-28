package com.meizu.simplify.classload;

import com.meizu.simplify.classload.entity.TestUser;

/**
 * <p><b>Title:</b><i>被加载的测试类</i></p>
* <p>Desc: 用于测试自定义classload的加载</p>
* <p>source folder:{@docRoot}</p>
* <p>Copyright:Copyright(c)2014</p>
* <p>Company:meizu</p>
* <p>Create Date:2016年1月6日 上午11:08:36</p>
* <p>Modified By:luchuangye-</p>
* <p>Modified Date:2016年1月6日 上午11:08:36</p>
* @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
* @version Version 0.1
*
*/
public class TestService {
	public void getName() {
		TestUser testUser = new TestUser();
		testUser.setName("lcy");
		System.out.println("test10 "/* +testUser.getName() */);
	}
}
