package vip.simplify.rpc.client.service.config;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.BeanConfig;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.ioc.annotation.Injects;
import vip.simplify.rpc.annotations.ClientBean;
import vip.simplify.rpc.client.service.ITestRemoteService;
import vip.simplify.rpc.client.service.TestService;
import vip.simplify.rpc.client.service.TestSubService;
import vip.simplify.rpc.client.service.outter.TestOutterService;
import vip.simplify.rpc.client.service.outter.TestOutterSubService;

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
    @Injects(@Inject(type = TestSubService.class))
    public TestService testFirstService;

    @ClientBean(version = "1.0.0",check = false)
    @Bean
    private ITestRemoteService testRemoteService;

    @Bean
    public TestSubService testSubServiceClass;

    @Bean
    public TestOutterService testSubService() {
        TestOutterService testOutterService = new TestOutterService();
        testOutterService.setTestOutterSubService(testOutterSubService());
        return testOutterService;
    }

    @Bean
    public TestOutterSubService testOutterSubService() {
        return new TestOutterSubService();
    }

}
