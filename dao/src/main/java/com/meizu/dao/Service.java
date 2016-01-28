package com.meizu.dao;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.entity.IdEntity;

/**
 * 
 * <p><b>Title:</b><i> 基于Mybatis的基础泛型DAO实现类</i></p>
 * <p>Desc:1. SqlSessionTemplate的实现方式
 *         2. 注意sql注入的问题</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月10日 下午1:42:18</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月10日 下午1:42:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 * @param <T> 业务实体类型 
 * @param <PK>  PK类型 ，如：String、Long、Integer 等
 */
public class Service<T extends IdEntity<Serializable,Integer>, PK extends Serializable>  {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public Integer save(T t) {
		return null;
	}
	
}
