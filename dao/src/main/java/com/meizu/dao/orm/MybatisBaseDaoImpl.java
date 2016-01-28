package com.meizu.dao.orm;

import java.io.Serializable;

import javax.annotation.Resource;

import com.meizu.entity.IdEntity;

//public abstract class MybatisBaseDaoImpl<T extends BaseEntity<T>, PK extends Serializable>
//extends SqlSessionDaoSupport implements IBaseDao<T, PK> {
public abstract class MybatisBaseDaoImpl<T extends IdEntity<Serializable,Integer>, PK extends Serializable> implements IBaseDao<T, PK> {
	@Resource
	private Dao<T, PK> dao;
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findById(java.io.Serializable)
	 */
//	@Override
//	public T findById(PK id) {
//		dao.setSqlSession(this.getSqlSession());
//		return dao.findById(id);
//	}

	/**
	 * 
	 * 方法用途: 各种实现MybatisBaseDaoImpl的类去做初始化<br>
	 * 操作步骤: TODO<br>
	 */
	public abstract void init();
	
}
