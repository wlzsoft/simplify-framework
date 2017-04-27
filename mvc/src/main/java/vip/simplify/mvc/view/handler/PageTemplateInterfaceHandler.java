package vip.simplify.mvc.view.handler;

import vip.simplify.Constants;
import vip.simplify.config.annotation.Config;
import vip.simplify.ioc.IInterfaceHandler;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.HandleInterface;
import vip.simplify.view.IPageTemplate;

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
public class PageTemplateInterfaceHandler implements IInterfaceHandler {
	
	@Config("system.templateType")
	private String templateType;
	@Override
	public Class<?> handle() {
		if(templateType == null) {
			templateType = Constants.packagePrefix+".mvc.view.JspTemplate";
		}
		try {
			return Class.forName(templateType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
