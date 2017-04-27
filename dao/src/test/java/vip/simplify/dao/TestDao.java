package vip.simplify.dao;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * <p><b>Title:</b><i>不使用其他模块的dao使用方式</i></p>
 * <p>Desc: 无容器注入和初始化，考虑更简单场景的使用</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月14日 上午10:20:33</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月14日 上午10:20:33</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class TestDao  {
 
	public Object findByTo(Connection conn ,short to,int top)  {
		try {
			conn.prepareStatement("select * from test where id = ?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null; 
	}
	 
}