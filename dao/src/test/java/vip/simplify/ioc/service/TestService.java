package vip.simplify.ioc.service;

import vip.simplify.dao.entity.Test;
import vip.simplify.dao.orm.Dao;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Resource;

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
	
	@Resource(name="vip.simplify.dao.entity.TestBaseDao")//bugfix: 修复没有包名情况的冲突问题[注入testBaseDao改成全路径vip.simplify.dao.entity.TestBaseDao] lcy 2016/8/15
	private Dao<Test,Integer> dao;

	@Override
	public IDemoService getDemoService() {
		System.out.println(dao.findById(1).getFid()+":Resource2");
		System.out.println(dao.findById(1).getName()+":Resource1");
		return demoService;
	}

	public void setDemoService(IDemoService demoService) {
		this.demoService = demoService;
	}
	
}
