package com.meizu.demo.mvc.service;

import java.io.Serializable;

import com.meizu.demo.mvc.entity.Test;
import com.meizu.demo.system.IAutoBusinessService;
import com.meizu.simplify.dao.orm.BaseDao;
import com.meizu.simplify.entity.IdEntity;
import com.meizu.simplify.ioc.annotation.Bean;

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
	public IdEntity<Serializable, Integer> get(Serializable id) {
		 return BaseDao.getIns(Test.class).findById(id);
	}

	@Override
	public <T> boolean save(T t, Class<T> clazz) {
		Test test = new Test();
		test.setName("lcy-auto");
		boolean isSave = BaseDao.getIns(Test.class).save(test);
		return isSave;
	}

}
