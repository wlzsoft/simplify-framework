package com.meizu.simplify.utils;

import java.util.Date;

import org.junit.Test;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月23日 下午4:09:47</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月23日 下午4:09:47</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class DateUtilTest {

	@Test
	public void format() {
		System.out.println(DateUtil.formatAll(new Date()));
	}
}
