package com.meizu.dao.mybatis.base;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月18日 上午10:21:10</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月18日 上午10:21:10</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class SaveDTO extends BaseDTO {
	private static final long serialVersionUID = 5103587275472705466L;
	
	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
