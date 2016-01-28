package com.meizu.dao.entity;

import com.meizu.dao.annotations.Entity;
import com.meizu.dao.annotations.Table;
import com.meizu.dao.annotations.Transient;
import com.meizu.entity.BaseEntity;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月28日 下午6:02:55</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月28日 下午6:02:55</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Entity
@Table(name="sys_role")
@Transient({"delFlag"})
public class Test extends BaseEntity {

}
