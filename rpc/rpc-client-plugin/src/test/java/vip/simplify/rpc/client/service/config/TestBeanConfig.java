package vip.simplify.rpc.client.service.config;

import vip.simplify.ioc.annotation.BeanConfig;
import vip.simplify.rpc.annotations.ClientBeanConfig;
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

    @BeanConfig(annoType = ClientBeanConfig.class)
    public TestService testServiceClass;

    @BeanConfig
    public TestSubService testSubServiceClass;

    @BeanConfig
    public TestOutterService testSubService() {
        return new TestOutterService();
    }

    @BeanConfig
    public TestOutterSubService testOutterSubService() {
        return new TestOutterSubService();
    }

}
