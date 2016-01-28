package com.meizu.dao.datasource;

import java.util.Map;


/**
 * <p><b>Title:</b><i>多数据源处理</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年4月28日 下午2:00:52</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年4月28日 下午2:00:52</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource{
	
	@Override
	protected Object determineCurrentLookupKey() {
		 String dataSourceName = DynamicDataSourceHolder.getDataSourceName();
	        return dataSourceName;
	}
	    
    @Override
    public void setTargetDataSources(Map<Object,Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
    }

	
}
