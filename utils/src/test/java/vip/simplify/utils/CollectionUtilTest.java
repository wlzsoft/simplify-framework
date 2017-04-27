package vip.simplify.utils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月25日 下午3:32:17</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月25日 下午3:32:17</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class CollectionUtilTest {
	@Test
	public void duplicate() {
		String[] strArr={"doSomeThing","addTest","doSomeThing2","addTest","addTestObj"};
		List<String> strList = CollectionUtil.duplicate(strArr);
		String result = "";
		for (String string : strList) {
			result+=string+",";
		}
		System.out.println(result);
		String[] strArr2={"doSomeThing","addTest","doSomeThing2","addTestObj"};
		Assert.assertArrayEquals(strArr2, strList.toArray());
	}
}
