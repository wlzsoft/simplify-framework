package com.meizu.data.util;



import java.util.List;

/**
 * 
 * <p><b>Title:</b><i>支持泛型的分页数据接口</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午5:12:05</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午5:12:05</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public interface IPage<T> {
	
	/**
	 * 
	 * 方法用途: 是否为第一页<br>
	 * 操作步骤: TODO<br>
	 * @return 是否为第一页
	 */
	boolean isFirstPage();

	/**
	 * 
	 * 方法用途: 是否为最后一页 <br>
	 * 操作步骤: TODO<br>
	 * @return 是否为最后一页 
	 */
	boolean isLastPage();

	/**
	 * 
	 * 方法用途:  有无下一页 <br>
	 * 操作步骤: TODO<br>
	 * @return 真(true)为有下一页，假(false)为无
	 */
	boolean isHasNextPage();

	/**
	 * 
	 * 方法用途: 有无上一页 <br>
	 * 操作步骤: TODO<br>
	 * @return 真(true)为有上一页，假(false)为无
	 */
	boolean isHasPreviousPage();

	/**
	 * 
	 * 方法用途: 得到最后一页的页号 <br>
	 * 操作步骤: TODO<br>
	 * @return 最后一页的页号 
	 */
	int getLastPageNo();

	

	/**
	 * 
	 * 方法用途: 得到数据总个数<br>
	 * 操作步骤: TODO<br>
	 * @return 数据总个数
	 */
	int getTotalRecord();
	/**
	 * 
	 * 方法用途: 获取当前页第一条记录在所有记录的编号，即数据项范围上标<br>
	 * 操作步骤: TODO<br>
	 * @return 当前页第一条记录在所有记录的编号
	 */
	int getThisPageFirstElementNumber();

	/**
	 * 
	 * 方法用途: 获取当前页最后一条记录在所有记录的编号 <br>
	 * 操作步骤: TODO<br>
	 * @return 当前页最后一条记录在所有记录的编号
	 */
	int getThisPageLastElementNumber();
    
	/**
	 * 
	 * 方法用途: 得到下一页的页号<br>
	 * 操作步骤: TODO<br>
	 * @return 下一页的页号
	 */
	int getNextPageNo();

	/**
	 * 
	 * 方法用途: 得到前一页的页号 <br>
	 * 操作步骤: TODO<br>
	 * @return 前一页的页号
	 */
	int getPreviousPageNo();

	/**
	 * 
	 * 方法用途: 得到设定的页大小（每页数据个数） <br>
	 * 操作步骤: TODO<br>
	 * @return 页大小 （每页数据个数）
	 */
	int getPageSize();

	/**
	 * 
	 * 方法用途: 得到当前页的页号 <br>
	 * 操作步骤: TODO<br>
	 * @return 当前页的页号 
	 */
	int getPageNo();
	/**
	 * 
	 * 方法用途: 得到总页数<br>
	 * 操作步骤: TODO<br>
	 * @return 总页数
	 */
	int getTotalPage();

	/**
	 * 
	 * 方法用途: 获取当前页中包含的数据<br>
	 * 操作步骤: TODO<br>
	 * @return 当前页中包含的数据
	 */
	List<T> getResults();

}
