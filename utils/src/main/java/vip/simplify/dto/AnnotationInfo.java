package vip.simplify.dto;

import java.lang.annotation.Annotation;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月19日 上午11:48:46</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月19日 上午11:48:46</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class AnnotationInfo<T extends Annotation> {

	/**
	 * annotation类型
	 */
	private T annotatoionType;
	
	private Class<?> returnType;

	public Class<?> getReturnType() {
		return returnType;
	}

	public T getAnnotatoionType() {
		return annotatoionType;
	}

	public void setAnnotatoionType(T annotatoionType) {
		this.annotatoionType = annotatoionType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
}
