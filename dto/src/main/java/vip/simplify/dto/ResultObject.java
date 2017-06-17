package vip.simplify.dto;

/**
 * 
 * <p><b>Title:</b><i>传递结果信息</i></p>
 * <p>Desc: 包含复杂对象结果信息</p>
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
public class ResultObject<T> extends Result {
	
	/**
	 * 结果对象
	 */
	private T value;
	
	public ResultObject(T value) {
		this.value = value;
	}
	
	public ResultObject(int code,T value) {
		this.setCode(code);
		this.value = value;
	}
	
	public ResultObject() {
		
	}

	public T getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "ResultObject "+super.toString()+" [value=" + value+ "]";
	}
}
