package com.meizu.simplify.codegen.resolver;

import com.meizu.simplify.codegen.CodeGenUtil;
import com.meizu.simplify.template.BeetlTemplate;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月28日 下午6:17:56</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年4月28日 下午6:17:56</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class MethodSelectorGenMain {
	public static void main(String[] args) {
//		Startup.start();
		CodeGenUtil gen = new CodeGenUtil(new BeetlTemplate());
		MethodSelectorGenBuild.init("com.meizu.demo.mvc.controller", gen);
	}
}
