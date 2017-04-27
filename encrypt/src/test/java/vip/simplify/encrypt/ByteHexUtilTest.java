package vip.simplify.encrypt;

import org.junit.Test;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 下午4:32:19</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月9日 下午4:32:19</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class ByteHexUtilTest {
	@Test
	public void test() {
		System.out.println(ByteHexUtil.hex2Bytes("7d314ad9eb227ca5dfea54a9f3205177").length);
	}
}
