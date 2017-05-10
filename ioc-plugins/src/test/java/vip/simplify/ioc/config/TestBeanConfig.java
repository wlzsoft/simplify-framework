package vip.simplify.ioc.config;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.BeanConfig;
import vip.simplify.ioc.config.service.TestService;
import vip.simplify.ioc.config.service.TestSubService;
import vip.simplify.ioc.config.service.outter.TestOutterService;
import vip.simplify.ioc.config.service.outter.TestOutterSubService;

/**
 * <p><b>Title:</b><i>基于BeanConfig来标识Bean类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月5日 下午6:35:20</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月5日 下午6:35:20</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@BeanConfig
public class TestBeanConfig {

    @Bean
    public TestService testServiceClass;

    @Bean
    public TestSubService testSubServiceClass;

    @Bean
    public TestOutterService testSubService() {
        return new TestOutterService();
    }

    @Bean
    public TestOutterSubService testOutterSubService() {
        return new TestOutterSubService();
    }

}
