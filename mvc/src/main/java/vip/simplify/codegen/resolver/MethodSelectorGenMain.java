package vip.simplify.codegen.resolver;

import vip.simplify.codegen.CodeGenUtil;
import vip.simplify.template.beetl.BeetlTemplate;

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
		System.out.println("controll需要生成代码的扫描目录:"+args[0]);
		MethodSelectorGenBuild.init(args[0], gen);
	}
}