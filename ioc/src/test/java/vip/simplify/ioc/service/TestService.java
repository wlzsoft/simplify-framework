package vip.simplify.ioc.service;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Resource;
import vip.simplify.ioc.dao.Dao;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月7日 下午3:15:25</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月7日 下午3:15:25</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
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
	
	@Resource(name="test1BaseDao")
	private Dao dao;

	@Override
	public IDemoService getDemoService() {
		System.out.println(dao.getSql()+":Resource(\"test1Dao\")");
		return demoService;
	}

	public void setDemoService(IDemoService demoService) {
		this.demoService = demoService;
	}
	
}
