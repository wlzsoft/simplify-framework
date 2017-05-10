package vip.simplify.rpc.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.rpc.client.service.ITestRemoteService;
import vip.simplify.rpc.client.service.TestService;
import vip.simplify.rpc.client.service.outter.TestOutterService;
import vip.simplify.test.SimplifyJUnit4ClassRunner;

/**
 * <p>基于BeanConfig的clientBean注解解析测试类</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月15日 下午4:45:25</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年4月15日 下午4:45:25</p>
 * @author <a href="wanghaibin@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 3.0
 *
 */
@Bean
@RunWith(SimplifyJUnit4ClassRunner.class)
public class ClientBeanAnnotationResolverTest {
    @Inject
    private TestService testService;

    @Inject
    private TestOutterService testOutterService;

    @Inject
    private ITestRemoteService testRemoteService;

    @Test
    public void test() {
        testService.getName();
        testOutterService.test();
        System.out.println(testRemoteService);
    }
}
