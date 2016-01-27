package com.meizu.data.mybatis;

import java.io.Serializable;

import javax.annotation.Resource;


import com.meizu.entity.baseEntity.IdEntity;
/**
 * 
 * <p><b>Title:</b><i> 基于Mybatis的基础泛型DAO实现类  TODO　未实现 </i></p>
 * <p>Desc: SqlSessionDaoSupport的实现方式
 *          注意：目前只实现了findById方法，复用了Dao类的实现，通过这个类获取sqlSession注入到Dao类中</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午5:30:07</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午5:30:07</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 * @param <T> 业务实体类型 
 * @param <PK>  PK类型 ，如：String、Long、Integer 等
 */
//public abstract class MybatisBaseDaoImpl<T extends BaseEntity<T>, PK extends Serializable>
//extends SqlSessionDaoSupport implements IBaseDao<T, PK> {
public abstract class MybatisBaseDaoImpl<T extends IdEntity<Serializable,Integer>, PK extends Serializable>
/*extends SqlSessionDaoSupport*/ implements IBaseDao<T, PK> {
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
