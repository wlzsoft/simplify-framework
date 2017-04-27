package vip.simplify.codegen;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.template.ITemplate;
import vip.simplify.utils.FileUtil;

/**
  * <p><b>Title:</b><i>模版渲染生成文件工具类</i></p>
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
@Bean
public class CodeGenUtil {
	@Inject
	private ITemplate template;
	
	public CodeGenUtil() {
		
	}
	public CodeGenUtil(ITemplate template) {
		this.template = template;
	}
	public void gen(Map<String, Object> parameters, String outDir,String javaFileName) {
		try {
			FileUtil.createDirectory(outDir);
			String javafileinfo = template.render(parameters, javaFileName, "/codegen/",".template");
			File file = FileUtil.createFile(outDir, javaFileName, true);
			FileUtil.saveFile(file, javafileinfo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
