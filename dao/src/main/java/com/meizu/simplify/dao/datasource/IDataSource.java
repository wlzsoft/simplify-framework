package com.meizu.simplify.dao.datasource;

import javax.sql.DataSource;

import com.meizu.simplify.ioc.annotation.DefaultBean;

/**
 * <p><b>Title:</b><i>数据源接口</i></p>
 * <p>Desc: TODO </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月29日 下午3:00:39</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月29日 下午3:00:39</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@DefaultBean
public interface IDataSource {
	
	/**
	 * 
	 * 方法用途: <br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public DataSource value();
	
	/**
	 * 
	 * 方法用途: 打印当前数据源配置等相关信息<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public String print();
	
	/**
	 * 
	 * 方法用途: 初始化数据源<br>
	 * 操作步骤:  init-method="init"<br>
	 */
	public void init();
	
	/**
	 * 
	 * 方法用途: 关闭数据源<br>
	 * 操作步骤:  destroy-method="close"<br>
	 */
	public void close();

	/**
	 * 
	 * 方法用途: 获取数据源名称<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public String getName();
}

