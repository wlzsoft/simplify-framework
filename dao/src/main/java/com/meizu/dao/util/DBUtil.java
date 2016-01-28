package com.meizu.dao.util;

import com.meizu.dao.ICommonDao;
import com.meizu.simplify.ioc.annotation.Resource;


/**
 * <p><b>Title:</b><i>数据库工具类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月18日 上午11:38:37</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月18日 上午11:38:37</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class DBUtil  {
	
	@Resource
	private static ICommonDao dao;
	
	/**
	 * 方法用途: 获取系统当前时间<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static String getMysqlDbDateByString(){
		String dateStr = dao.getMysqlDbDate();
		return dateStr;
	}
	
	
	
}
