package vip.simplify.entity.page;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2017年2月22日 下午5:02:08</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2017年2月22日 下午5:02:08</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class PageTest {

	@Test
	public void test() {
		Page<Map<String,String>> page = new Page<>(1,20);//等同于Page<Map<String,String>> page = new Page<>(1,20,0);
		Assert.assertEquals(1, page.getCurrentPage());
		System.out.println("|lastPageNo="
				+page.getLastPageNo()+"|getPrevPageNo="+page.getPrevPageNo()+"|getNextPageNo="+page.getNextPageNo()+"|isFirstPage="+page.isFirstPage()
				+"|isLastPage="+page.isLastPage()+"|isHasNextPage="+page.isHasNextPage()+"|isHasPrevPage="+page.isHasPrevPage());
		Assert.assertEquals(true, page.isFirstPage());
		Assert.assertEquals(true, page.isLastPage());
		Assert.assertEquals(false, page.isHasPrevPage());
		Assert.assertEquals(false, page.isHasNextPage());
	}
	@Test
	public void test7() {
		Page<Map<String,String>> page = new Page<>(1,20);//等同于Page<Map<String,String>> page = new Page<>(1,20,0);
		page.init(5, false);
		Assert.assertEquals(1, page.getCurrentPage());
		System.out.println("|lastPageNo="
				+page.getLastPageNo()+"|getPrevPageNo="+page.getPrevPageNo()+"|getNextPageNo="+page.getNextPageNo()+"|isFirstPage="+page.isFirstPage()
				+"|isLastPage="+page.isLastPage()+"|isHasNextPage="+page.isHasNextPage()+"|isHasPrevPage="+page.isHasPrevPage());
		Assert.assertEquals(true, page.isFirstPage());
		Assert.assertEquals(true, page.isLastPage());
		Assert.assertEquals(false, page.isHasPrevPage());
		Assert.assertEquals(false, page.isHasNextPage());
	}
	@Test
	public void test2() {
		Page<Map<String,String>> page = new Page<>(1,20,5);
		Assert.assertEquals(1, page.getCurrentPage());
		System.out.println("|lastPageNo="
		+page.getLastPageNo()+"|getPrevPageNo="+page.getPrevPageNo()+"|getNextPageNo="+page.getNextPageNo()+"|isFirstPage="+page.isFirstPage()
		+"|isLastPage="+page.isLastPage()+"|isHasNextPage="+page.isHasNextPage()+"|isHasPrevPage="+page.isHasPrevPage());
		Assert.assertEquals(1, page.getNextPageNo());
		Assert.assertEquals(1, page.getPrevPageNo());
		Assert.assertEquals(true, page.isFirstPage());
		Assert.assertEquals(true, page.isLastPage());
		Assert.assertEquals(false, page.isHasPrevPage());
		Assert.assertEquals(false, page.isHasNextPage());
	}
	@Test
	public void test3() {
		Page<Map<String,String>> page = new Page<>(1,20,20);
		Assert.assertEquals(1, page.getCurrentPage());
		System.out.println("|lastPageNo="
				+page.getLastPageNo()+"|getPrevPageNo="+page.getPrevPageNo()+"|getNextPageNo="+page.getNextPageNo()+"|isFirstPage="+page.isFirstPage()
				+"|isLastPage="+page.isLastPage()+"|isHasNextPage="+page.isHasNextPage()+"|isHasPrevPage="+page.isHasPrevPage());
		Assert.assertEquals(1, page.getNextPageNo());
		Assert.assertEquals(1, page.getPrevPageNo());
		Assert.assertEquals(true, page.isFirstPage());
		Assert.assertEquals(true, page.isLastPage());
		Assert.assertEquals(false, page.isHasPrevPage());
		Assert.assertEquals(false, page.isHasNextPage());
	}
	@Test
	public void test4() {
		Page<Map<String,String>> page = new Page<>(1,20,25);
		Assert.assertEquals(1, page.getCurrentPage());
		System.out.println("|lastPageNo="
				+page.getLastPageNo()+"|getPrevPageNo="+page.getPrevPageNo()+"|getNextPageNo="+page.getNextPageNo()+"|isFirstPage="+page.isFirstPage()
				+"|isLastPage="+page.isLastPage()+"|isHasNextPage="+page.isHasNextPage()+"|isHasPrevPage="+page.isHasPrevPage());
		Assert.assertEquals(2, page.getNextPageNo());
		Assert.assertEquals(1, page.getPrevPageNo());
		Assert.assertEquals(true, page.isFirstPage());
		Assert.assertEquals(false, page.isLastPage());
		Assert.assertEquals(false, page.isHasPrevPage());
		Assert.assertEquals(true, page.isHasNextPage());
	}
	@Test
	public void test5() {
		Page<Map<String,String>> page = new Page<>(2,20,25);
		Assert.assertEquals(2, page.getCurrentPage());
		System.out.println("|lastPageNo="
				+page.getLastPageNo()+"|getPrevPageNo="+page.getPrevPageNo()+"|getNextPageNo="+page.getNextPageNo()+"|isFirstPage="+page.isFirstPage()
				+"|isLastPage="+page.isLastPage()+"|isHasNextPage="+page.isHasNextPage()+"|isHasPrevPage="+page.isHasPrevPage());
		Assert.assertEquals(2, page.getNextPageNo());
		Assert.assertEquals(1, page.getPrevPageNo());
		Assert.assertEquals(false, page.isFirstPage());
		Assert.assertEquals(true, page.isLastPage());
		Assert.assertEquals(true, page.isHasPrevPage());
		Assert.assertEquals(false, page.isHasNextPage());
	}
	@Test
	public void test6() {
		Page<Map<String,String>> page = new Page<>(2,20,63);
		Assert.assertEquals(2, page.getCurrentPage());
		System.out.println("|lastPageNo="
				+page.getLastPageNo()+"|getPrevPageNo="+page.getPrevPageNo()+"|getNextPageNo="+page.getNextPageNo()+"|isFirstPage="+page.isFirstPage()
				+"|isLastPage="+page.isLastPage()+"|isHasNextPage="+page.isHasNextPage()+"|isHasPrevPage="+page.isHasPrevPage());
		Assert.assertEquals(3, page.getNextPageNo());
		Assert.assertEquals(1, page.getPrevPageNo());
		Assert.assertEquals(false, page.isFirstPage());
		Assert.assertEquals(false, page.isLastPage());
		Assert.assertEquals(true, page.isHasPrevPage());
		Assert.assertEquals(true, page.isHasNextPage());
	}
}
