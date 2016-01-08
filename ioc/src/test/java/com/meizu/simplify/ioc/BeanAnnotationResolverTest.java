package com.meizu.simplify.ioc;

import org.junit.Test;

import com.meizu.simplify.ioc.resolver.BeanAnnotationResolver;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月7日 下午3:12:28</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月7日 下午3:12:28</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class BeanAnnotationResolverTest {
	@Test
	public void resolve() {
		new BeanAnnotationResolver().resolve(null);
	}
}
