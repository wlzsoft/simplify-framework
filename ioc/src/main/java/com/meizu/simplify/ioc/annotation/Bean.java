package com.meizu.simplify.ioc.annotation;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.meizu.simplify.ioc.enums.BeanTypeEnum;

/**
 * <p><b>Title:</b><i>实例创建标示</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月5日 下午6:35:20</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月5日 下午6:35:20</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Bean {

	BeanTypeEnum type() default BeanTypeEnum.SINGLE;

}
