package com.meizu.simplify.dao.entity;

import com.meizu.simplify.entity.BaseEntity;
import com.meizu.simplify.entity.annotations.Entity;
import com.meizu.simplify.entity.annotations.Table;
import com.meizu.simplify.entity.annotations.Transient;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月28日 下午6:02:55</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月28日 下午6:02:55</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Entity
@Table(name="test_web")
@Transient({"delFlag","deleteflag"})
public class Test extends BaseEntity {

	private static final long serialVersionUID = 246628316050179125L;
	private String name;
	private String url;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
