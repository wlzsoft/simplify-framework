package vip.simplify.cache.entity;

import java.io.Serializable;

/**
  * <p><b>Title:</b><i>商品实体</i></p>
 * <p>Desc: 测试用</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2017年6月22日 下午8:53:23</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2017年6月22日 下午8:53:23</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class Goods<T> implements Serializable{
	private static final long serialVersionUID = -3417769368460253266L;
	
	private String title;
	private T t;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public T getT() {
		return t;
	}
	public void setT(T t) {
		this.t = t;
	}
	
}
