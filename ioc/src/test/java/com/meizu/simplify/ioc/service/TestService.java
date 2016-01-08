package com.meizu.simplify.ioc.service;

import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月7日 下午3:15:25</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月7日 下午3:15:25</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class TestService implements ITestService{

	private String name;
	public String getName() {
		return this+name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Resource
	private IDemoService demoService;

	@Override
	public IDemoService getDemoService() {
		return demoService;
	}

	public void setDemoService(IDemoService demoService) {
		this.demoService = demoService;
	}
	
}
