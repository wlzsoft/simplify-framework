package com.meizu.simplify.dao.orm;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.utils.ReflectionUtil;

/**
 * <p><b>Title:</b><i>普通对象操作dao，主要用于查询普通对象结果集</i></p>
 * <p>Desc: TODO暂未实现</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月31日 下午5:45:25</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月31日 下午5:45:25</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 * @param <T>
 */
@Bean
public class SearchByPojoDao {
	private static final Logger logger = LoggerFactory.getLogger(SearchByMapDao.class);
	/**
	 * 
	 * 方法用途: 可参考<code>Dao.find(String sql,Object... params)</code>，是它的克隆版，针对非数据库映射表实体<br>
	 * 操作步骤: TODO<br>
	 * @param entityClass
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T> List<T> find(Class<T> entityClass,String sql,Object... params) {
		logger.info(sql);
		List<T> tList = SQLExecute.executeQuery(sql, new IDataCallback<T>() {
			@Override
			public T paramCall(PreparedStatement prepareStatement,Object... obj) throws SQLException {
				return IDataCallback.super.paramCall(prepareStatement,params);
			}
			@Override
			public T resultCall(String columnLabel, Object val,T t) {
				try {
					if(val != null) {
						Class<?> valClazz = val.getClass();
						ReflectionUtil.invokeSetterMethod(t, columnLabel, val,valClazz);
					}
				} catch(IllegalArgumentException ex) {
					throw new IllegalArgumentException("请检查是否数据库类型和实体类型不匹配，或是字段名和属性名不匹配==>>"+ex.getMessage());
				}
				return t;
			}
		},entityClass);
		return tList;
	}
}
