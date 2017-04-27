package vip.simplify.dao.orm;

/**
 * <p><b>Title:</b><i>枚举类型接口，实现该接口的类必须是枚举类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年10月12日 下午2:26:37</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年10月12日 下午2:26:37</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IEnum {
	/**
	 * 方法用途: 获取枚举类型实例要显示的文本<br>
	 * 操作步骤: TODO<br>
	 * @return 返回枚举类型实例的文本
	 */
	String getText();

	/**
	 * 方法用途: 获取枚举类型实例的值<br>
	 * 操作步骤: 返回枚举类型实例的值<br>
	 * @return
	 */
	String getValue();
}
