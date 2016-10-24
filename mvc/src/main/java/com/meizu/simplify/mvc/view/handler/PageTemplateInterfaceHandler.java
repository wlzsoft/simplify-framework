package com.meizu.simplify.mvc.view.handler;

import com.meizu.simplify.Constants;
import com.meizu.simplify.config.annotation.Config;
import com.meizu.simplify.ioc.IInterfaceHandler;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.HandleInterface;
import com.meizu.simplify.view.IPageTemplate;

/**
  * <p><b>Title:</b><i>页面模版bean多实现类选择器</i></p>
 * <p>Desc: TODO</p>
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
@HandleInterface(IPageTemplate.class)
public class PageTemplateInterfaceHandler implements IInterfaceHandler{
	
	@Config("system.templateType")
	private String templateType;
	@Override
	public Class<?> handle() {
		if(templateType == null) {
			templateType = Constants.packagePrefix+".simplify.mvc.view.JspTemplate";
		}
		try {
			return Class.forName(templateType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
