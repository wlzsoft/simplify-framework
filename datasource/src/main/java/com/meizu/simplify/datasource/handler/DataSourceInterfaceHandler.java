package com.meizu.simplify.datasource.handler;

import com.meizu.simplify.dao.datasource.IDataSource;
import com.meizu.simplify.datasource.MutilDataSource;
import com.meizu.simplify.ioc.IInterfaceHandler;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.HandleInterface;

/**
  * <p><b>Title:</b><i>单数据源和多数据源实现类选择器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月14日 上午10:58:04</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月14日 上午10:58:04</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@HandleInterface(IDataSource.class)
public class DataSourceInterfaceHandler implements IInterfaceHandler{

	@Override
	public Class<?> handle() {
		return MutilDataSource.class;
	}
	
}
