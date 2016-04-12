package com.meizu.simplify.dao.util;

import com.meizu.simplify.dao.IDBUtilDao;
import com.meizu.simplify.ioc.annotation.Resource;


/**
 * <p><b>Title:</b><i>数据库函数工具类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月18日 上午11:38:37</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月18日 上午11:38:37</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class DBUtil  {
	
	@Resource
	private static IDBUtilDao dao;
	
	/**
	 * 方法用途: 获取数据库系统当前时间<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static String getDbDate(){
		return dao.getDbDate();
	}
	
	
	
}
