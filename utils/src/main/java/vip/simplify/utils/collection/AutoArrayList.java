package vip.simplify.utils.collection;

import java.util.ArrayList;

/**
 * 
 * <p><b>Title:</b><i>自增长ArrayList</i></p>
 * <p>Desc: 注意：使用了反射，使用要考虑其对性能的影响</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2014年12月22日 下午2:41:24</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2014年12月22日 下午2:41:24</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class AutoArrayList<T extends Object> extends ArrayList<T> {

	private static final long serialVersionUID = 5332296994651370661L;
	private Class<T> classItem;

	public AutoArrayList(Class<T> classItem) {
		this.classItem = classItem;
	}

	public T get(int index) {
		try {
			while (index >= size()) {
				add(classItem.newInstance());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.get(index);
	}

}
