package com.meizu.dao.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.meizu.simplify.exception.UncheckedException;

//import com.fasterxml.jackson.annotation.JsonIgnore;
   
/**
 *  
 * <p><b>Title:</b><i>分页实体</i></p>
 * <p>Desc: 对分页的基本数据进行一个简单的封装</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2014年12月22日 下午2:39:51</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2014年12月22日 下午2:39:51</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class Page<T> extends AbstractPage<T> implements IPage<T>{  
   
	static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"  
            + "(\\b(and|exec|execute|insert|select|delete|update|count|drop|\\*|%|chr|mid|master|truncate|char|declare|sitename|net user|xp_cmdshell|;|or|-|\\+|,|like'|and|exec|execute|insert|create|drop|table|from|grant|use|group_concat|column_name|information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|chr|mid|master|truncate|char|declare|or|;|-|--|,|like|//|/|%|#)\\b)";  
  
    static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);  

	private static final long serialVersionUID = -1096861038358450099L;
	//js grid控件中不会用到,需要seo优化的存html的表格页面上会用到,需要页面跳转 start
//	@JsonIgnore
	private boolean isFirstPage;
//	@JsonIgnore
	private boolean isLastPage;
//	@JsonIgnore
	private boolean hasNextPage;
//	@JsonIgnore
	private boolean hasPreviousPage;
	//js grid控件中不会用到,需要seo优化的存html的表格页面上会用到,需要页面跳转 end
	
	private int pageNo = 1;//页码，默认是第一页  
	public String getSortname() {
		return sortname;
	}
	public void setSortname(String sortname) {
		if (sqlPattern.matcher(sortname).find()) { 
			this.sortname = "";
		}else{
			this.sortname = sortname;
		}
	}
	public String getSortorder() {
		return sortorder;
	}
	public void setSortorder(String sortorder) {
		if (sqlPattern.matcher(sortorder).find()) { 
			this.sortorder = "";
		}else{
			this.sortorder = sortorder;
		}
	}
	
	private int start = 0;// 当前页第一条数据在List中的位置,从0开始
	private static int DEFAULT_PAGE_SIZE = 10;
    private int pageSize = 10;//每页显示的记录数，默认是10  
    private int totalRecord;//总记录数  
    private int totalPage;//总页数  
	private List<T> results;//对应的当前页记录  
    private String url;//请求URL
    private String sortname; //排序字段
    private String sortorder; //排序属性
    

	private List data; // 当前页中存放的记录,类型一般为List
	/**
	 * 构造方法，只构造空页.
	 */
    public Page(){
    	super();
    	this.pageSize = DEFAULT_PAGE_SIZE;
    }
    /**
	 * 默认构造方法.
	 *
	 * @param start	 本页数据在数据库中的起始位置
	 * @param totalSize 数据库中总记录条数
	 * @param pageSize  本页容量
	 * @param data	  本页包含的数据
	 */
	public Page(int start, int totalSize, int pageSize, List data) {
		this.pageSize = pageSize;
		this.start = start;
		this.totalRecord = totalSize;
		this.data = data;
	}
    /**
	 * 根据当前页号、页大小（每页数据个数）、当前页数据列表、数据总个数构造分页数据对象的实例。
	 * @param pageNo 当前页号
	 * @param pageSize 页大小（每页数据个数）
	 * @param elements 当前页数据列表
	 * @param totalCount 数据总个数
	 */
	public Page(
			int pageNo, int pageSize, List<T> results, int totalRecord) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.results = results;
		this.totalRecord = totalRecord;
	}
	
	public Page(Integer recordCount, Integer pageNo, Integer pageSize) {
		super(recordCount,pageNo,pageSize);
	}
	public Page(Integer pageSize) {
		super(pageSize);
	}
    /**
     * 定义一空页
     * @see #emptyPage()
     */
	public static final Page EMPTY_PAGE = new Page(
			0, 0, Collections.emptyList(), 0);

    /**
     * 获取一空页
     */
	public static <E> Page<E> emptyPage() {
		return (Page<E>) EMPTY_PAGE;
	}
    
    
	private Map<String, Object> params = new HashMap<String, Object>();//其他的参数我们把它分装成一个Map对象  
   
	
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
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	@Override
	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}
	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	@Override
	public int getLastPageNo() {
		return this.getTotalPage();
	}

	@Override
	public int getThisPageFirstElementNumber() {
		return pageNo*pageSize-1;
	}

	@Override
	public int getThisPageLastElementNumber() {
		return (pageNo+1)*pageSize-1;
		
	}

	@Override
	public int getNextPageNo() {
		if(pageNo>totalPage) {
			return totalPage;
		}
		return pageNo+1;
	}

	@Override
	public int getPreviousPageNo() {
		if(pageNo<1) {
			return 1;
		}
		return pageNo-1;
	}  
	
  	public int getStart() {
  		if(pageNo<1) {
  			pageNo = 1;
  		}
  		start = (pageNo-1)*pageSize;
  		return start;
  	}

  	public void setStart(int start) {
  		this.start = start;
  	}
  	@Override
    public int getPageNo() {  
       return pageNo;  
    }  
   
    public void setPageNo(int pageNo) {  
       this.pageNo = pageNo;  
    } 
    
    /* 取每页数据容量
     * @see com.meizu.util.IPage#getPageSize()
     */
    @Override
    public int getPageSize() {  
       return pageSize;  
    }  
   
    public void setPageSize(int pageSize) {  
       this.pageSize = pageSize;  
    }  
   
    

	/**
	 * 取总页数.
	 */
	public long getTotalPageCount() {
		if (totalRecord % pageSize == 0)
			return totalRecord / pageSize;
		else
			return totalRecord / pageSize + 1;
	}
	/**
	 * 取总记录数.
	 */
    @Override
    public int getTotalRecord() {  
       return totalRecord;  
    }  
   
    public void setTotalRecord(Integer totalRecord) {  
    	if(totalRecord == null) {
    		totalRecord =0;
    	}
    	if(totalRecord<0) {
    		throw new UncheckedException("总记录数必须大于等于零！");
    	}
       this.totalRecord = totalRecord;  
       //在设置总页数的时候计算出对应的总页数，在下面的三目运算中加法拥有更高的优先级，所以最后可以不加括号。  
       int totalPage = totalRecord%pageSize==0 ? totalRecord/pageSize : totalRecord/pageSize + 1;  
       this.setTotalPage(totalPage);  
    }  
    @Override
    public int getTotalPage() {  
       return totalPage;  
    }  
   
    public void setTotalPage(int totalPage) {  
       this.totalPage = totalPage;  
    }  
   
    @Override
    public List<T> getResults() {  
       return results;  
    }  
   
    public void setResults(List<T> results) {  
       this.results = results;  
    }  
     
   

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	@Override  
    public String toString() {  
       StringBuilder builder = new StringBuilder();  
       builder.append("Page [pageNo=").append(pageNo).append(", pageSize=")  
              .append(pageSize).append(", results=").append(results).append(  
                     ", totalPage=").append(totalPage).append(  
                     ", totalRecord=").append(totalRecord).append("]");  
       return builder.toString();  
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	 /**
     * 根据页大小（每页数据个数）获取给定页号的第一条数据在总数据中的位置（从1开始）
     * @param pageNo 给定的页号
     * @param pageSize 页大小（每页数据个数）
     * @return 给定页号的第一条数据在总数据中的位置（从1开始）
     */
    public static int getStartOfPage(int pageNo, int pageSize) {
        int startIndex = (pageNo - 1) * pageSize + 1;
        if (startIndex < 1) startIndex = 1;
        return startIndex;
    }
    

	public void setData(List data) {
		this.data = data;
	}

	/**
	 * 取当前页中的记录.
	 */
	public List getResult() {
		return data;
	}

	/**
	 * 取该页当前页码,页码从1开始.
	 */
	public long getCurrentPageNo() {
		return start / pageSize + 1;
	}

	/**
	 * 该页是否有下一页.
	 */
	public boolean hasNextPage() {
		return this.getCurrentPageNo() < this.getTotalPageCount() - 1;
	}

	/**
	 * 该页是否有上一页.
	 */
	public boolean hasPreviousPage() {
		return this.getCurrentPageNo() > 1;
	}

	/**
	 * 获取任一页第一条数据在数据集的位置，每页条数使用默认值.
	 *
	 * @see #getStartOfPage(long,long)
	 */
	protected static long getStartOfPage(long pageNo) {
		return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
	}

	/**
	 * 获取任一页第一条数据在数据集的位置.
	 *
	 * @param pageNo   从1开始的页号
	 * @param pageSize 每页记录条数
	 * @return 该页第一条数据
	 */
	public static long getStartOfPage(long pageNo, long pageSize) {
		return (pageNo - 1) * pageSize;
	}
}  