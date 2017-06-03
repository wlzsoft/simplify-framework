package vip.simplify.template.freemarker;

import org.junit.Assert;
import org.junit.Test;

import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.Startup;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2017年6月3日 下午2:07:34</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2017年6月3日 下午2:07:34</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class FreemarkerConfigTest {
	@Test
	public void test() {
		Startup.start();
		FreemarkerConfig freemarkerConfig = BeanFactory.getBean(FreemarkerConfig.class);
		Assert.assertEquals("computer", freemarkerConfig.getNumberFormat());
		Assert.assertEquals("true,false", freemarkerConfig.getBooleanFormat());
		Assert.assertEquals("yyyy-MM-dd HH:mm:ss", freemarkerConfig.getDateTimeFormat());
		Assert.assertEquals("yyyy-MM-dd", freemarkerConfig.getDateFormat());
		Assert.assertEquals("HH:mm:ss", freemarkerConfig.getTimeFormat());
		Assert.assertEquals("UTF-8", freemarkerConfig.getDefaultEncoding());
		Assert.assertEquals(Integer.valueOf(5), freemarkerConfig.getTemplateUpdateDelay());
		Assert.assertEquals("UTF-8", freemarkerConfig.getUrlEscapingCharset());
	}
}
