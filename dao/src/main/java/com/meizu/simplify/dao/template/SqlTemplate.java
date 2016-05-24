package com.meizu.simplify.dao.template;

import java.io.IOException;
import java.util.Map;

import com.meizu.simplify.config.annotation.Config;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.template.ITemplate;
import com.meizu.simplify.utils.StringUtil;

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
	@Resource
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
