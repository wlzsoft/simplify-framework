package vip.simplify.dao.template;

import java.io.IOException;
import java.util.Map;

import vip.simplify.config.annotation.Config;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.template.ITemplate;
import vip.simplify.utils.StringUtil;

/**
  * <p><b>Title:</b><i>sql模版渲染</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月5日 下午6:08:34</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月5日 下午6:08:34</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class SqlTemplate {
	@Inject
	private ITemplate template;
	@Config("system.sqlTemplatePath")
	private String sqlTemplatePath;
	public String render(String sqlName,Map<String, Object> parameters) {
		if(StringUtil.isBlank(sqlTemplatePath)) {
			sqlTemplatePath = "";
		}
		try {
			String sql = template.render(parameters, sqlName, sqlTemplatePath,".sql");
			return sql;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
