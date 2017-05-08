package vip.simplify.ioc.resolver;

import org.junit.Test;
import vip.simplify.dto.BeanMetaDTO;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.utils.clazz.ClassInfo;

import java.util.Map;

/**
 * <p><b>Title:</b><i>BeanConfigAnnotationResolver测试类</i></p>
 * <p>Desc: TODO </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 上午11:08:36</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 上午11:08:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class BeanConfigAnnotationResolverTest {
    @Test
    public void resolve() {
        new BeanConfigAnnotationResolver().resolve(null);
        Map<Class<?>,ClassInfo<BeanMetaDTO>> beanClassMap = ClassMetaResolver.getBeanClassMap();
        beanClassMap.forEach((k,v) -> {
            System.out.println("name="+v.getClazz().getName());
        });
        Map<String,Object> map = BeanFactory.getBeanContainer().getMapContainer();
        map.forEach((k,v) -> {
            System.out.println(k+","+v);
        });
    }
}
