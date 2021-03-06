package vip.simplify.demo.mvc.entity;

import vip.simplify.entity.BaseEntity;
import vip.simplify.entity.annotations.Entity;
import vip.simplify.entity.annotations.Table;
import vip.simplify.entity.annotations.Transient;

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
@Transient({"deleteflag"})
public class Test extends BaseEntity {

	private static final long serialVersionUID = 246628316050179125L;

	private String name;
	@Transient
	private String createName;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
}
