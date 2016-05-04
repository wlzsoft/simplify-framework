package com.meizu.simplify.codegen;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.template.ITemplate;
import com.meizu.simplify.utils.FileUtil;

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
@Bean
public class ControllerMethodCodeGen {
	@Resource
	private ITemplate template;
	
	public ControllerMethodCodeGen() {
		
	}
	public ControllerMethodCodeGen(ITemplate template) {
		this.template = template;
	}
	public void gen(Map<String, Object> parameters, String outDir,String javaFileName) {
		try {
			FileUtil.createDirectory(outDir);
			String javafileinfo = template.render(parameters, javaFileName, "/codegen/","html");
			File file = FileUtil.createFile(outDir, javaFileName, true);
			FileUtil.saveFile(file, javafileinfo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
