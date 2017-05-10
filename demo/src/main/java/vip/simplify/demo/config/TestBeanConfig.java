package vip.simplify.demo.config;

import vip.simplify.config.api.service.IConfigService;
import vip.simplify.demo.mvc.service.ITestRemoteService;
import vip.simplify.demo.mvc.service.TestFirstService;
import vip.simplify.demo.mvc.service.TestSubService;
import vip.simplify.demo.mvc.service.outter.TestOutterService;
import vip.simplify.demo.mvc.service.outter.TestOutterSubService;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.BeanConfig;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.ioc.annotation.Injects;
import vip.simplify.rpc.annotations.ClientBean;

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
    public TestFirstService testFirstService;

    @ClientBean(version = "1.0.0",check = false)
    @Bean
    private ITestRemoteService testRemoteService;

    @Bean
    public TestSubService testSubService;

    //以下方式暂不建议使用
    @Bean
    public TestOutterService testSubService() {
        TestOutterService testOutterService = new TestOutterService();
        //这种方式设置会导致创建多实例，并且这个实例非Bean容器托管的，后续需要特殊处理
        testOutterService.setTestOutterSubService(testOutterSubService());
        return testOutterService;
    }

    @Bean
    public TestOutterSubService testOutterSubService() {
        return new TestOutterSubService();
    }

}
