
package vip.simplify.test;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.exception.UncheckedException;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.Startup;

/**
 * <p><b>Title:</b><i>Simplify单元测试运行器</i></p>
 * <p>Desc: junit4.5以上</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年10月11日 下午2:52:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年10月11日 下午2:52:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 */
public class SimplifyJUnit4ClassRunner extends BlockJUnit4ClassRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimplifyJUnit4ClassRunner.class);

	/**
	 * @param clazz 当前需要执行的单元测试类的Class对象
	 */
	public SimplifyJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("基于Simplify框架的单元测试类[" + clazz + "]开始执行，会使用框架上下文环境");
		}
	}

	@Override
	protected Statement withBeforeClasses(Statement statement) {
		Statement junitBeforeClasses = super.withBeforeClasses(statement);
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				junitBeforeClasses.evaluate();
			}
		};
	}

	@Override
	protected Statement withAfterClasses(Statement statement) {
		Statement junitAfterClasses = super.withAfterClasses(statement);
		return junitAfterClasses;
	}

	@Override
	protected Object createTest() throws Exception {
		//启动框架开始执行单元测试
		Startup.start();
		Class<?> testClass= super.getTestClass().getJavaClass();
		Object testInstance = BeanFactory.getBean(testClass);
		if (testInstance == null) {
			String errorMessage = "基于Simplify框架的单元测试类[" + testClass + "]的Bean未被创建，请检查是否system.classpaths配置有问题，或是测试类的包名有误";
			LOGGER.info(errorMessage);
			throw new UncheckedException(errorMessage);
		}
		return testInstance;
	}

	@Override
	protected Statement withBefores(FrameworkMethod frameworkMethod, Object testInstance, Statement statement) {
		Statement junitBefores = super.withBefores(frameworkMethod, testInstance, statement);
		return junitBefores;
	}

	@Override
	protected Statement withAfters(FrameworkMethod frameworkMethod, Object testInstance, Statement statement) {
		Statement junitAfters = super.withAfters(frameworkMethod, testInstance, statement);
		return junitAfters;
	}

}
