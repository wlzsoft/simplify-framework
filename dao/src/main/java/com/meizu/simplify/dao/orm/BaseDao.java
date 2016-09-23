package com.meizu.simplify.dao.orm;

import java.io.Serializable;

import com.meizu.simplify.entity.IdEntity;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.BeanFactory;

/**
 * 
 * <p><b>Title:</b><i>sql数据库Dao操作基类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月10日 下午1:42:18</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月10日 下午1:42:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 */
public class BaseDao  {

	/**
	 * 方法用途: 获取基于map结果集的对应的dao实例<br>
	 * 操作步骤： TODO <br>
	 * @return
	 */
	public static  SearchByMapDao getInsMap() {
		SearchByMapDao dao = BeanFactory.getBean(SearchByMapDao.class);
		if(dao == null) {
			throw new UncheckedException("无法获取到SearchByMapDao.class的dao，请检查是否已经初始化了");
		}
		return dao;
	}
	
	/**
	 * 
	 * 方法用途: 获取基于pojo对象结果集的对应的dao实例<br>
	 * 操作步骤: TODO不建议使用<br>
	 * @param className 结果实体类的名称
	 * @return
	 */
	public static SearchByPojoDao getInsPojo () {
		SearchByPojoDao dao = BeanFactory.getBean(SearchByPojoDao.class);
		if(dao == null) {
			throw new UncheckedException("无法获取到"+"SearchByPojoDao"+"实体对应的dao[PojoDao]");
		}
		return dao;
	}
	
	/**
	 * 方法用途: 获取业务实体类对应的dao实例<br>
	 * 操作步骤: 1.第一步：对于的实体类必须实现基于IdEntity的实体或是IdEntity本身
	 *        2.第二步：必须在对于的实体类上标注javax.persistence.Entity注解,才能动态生成对应的dao操作类
	 *        3.第三步：必须在对于的实体类上标注javax.persistence.Table注解,并指定表名，才能知道具体操作哪个表，
	 *        。。。。具体的信息，参照com.meizu.entity.Test类<br>
	 * @param entityClass 业务实体类
	 * @return
	 */
	public static <T extends IdEntity<Serializable,Integer>>  Dao<T, Serializable> getIns (Class<T> entityClass) {
		String className = entityClass.getName();//bugfix: 修复没有包名情况的冲突问题[getSimpleName改成getName] lcy 2016/8/15
		return getIns(className);
	}
	
	/**
	 * 
	 * 方法用途: 参考<code> getIns(Class<T> classz)</code><br>
	 * 操作步骤: TODO<br>
	 * @param className 业务实体类的名称
	 * @return
	 */
	public static <T extends IdEntity<Serializable,Integer>>  Dao<T, Serializable> getIns (String className) {
		/*char[] chars = className.toCharArray();
		chars[0] = Character.toLowerCase(chars[0]);
		String daoClassName = new String(chars) + "BaseDao";*///bugfix: 修复没有包名情况的冲突问题[无需设置第一个字母为小写] lcy 2016/8/15 
		String daoClassName = className+"BaseDao";
		Dao<T, Serializable> dao = BeanFactory.getBean(daoClassName);
		if(dao == null) {
			throw new UncheckedException("无法获取到"+className+"实体对应的dao["+daoClassName+"]，请检查下实体是否有标注注解：@Entity和@Table(name='demo')");
		}
		dao.setIndex(null);
		return dao;
	}

	/**
	 * 方法用途: 直接通过表名对数据库进行操作，无需实体<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static ExecByTableNameDao getTable() {
		ExecByTableNameDao dao = BeanFactory.getBean(ExecByTableNameDao.class);
		if(dao == null) {
			throw new UncheckedException("无法获取到"+"ExecByTableNameDao"+"实体对应的dao[tableNameDao]");
		}
		return dao;
	}

}