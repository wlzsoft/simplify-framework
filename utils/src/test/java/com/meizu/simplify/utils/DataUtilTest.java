package com.meizu.simplify.utils;

import org.junit.Test;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月27日 上午10:00:48</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月27日 上午10:00:48</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class DataUtilTest {
	@Test
	public void convertType() {
		Integer o = DataUtil.convertType(Integer.class, "2");
		System.out.println(o);
	}
}
