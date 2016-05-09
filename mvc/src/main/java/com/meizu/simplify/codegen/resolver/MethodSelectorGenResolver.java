package com.meizu.simplify.codegen.resolver;

import java.util.List;

import com.meizu.simplify.codegen.CodeGenUtil;
import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;

/**
  * <p><b>Title:</b><i>controll方法代码生成初始化解析</i></p>
 * <p>Desc: TODO注意：暂不启用，目前是有maven控制，不是容器启动控制</p>
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
@Init(InitTypeEnum.METHOD_GEN)
public class MethodSelectorGenResolver implements IAnnotationResolver<Class<?>>{
	private PropertiesConfig config = BeanFactory.getBean(PropertiesConfig.class);
	private CodeGenUtil gen = BeanFactory.getBean(CodeGenUtil.class);
	
	private String classPath; 
	
	@Override	
	public void resolve(List<Class<?>> resolveList) {
		//TODO 正在使用打开
//		classPath = config.getControllerClasspath(); 
//		MethodSelectorGenBuild.init(classPath, gen);
	}
	
	
}
