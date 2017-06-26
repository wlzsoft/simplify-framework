package vip.simplify.entity.page;



//import java.io.Externalizable;
import java.io.Serializable;
import java.util.List;

/**
 * 
 * <p><b>Title:</b><i>支持泛型的分页数据接口</i></p>
 * <p>Desc: Externalizable的作用，是否代替Serializable</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午5:12:05</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午5:12:05</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public interface IPage<T> extends /*Externalizable,*/Serializable {
	
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
	 * 方法用途:  是否有下一页<br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	boolean isHasNextPage();

	/**
	 * 
	 * 方法用途: 是否有上一页 <br>
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
	 * 方法用途: 获取当前页第一条数据在所有数据记录的位置编号，即当前页范围上限 <br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	int getStartOfPage();

	/**
	 * 方法用途: 获取当前页最后一条数据在所有数据记录的位置编号，即当前页范围下限 <br>
	 * 操作步骤: TODO<br>
	 * @return 返回-1 说明 totalRecord 为0,返回记录数为空
	 */
	int getEndOfPage();
    
	/**
	 * 
	 * 方法用途: 获取下一页的页码<br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	int getNextPage();

	/**
	 * 
	 * 方法用途: 获取前一页的页码 <br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	int getPrevPage();

	/**
	 * 
	 * 方法用途: 获取每页记录数<br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	int getPageSize();

	/**
	 * 
	 * 方法用途: 获取当前页的页码,页码从1开始 <br>
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
