package com.meizu.simplify.dao.util;



import java.io.Serializable;
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
public interface IPage<T> extends Serializable {
	
	/**
	 * 
	 * 方法用途: 是否为第一页<br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	boolean isFirstPage();

	/**
	 * 
	 * 方法用途: 是否为最后一页 <br>
	 * 操作步骤: TODO<br>
	 * @return  
	 */
	boolean isLastPage();

	/**
	 * 
	 * 方法用途:  有无下一页 <br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	boolean isHasNextPage();

	/**
	 * 
	 * 方法用途: 有无上一页 <br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	boolean isHasPrevPage();

	/**
	 * 
	 * 方法用途: 获取最后一页的页码 <br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	int getLastPageNo();
	/**
	 * 
	 * 方法用途: 获取总记录数<br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	int getTotalRecord();
	/**
	 * 
	 * 方法用途: 获取当前页第一条记录在所有记录的编号，即数据项范围上标<br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	int getThisPageFirstElementNumber();

	/**
	 * 
	 * 方法用途: 获取当前页最后一条记录在所有记录的编号 <br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	int getThisPageLastElementNumber();
    
	/**
	 * 
	 * 方法用途: 获取下一页的页码<br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	int getNextPageNo();

	/**
	 * 
	 * 方法用途: 获取前一页的页码 <br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	int getPrevPageNo();

	/**
	 * 
	 * 方法用途: 获取每页记录数<br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	int getPageSize();

	/**
	 * 
	 * 方法用途: 获取当前页的页码 <br>
	 * 操作步骤: TODO<br>
	 * @return  
	 */
	int getCurrentPage();
	/**
	 * 
	 * 方法用途: 获取总页数<br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	int getTotalPage();

	/**
	 * 
	 * 方法用途: 获取当前页中包含的数据<br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	List<T> getResults();


}
