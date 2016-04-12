package com.meizu.simplify.dao.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.meizu.simplify.exception.UncheckedException;

/**
 * <p><b>Title:</b><i>分页实体</i></p>
 * <p>Desc: 对分页的基本数据的封装</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月28日 下午1:31:45</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月28日 下午1:31:45</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 * @param <T> 分页对象中包含内容的对象类型
 */
public class Page<T> implements IPage<T> {

	private static final long serialVersionUID = 528967450681022746L;

	//为了安全，考虑到order by 无法使用预处理，这里使用正则过滤特殊的字符 start
	static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
			+ "(\\b(and|exec|execute|insert|select|delete|update|count|drop|\\*|%|chr|mid|master|truncate|char|declare|sitename|net user|xp_cmdshell|;|or|-|\\+|,|like'|and|exec|execute|insert|create|drop|table|from|grant|use|group_concat|column_name|information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|chr|mid|master|truncate|char|declare|or|;|-|--|,|like|//|/|%|#)\\b)";
	static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
	//为了安全，考虑到order by 无法使用预处理，这里使用正则过滤特殊的字符 end

	// js grid控件中不会用到,需要seo优化的存html的表格页面上会用到,需要页面跳转 start
	// @JsonIgnore
	private boolean isFirstPage;
	// @JsonIgnore
	private boolean isLastPage;
	// @JsonIgnore
	private boolean hasNextPage;
	// @JsonIgnore
	private boolean hasPrevPage;
	private Integer next = 1;// 下一页页码
	private Integer prev = 1;// 上一页页码
	private String url;// 请求URL，如果是ajax的话，可以不必要带上url，会增加服务端负担，虽说可以开发
	// js grid控件中不会用到,需要seo优化的存html的表格页面上会用到,需要页面跳转 end

	private int currentRecord = 0;// 当前页第一条数据在数据库中位置,从0开始,在mysql limit 中就是第一个参数
	private static int DEFAULT_PAGE_SIZE = 10;
	private int currentPage = 1;// 当前页码(当前页数) ，默认是第一页
	private int pageSize = DEFAULT_PAGE_SIZE;// 每页显示的记录数，默认是10
	private int totalRecord;// 总记录数
	private int totalPage;// 总页数
	private String sortname; // 排序字段
	private String sortorder; // 排序属性
	private List<T> results;// 对应的当前页存放记录
	
	/**
	 * 构造方法
	 * 通过指定记录总数、当前页数、每页记录数来构造一个分页对象
	 * 根据当前页码、页大小（每页数据个数）、当前页数据列表、数据总个数构造分页数据对象的实例
	 * @param currentPage 当前页码
	 * @param pageSize 页大小（每页数据个数）
	 * @param totalRecord 数据库中总记录条数
	 */
	public Page(int currentPage, int pageSize,int totalRecord) {

		this.currentPage = currentPage;
		this.pageSize = pageSize;
		setTotalRecord(totalRecord);
		
		if (totalRecord % pageSize > 0) {
			totalPage = totalRecord / pageSize + 1;
		} else {
			totalPage = totalRecord / pageSize;
		}

		if (totalPage < currentPage) {
			this.currentPage = totalPage;
		} else {
			this.currentPage = currentPage;
		}

		if (this.currentPage <= 1) {
			isFirstPage = true;
			prev = this.currentPage;
		} else {
			isFirstPage = false;
			prev = this.currentPage - 1;
		}

		if (this.currentPage >= totalPage) {
			isLastPage = true;
			next = this.currentPage;
		} else {
			isLastPage = false;
			next = this.currentPage + 1;
		}
		
		hasNextPage = this.getCurrentPage() < this.getTotalPageCount() - 1;
		hasPrevPage = this.getCurrentPage() > 1;
		results = new ArrayList<T>();

	}
	

