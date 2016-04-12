package com.meizu.simplify.ioc.annotation;
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
/**
 * <p><b>Title:</b><i>异步标示</i></p>
 * <p>Desc: 标注方法上，使方法使用异步方式执行
 *    TODO 后续规划，确认是否属于ioc模块 </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 上午10:53:08</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 上午10:53:08</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 * @since ioc 0.1
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface Asyn {
}

