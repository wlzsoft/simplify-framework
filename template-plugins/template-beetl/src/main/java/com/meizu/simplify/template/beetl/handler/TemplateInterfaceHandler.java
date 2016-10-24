package com.meizu.simplify.template.beetl.handler;

import com.meizu.simplify.config.annotation.Config;
import com.meizu.simplify.ioc.IInterfaceHandler;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.HandleInterface;
import com.meizu.simplify.template.ITemplate;
import com.meizu.simplify.template.beetl.BeetlTemplate;

/**
  * <p><b>Title:</b><i>模版bean多实现类选择器</i></p>
 * <p>Desc: 暂时默认选用BeetlTemplate实现类，后续要考虑更灵活的方式,比如使用配置的方式来实现</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月30日 下午3:02:19</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月30日 下午3:02:19</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@HandleInterface(ITemplate.class)
public class TemplateInterfaceHandler implements IInterfaceHandler{
	
	
	@Config("system.templateBaseType")
	private String templateBaseType;
	@Override
	public Class<?> handle() {
		if(templateBaseType == null) {
			templateBaseType = BeetlTemplate.class.getName();
		}
		try {
			return Class.forName(templateBaseType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