	/**
	 * 
	 * 方法用途: 获取一空页<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static <E> Page<E> emptyPage() {
		return (Page<E>) new Page(0, 0, 0);
	}

	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		if (sqlPattern.matcher(sortname).find()) {
			this.sortname = "";
		} else {
			this.sortname = sortname;
		}
	}

	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		if (sqlPattern.matcher(sortorder).find()) {
			this.sortorder = "";
		} else {
			this.sortorder = sortorder;
		}
	}
	

	@Override
	public boolean isFirstPage() {
		return isFirstPage;
	}

	public void setFirstPage(boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	@Override
	public boolean isLastPage() {
		return isLastPage;
	}

	public void setLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	@Override
	public boolean isHasNextPage() {
		return hasNextPage;
	}

	@Override
	public boolean isHasPrevPage() {
		return hasPrevPage;
	}

	@Override
	public int getLastPageNo() {
		return this.getTotalPage();
	}

	@Override
	public int getThisPageFirstElementNumber() {
		return currentPage * pageSize - 1;
	}

	@Override
	public int getThisPageLastElementNumber() {
		return (currentPage + 1) * pageSize - 1;

	}

	@Override
	public int getNextPageNo() {
		if (currentPage > totalPage) {
			return totalPage;
		}
		return currentPage + 1;
	}

	@Override
	public int getPrevPageNo() {
		if (currentPage < 1) {
			return 1;
		}
		return currentPage - 1;
	}
	/**
	 * 
	 * 方法用途: 根据页大小（每页数据个数）获取给定页码的第一条数据在总数据中的位置（从1开始）<br>
	 * 操作步骤: TODO<br>
	 * @param currentPage 给定的页码
	 * @param pageSize 页大小（每页数据个数）
	 * @return 给定页码的第一条数据在总数据中的位置（从1开始）
	 */
	public int getCurrentRecord() {
		if (currentPage < 1) {
			currentPage = 1;
		}
		currentRecord = (currentPage - 1) * pageSize;
		if(currentRecord < 0) {
			currentRecord = 0;
		}
		return currentRecord;
	}

	public void setCurrentRecord(int currentRecord) {
		this.currentRecord = currentRecord;
	}
	
	@Override
	public int getCurrentPage() {
		return currentPage;//currentRecord / pageSize + 1
	}
	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/* 
	 * @see com.meizu.simplify.dao.util.IPage#getPageSize()
	 */
	@Override
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 取总页数
	 */
	public long getTotalPageCount() {
		if (totalRecord % pageSize == 0)
			return totalRecord / pageSize;
		else
			return totalRecord / pageSize + 1;
	}

	/* 
	 * @see com.meizu.simplify.dao.util.IPage#getTotalRecord()
	 */
	@Override
	public int getTotalRecord() {
		return totalRecord;
	}

	private void setTotalRecord(Integer totalRecord) {
		if (totalRecord == null) {
			totalRecord = 0;
		}
		if (totalRecord < 0) {
			throw new UncheckedException("总记录数必须大于等于零！");
		}
		this.totalRecord = totalRecord;
		// 在设置总页数的时候计算出对应的总页数，在下面的三目运算中加法拥有更高的优先级，所以最后可以不加括号。
		int totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
		this.setTotalPage(totalPage);
	}

	@Override
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/* (non-Javadoc)
	 * @see com.meizu.simplify.dao.util.IPage#getResults()
	 */
	@Override
	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Integer getNext() {
		return next;
	}

	public void setNext(Integer next) {
		this.next = next;
	}

	public Integer getPrev() {
		return prev;
	}

	public void setPrev(Integer prev) {
		this.prev = prev;
	}

	/**
	 * 获取任一页第一条数据在数据集的位置，每页条数使用默认值
	 * @see #getStartOfPage(long,long)
	 */
	protected static long getStartOfPage(long currentPage) {
		return getStartOfPage(currentPage, DEFAULT_PAGE_SIZE);
	}

	/**
	 * 
	 * 方法用途: 获取任一页第一条数据在数据集的位置<br>
	 * 操作步骤: TODO<br>
	 * @param currentPage 从1开始的页码
	 * @param pageSize 每页记录条数
	 * @return 该页第一条数据
	 */
	public static long getStartOfPage(long currentPage, long pageSize) {
		return (currentPage - 1) * pageSize;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Page [currentPage=").append(currentPage).append(", pageSize=").append(pageSize).append(", results=")
				.append(results).append(", totalPage=").append(totalPage).append(", totalRecord=").append(totalRecord)
				.append("]");
		return builder.toString();
	}
}