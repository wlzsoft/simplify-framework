package vip.simplify.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.TypeReference;

import vip.simplify.demo.mvc.entity.User;
import vip.simplify.entity.page.Page;
import vip.simplify.utils.JsonUtil;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2017年6月23日 下午5:04:23</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2017年6月23日 下午5:04:23</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class PageTest {
	@Test
	public void testPage() {
		List<User> list = new ArrayList<>();
		User user = new User();
		user.setName("卢创业");
		list.add(user);
		Page<User> page = new Page<>(2,20,600);
		page.setResults(list);
		Page<User> goodsResult = JsonUtil.jsonToObject(JsonUtil.objectToString(page),new TypeReference<Page<User>>(){});
		Assert.assertEquals(2, goodsResult.getCurrentPage());
		Assert.assertEquals(20, goodsResult.getPageSize());
		Assert.assertEquals(600, goodsResult.getTotalRecord());
		Assert.assertEquals("卢创业", goodsResult.getResults().get(0).getName());
	}
}
