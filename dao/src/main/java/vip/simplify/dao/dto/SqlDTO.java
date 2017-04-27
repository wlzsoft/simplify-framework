package vip.simplify.dao.dto;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月18日 下午3:30:07</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月18日 下午3:30:07</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class SqlDTO {

	private String whereName;
	private Object[] whereValues;
	public String getWhereName() {
		return whereName;
	}
	public void setWhereName(String whereName) {
		this.whereName = whereName;
	}
	public Object[] getWhereValues() {
		return whereValues;
	}
	public void setWhereValues(Object[] whereValues) {
		this.whereValues = whereValues;
	}

}
