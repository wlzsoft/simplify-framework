package vip.simplify.jdk;

import org.junit.Test;

/**
 * <p><b>Title:</b><i>final类型变量和其他变量的区别</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月5日 下午2:59:54</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月5日 下午2:59:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class FinalTest {
	@Test
    public  void test()  {
        String a = "hello2"; 
        final String b = "hello";//编译器能够确认final变量的值
        String d = "hello";
        String c = b + 2; 
        String e = d + 2;
        System.out.println((a == c));
        System.out.println((a == e));
    }

	@Test
	public void test2() {
		String a = "hello2";
		final String b = getHello();//编译器无法确认final 变量 b的值
		String c = b + 2;
		System.out.println((a == c));

	}

	public static String getHello() {
		return "hello";
	}
}