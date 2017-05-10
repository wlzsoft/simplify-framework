package vip.simplify.ioc.annotation;
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
/**
 * <p><b>Title:</b><i>依赖注入标示</i></p>
 * <p>Desc: 只要类上标注了@Bean和@StaticType注解，那么其下标注了Resource注解的属性，都会被注入，不同的是标注了@Bean的可以成员属性注入，也可以静态属性注入，而标注@StaticType的只可以注入静态属性</p>
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
@Target({FIELD})
@Retention(RUNTIME)
@Documented
public @interface Inject {
    /**
     * 
     * 方法用途: 用于指定具体实现类的实例名<br>
     * 操作步骤: TODO<br>
     * @return
     */
    String name() default "";

    /**
     * 
     * 方法用途: 用于指定具体实现类的类型<br>
     * 操作步骤: TODO<br>
     * @return
     */
    Class<?> type() default java.lang.Object.class;

    /**
     * 
     * 方法用途: 描述信息，用于描述具体bean的功能<br>
     * 操作步骤: TODO<br>
     * @return
     */
    String desc() default "";
}

