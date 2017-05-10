package vip.simplify.ioc.hook;

import vip.simplify.dto.BeanMetaDTO;
import vip.simplify.ioc.BeanEntity;
import vip.simplify.utils.clazz.ClassInfo;

import java.lang.annotation.Annotation;

/**
  * <p><b>Title:</b><i>bean单例钩子处理</i></p>
 * <p>Desc: 用于bean的单实例创建定制操作-必须是标识某一注解</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月11日 上午10:51:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月11日 上午10:51:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 * @param <T> 指定注解类型
 */
public interface IBeanHook<T extends Annotation> {
	/**
	 * 
	 * 方法用途: bean初始化钩子方法<br>
	 * 操作步骤: TODO<br>
	 * @param clazz
	 * @param annotation
	 * @return
	 */
	BeanEntity<?> hook(Class<?> clazz,T annotation);
}
