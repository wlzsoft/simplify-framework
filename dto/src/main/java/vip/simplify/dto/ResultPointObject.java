package vip.simplify.dto;

/**
 * 
 * <p><b>Title:</b><i>传输JSON对象</i></p>
 * <p>Desc: 传输JSON对象</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2014年12月22日 下午2:56:46</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2014年12月22日 下午2:56:46</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class ResultPointObject<T> extends ResultObject<T> {
	
	public ResultPointObject(T value) {
		super(value);
	}

	/**
	 * 导向跳转地址
	 */
	public String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
