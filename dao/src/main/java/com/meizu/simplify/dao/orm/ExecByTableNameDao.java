package com.meizu.simplify.dao.orm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.ioc.annotation.Bean;
/**
 * <p><b>Title:</b><i>基于表名的基础dao实现</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月24日 上午11:35:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月24日 上午11:35:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class ExecByTableNameDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecByTableNameDao.class);
	

	/**
	 * 方法用途: 忽略的字段<br>
	 * 操作步骤: TODO<br>
	 * @param columnNames
	 * @return
	 */
	public ExecByTableNameDao transiented(String... columnNames) {
		LOGGER.info("忽略的字段");
		return this;
	}


	/**
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param columns
	 * @return
	 */
	public Integer save(String... columns) {
		// TODO Auto-generated method stub
		return null;
	}
}
