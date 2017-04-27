package vip.simplify.dao;

import vip.simplify.dao.datasource.DataSourceManager;
import vip.simplify.ioc.IStopRelease;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Resource;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月18日 下午6:24:07</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月18日 下午6:24:07</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class DaoStopRelease implements IStopRelease{

	@Resource
	private DataSourceManager dataSourceManager;
	
	@Override
	public void release() {
		if(dataSourceManager == null) {
			System.out.println("IStopRelease:无法释放数据源，因为针对这个类的依赖注入过程还未开始，可能数据源还未被初始化或是数据源已被初始化但未来得及注入，问题出现在InitTypeEnum.IOC的这个启动初始化这个阶段或之前的任何一个阶段导致的问题");
			return;
		}
		dataSourceManager.closeDataSource();
	}

}
