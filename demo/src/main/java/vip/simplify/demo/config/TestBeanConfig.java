package vip.simplify.demo.config;

import vip.simplify.demo.mvc.service.TestFirstService;
import vip.simplify.demo.mvc.service.TestSubService;
import vip.simplify.demo.mvc.service.outter.TestOutterService;
import vip.simplify.demo.mvc.service.outter.TestOutterSubService;
import vip.simplify.ioc.annotation.BeanConfig;

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

    @BeanConfig(attributes = TestSubService.class)
    public TestFirstService testFirstService;

    @BeanConfig
    public TestSubService testSubService;

    //以下方式暂不建议使用
    @BeanConfig
    public TestOutterService testSubService() {
        TestOutterService testOutterService = new TestOutterService();
        //这种方式设置会导致创建多实例，并且这个实例非Bean容器托管的，后续需要特殊处理
        testOutterService.setTestOutterSubService(testOutterSubService());
        return testOutterService;
    }

    @BeanConfig
    public TestOutterSubService testOutterSubService() {
        return new TestOutterSubService();
    }

}
