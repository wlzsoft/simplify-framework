package vip.simplify.mvc.model;

import java.lang.annotation.*;

/**
 * <p><b>Title:</b><i>对表单数据校验标记，也可以针对entity</i></p>
 * <p>Desc: 标注于set方法上和属性上，后续这个注解的解析可以单独独立成一个模块，非web项目也可以用</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月22日 下午4:58:08</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年7月22日 下午4:58:08</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Valid {
    /**
     * 方法用途: 设置属性的校验类型，可以有一个到多个校验类型<br>
     * 操作步骤: 这个属性必须<br>
     * @return
     */
    ValidTypeEnum[] value();

    /**
     * 方法用途: 设置校验没通过的默认提示信息<br>
     * 操作步骤: 这个属性非必须<br>
     * @return
     */
    String[] defaultMessages() default "";
}
