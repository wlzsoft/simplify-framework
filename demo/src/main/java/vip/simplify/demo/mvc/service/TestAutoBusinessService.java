package vip.simplify.demo.mvc.service;

import java.io.Serializable;
import java.util.List;

import vip.simplify.demo.mvc.entity.Test;
import vip.simplify.demo.system.IAutoBusinessService;
import vip.simplify.dao.orm.BaseDao;
import vip.simplify.ioc.annotation.Bean;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年6月3日 上午11:45:32</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年6月3日 上午11:45:32</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean(value="testAutoBusinessService")
public class TestAutoBusinessService implements IAutoBusinessService{

	@Override
	public List<?> get(Serializable[] id) {
		List<?> testList = BaseDao.getIns(Test.class).findByIds(id);
		 return testList;
	}
	
	@Override
	public int del(Serializable[] id) {
		 return BaseDao.getIns(Test.class).remove(id);
	}
	
	@Override
	public <T> int update(T t, Class<T> clazz) {
		Test test = new Test();
		test.setFid(1);
		test.setName("lcy-auto");
		return BaseDao.getIns(Test.class).update(test);
	}
	
	@Override
	public <T> boolean save(T t, Class<T> clazz) {
		Test test = new Test();
		test.setName("lcy-auto");
		boolean isSave = BaseDao.getIns(Test.class).save(test);
		return isSave;
	}

	

}
