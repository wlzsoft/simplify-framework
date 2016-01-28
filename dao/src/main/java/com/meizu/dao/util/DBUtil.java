package com.meizu.dao.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import com.meizu.dao.ICommonDao;


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
	private ICommonDao dao;
	
	public static String getMysqlDbDateByString(){
		//ICommonDao commonDao = (ICommonDao)SpringContext.getBean("ICommonDao");
//		return commonDao.getMysqlDbDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	/**
	 * 方法用途: 获取系统当前时间<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	/*public static Date getMysqlDbDate(){
		//String dateStr = SpringContext.getInstance(ICommonDao.class).getMysqlDbDate();
		return DateUtil.getCurrentDate();
	}*/
	
}
