package vip.simplify.mvc;

import java.lang.annotation.Annotation;

import vip.simplify.dto.AnnotationInfo;

/**
  * <p><b>Title:</b><i>注解处理回调接口</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月25日 下午5:53:23</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年4月25日 下午5:53:23</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface AnnotationResolverCallback<T extends Annotation>  {
	/**
	 * 
	 * 方法用途: 解析回调过程<br>
	 * 操作步骤: TODO<br>
	 * @param annoInfo
	 */
	public void resolver(AnnotationInfo<T> annoInfo);
}
