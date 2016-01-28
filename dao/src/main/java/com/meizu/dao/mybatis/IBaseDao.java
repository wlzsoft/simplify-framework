package com.meizu.dao.mybatis;
import java.io.Serializable;
import java.util.List;

import com.meizu.dao.Criteria;
import com.meizu.dao.util.Page;
import com.meizu.entity.IdEntity;
 
/**
 * 
 * <p><b>Title:</b><i>基于Mybatis的基础DAO接口 </i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午5:29:23</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午5:29:23</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 * @param <T> 业务实体类型
 * @param <PK> PK类型 ，如：String、Long、Integer 等
 */
public interface IBaseDao<T extends IdEntity<Serializable,Integer>, PK extends Serializable> {
	
     
}