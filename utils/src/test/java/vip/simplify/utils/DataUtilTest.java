package vip.simplify.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

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
		Integer o = DataUtil.convertType(Integer.class, "2",false);
		System.out.println(o);
		Date date = DataUtil.convertType(Date.class, "2014-05-14", false);
		System.out.println(date);
		LocalDate localDate = DataUtil.convertType(LocalDate.class, "2014-05-14", false);
		System.out.println(localDate);
		LocalDateTime LocalDateTime = DataUtil.convertType(LocalDateTime.class, "2007-12-03T10:15:30", false);
		System.out.println(LocalDateTime);
		LocalTime LocalTime = DataUtil.convertType(LocalTime.class, "21:09:08", false);
		System.out.println(LocalTime);
	}
	
	/**
	 * 
	 * 方法用途: 可作为基础面试题或基础培训题<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void testNumberEqual() {
		Integer obj = 1;
		Object obj2 = "1";
		System.out.println(obj == 1);
		System.out.println(obj.equals(1));
		System.out.println(obj2.equals("1"));
		System.out.println(obj2=="1");
	}
}
