package vip.simplify.utils;

import org.junit.Assert;
import org.junit.Test;
/**
 * <p><b>Title:</b><i>UUID工具测试类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月1日 下午6:42:45</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月1日 下午6:42:45</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class UUIDUtilTest {
	
	@Test
	public void test() {
		Assert.assertEquals(UUIDUtil.getRandomUUID().length(), 32);
	}
}
