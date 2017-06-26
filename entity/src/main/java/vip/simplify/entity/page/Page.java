package vip.simplify.entity.page;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import vip.simplify.exception.UncheckedException;

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

	private static final long serialVersionUID = 528967450281022746L;

//为了安全，考虑到order by 无法使用预处理，这里使用正则过滤特殊的字符 start
	static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
			+ "(\\b(and|exec|execute|insert|select|delete|update|count|drop|\\*|%|chr|mid|master|truncate|char|declare|sitename|net user|xp_cmdshell|;|or|-|\\+|,|like'|and|exec|execute|insert|create|drop|table|from|grant|use|group_concat|column_name|information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|chr|mid|master|truncate|char|declare|or|;|-|--|,|like|//|/|%|#)\\b)";
	static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
//为了安全，考虑到order by 无法使用预处理，这里使用正则过滤特殊的字符 end

// 	以下属性 ：js grid控件中不会用到,需要seo优化的存html的表格页面上会用到,需要页面跳转 start
	
	// @JsonIgnore
	private boolean isFirstPage = true;
	// @JsonIgnore
	private boolean isLastPage = true;
	// @JsonIgnore
	private boolean hasNextPage = false;
	// @JsonIgnore
	private boolean hasPrevPage = false;
	
	/**
	 * 下一页页码
	 */
	private int nextPage = 1;
	
	/**
	 * 上一页页码
	 */
	private int prevPage = 1;
	
	/**
	 * 请求URL，如果是ajax的话，可以不必要带上url，会增加服务端负担，虽说可以开发
	 */
	private String url;
	
//	以上属性：js grid控件中不会用到,需要seo优化的存html的表格页面上会用到,需要页面跳转 end
	
	/**
	 * 当前页第一条数据在数据库中位置,从0开始,在mysql limit 中就是第一个参数
	 */
	private int currentRecord = 0;
	
	private static int DEFAULT_PAGE_SIZE = 10;
	/**
	 * 当前页码(当前页数) ，默认是第一页
	 */
	private int currentPage = 1;
	
	/**
	 * 每页显示的记录数，默认是10
	 */
	private int pageSize = DEFAULT_PAGE_SIZE; 
	
	/**
	 * 总记录数   需要在运行时设置，一般表示检索的记录总数
	 */
	private int totalRecord = 0;
	
	/**
	 * 总页数
	 */
	private int totalPage;
	
	/**
	 * 最大的翻页数: 默认值为-1，-1 时不限制，并以totalPage为基准计算翻页。非-1可以限制翻页范围，并以maxPage的范围为基准计算翻页
	 */
	private int maxPage=-1;
	
	/**
	 * 排序字段名：配合Dao一起使用，相当于Dao类中 方法的 sortName参数
	 */
	private String sortName;
	
	/**
	 * 排序类型，是否倒序：配合Dao一起使用 ,相当于 isDesc 是否降序 [排序方式（升序(asc)或降序(desc)]  sort order by 
	 */
	private Boolean isDesc; 
	
	/**
	 * 对应的当前页存放记录
	 */
	private List<T> results;
	
	/**
	 * 该无参构造方法紧用于序列化的时候需要调用：1.比如远程方法调用时候会用到序列化
	 * 和具体的序列化框架有关,需要确认是否在反序列化时触发
	 */
	public Page(){
	}
	/**
	 * 构造方法
	 * 通过指定记录总数、当前页数、每页记录数来构造一个分页对象
	 * 根据当前页码、页大小（每页数据个数）、当前页数据列表、数据总个数构造分页数据对象的实例
	 * @param currentPage 当前页码
	 * @param pageSize 页大小（每页数据个数）
	 * @param totalRecord 数据库中总记录条数
	 */
	public Page(int currentPage, int pageSize,int totalRecord) {
		this(currentPage,pageSize,totalRecord,true);
	}
	
	/**
	 * 不建议直接调用这个构建函数，如果使用，要注意调用init方法,并指定totalRecord
	 * @param currentPage
	 * @param pageSize
	 */
	public Page(int currentPage, int pageSize) {
		this(currentPage,pageSize,0,true);
	}
	
	/**
	 * 方法用途: 构建总页数<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	private int buildTotalPage(int pageSize,int totalRecord) {
		if (totalRecord % pageSize > 0) {
			//加法拥有更高的优先级，所以最后可以不加括号
			return totalRecord / pageSize + 1;
		} else {
			return totalRecord / pageSize;
		}
	}
	
	/**
	 * 构造方法
	 * 通过指定记录总数、当前页数、每页记录数来构造一个分页对象
	 * 根据当前页码、页大小（每页数据个数）、当前页数据列表、数据总个数构造分页数据对象的实例
	 * @param currentPage 当前页码
	 * @param pageSize 页大小（每页数据个数）
	 * @param totalRecord 数据库中总记录条数
	 * @param isReturnLastPage TODO 注意：为了兼容移动端和pc端多种风格的分页，提供一个控制参数，来指定是否需要返回最后一页记录
	 */
	public Page(int currentPage, int pageSize,int totalRecord,boolean isReturnLastPage) {

		this.currentPage = currentPage;
		this.pageSize = pageSize;
		results = new ArrayList<T>();
		if(totalRecord <= 0) {
			return;
		}
		init(totalRecord, isReturnLastPage);

	}
	
	/**
	 * 
	 * 方法用途: 外部不建议直接调用这个方法，如果使用，要注意调用init方法,并指定totalRecord<br>
	 * 操作步骤: TODO<br>
	 * @param totalRecord
	 * @param isReturnLastPage
	 */
	public void init(int totalRecord, boolean isReturnLastPage) {
		if(totalRecord < 0 ) {
			throw new UncheckedException("totalRecord:总记录数不能小于0");
		}
		this.setTotalRecord(totalRecord);
		// 在设置总记录数后计算出对应的总页数
		totalPage = buildTotalPage(pageSize,totalRecord);

		if (this.currentPage > totalPage) {//确保如果记录数大于数据库总记录数时[isReturnLastPage为true永远返回数据库最后一页,否则返回下一页空记录]
			if(isReturnLastPage) {
				this.currentPage = totalPage;
			} else {
				this.currentPage = totalPage+1;
			}
		}

		if (this.currentPage <= 1) {
			isFirstPage = true;
			prevPage = this.currentPage;
		} else {
			isFirstPage = false;
			prevPage = this.currentPage - 1;
		}

		if (this.currentPage >= totalPage) {
			isLastPage = true;
			nextPage = this.currentPage;
		} else {
			isLastPage = false;
			nextPage = this.currentPage + 1;
		}
		
		hasNextPage = this.currentPage < totalPage;
		hasPrevPage = this.currentPage > 1;
	}
	

	/**
	 * 
	 * 方法用途: 获取一空页<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static <E> Page<E> emptyPage() {
		Page<E> page = new Page<>(0, 0, 0,true);
		return (Page<E>) page;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		if (sqlPattern.matcher(sortName).find()) {
			this.sortName = "";
		} else {
			this.sortName = sortName;
		}
	}

	public Boolean getIsDesc() {
		return isDesc;
	}
	
	public void setIsDesc(Boolean isDesc) {
		this.isDesc = isDesc;
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
	public int getCurrentPageFirstRecord() {
		return currentPage * pageSize - 1;
	}

	@Override
	public int getCurrentPageLastRecord() {
		return (currentPage + 1) * pageSize - 1;

	}

	@Override
	public int getNextPage() {
		return nextPage;
	}

	@Override
	public int getPrevPage() {
		return prevPage;
	}
	/**
	 * 
	 * 方法用途: 根据页大小（每页数据个数）获取给定页码的第一条数据在总数据中的位置（从1开始）<br>
	 * 操作步骤: TODO<br>
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

	@Override
	public int getCurrentPage() {
		if(maxPage>0) {
			if(currentPage>maxPage) {
				return maxPage;
			}
		}
		return currentPage;//currentRecord / pageSize + 1
	}
	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/* 
	 * @see vip.simplify.dao.util.IPage#getPageSize()
	 */
	@Override
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/* 
	 * @see vip.simplify.dao.util.IPage#getTotalRecord()
	 */
	@Override
	public int getTotalRecord() {
		return totalRecord;
	}

	/**
	 * 
	 * 方法用途: 该方法只可用于内部调用<br>
	 * 操作步骤: 这里的访问权限是public，主要是用于Json转换需要使用到set方法，所以才提供<br>
	 * @param totalRecord
	 */
	public void setTotalRecord(Integer totalRecord) {
		if (totalRecord == null) {
			totalRecord = 0;
		}
		if (totalRecord < 0) {
			throw new UncheckedException("总记录数必须大于等于零！");
		}
		this.totalRecord = totalRecord;
	}

	@Override
	public int getTotalPage() {
		if(maxPage>0){
			if(totalPage > maxPage) {
				return maxPage;
			} else {
				return totalPage;
			}
		}
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	public int getMaxPage() {
		return maxPage;
	}
	
	/**
	 * 
	 * 方法用途: 设置最大的翻页数: 默认值为-1，-1 时不限制，并以totalPage为基准计算翻页。非-1可以限制翻页范围，并以maxPage的范围为基准计算翻页<br>
	 * 操作步骤: TODO<br>
	 * @param maxPage
	 */
	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	/* (non-Javadoc)
	 * @see vip.simplify.dao.util.IPage#getResults()
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
	
	/**
	 * 方法用途: 获取任一页第一条数据在数据集的位置，每页条数使用默认值<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public Integer getStartOfPage() {
		return (currentPage - 1) * pageSize;
	}
	
	/**
	 * 获取任一页最后一条数据在数据集的位置
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public Integer getEndOfPage(){
		Integer preEndPageNo = getStartOfPage()+pageSize;
		if(preEndPageNo<totalRecord){
			return preEndPageNo-1;
		} else {//如果当前页的最后一条记录超过总页数，那么最后一条记录为总页数减一
			return totalRecord-1;
		}
	}
	
	/**
	 * 方法用途: 获取任一页第一条数据在数据集的位置，每页条数使用默认值<br>
	 * 操作步骤: 注意这个是静态工具方法<br>
	 * @param currentPage
	 * @see #getStartByPage(Integer,Integer)
	 * @return
	 */
	public static Integer getStartByPage(Integer currentPage) {
		return getStartByPage(currentPage, DEFAULT_PAGE_SIZE);
	}

	/**
	 * 
	 * 方法用途: 获取任一页第一条数据在数据集的位置<br>
	 * 操作步骤: 注意这个是静态工具方法<br>
	 * @param currentPage 从1开始的页码
	 * @param pageSize 每页记录条数
	 * @return 该页第一条数据
	 */
	public static Integer getStartByPage(Integer currentPage, Integer pageSize) {
		return (currentPage - 1) * pageSize;
	}
	
	
//	这里提供的set方法，主要是用于Json转换，所以才提供，正常使用过程中，不应该调用这些方法 start
	
	/**
	 * 
	 * 方法用途: 这个set方法主要是用于Json转换注入，所以才提供，非对用户开放api，正常使用过程中，不应该调用这些方法<br>
	 * 操作步骤: TODO<br>
	 * @param hasNextPage
	 */
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	
	/**
	 * 
	 * 方法用途: 这个set方法主要是用于Json转换注入，所以才提供，非对用户开放api，正常使用过程中，不应该调用这些方法<br>
	 * 操作步骤: TODO<br>
	 * @param hasPrevPage
	 */
	public void setHasPrevPage(boolean hasPrevPage) {
		this.hasPrevPage = hasPrevPage;
	}
	
	/**
	 * 
	 * 方法用途: 这个set方法主要是用于Json转换注入，所以才提供，非对用户开放api，正常使用过程中，不应该调用这些方法<br>
	 * 操作步骤: TODO<br>
	 * @param nextPage
	 */
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	
	/**
	 * 
	 * 方法用途: 这个set方法主要是用于Json转换注入，所以才提供，非对用户开放api，正常使用过程中，不应该调用这些方法<br>
	 * 操作步骤: TODO<br>
	 * @param prevPage
	 */
	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}
	
	/**
	 * 
	 * 方法用途: 这个set方法主要是用于Json转换注入，所以才提供，非对用户开放api，正常使用过程中，不应该调用这些方法<br>
	 * 操作步骤: TODO<br>
	 * @param currentRecord
	 */
	public void setCurrentRecord(int currentRecord) {
		this.currentRecord = currentRecord;
	}
//	 end
	
	/**
	 * 
	 * 方法用途: 获得翻页导航<br>
	 * 操作步骤: TODO 带整理，使用场景有限 <br>
	 * @return
	 */
	/*public int [] getNavigation(){
		int tempTotalPage;
		if(maxPage<totalPage) {
			tempTotalPage = maxPage;
		} else {
			tempTotalPage = totalPage;
		}
		int [] navigationPage = new int[tempTotalPage];
		int i=0;
		if(currentPage <= tempTotalPage/2){
			for(; i<tempTotalPage; i++) {				
				navigationPage[i] = i+1;
			}
		} else {
			int count = currentPage - tempTotalPage/2 + 1;
			for(; i+count-1<tempTotalPage; i++) {
				navigationPage[i] = i+count;
			}
		}		
		return Arrays.copyOfRange(navigationPage,0,i);
	}*/
	
	/*@Override
	public int hashCode() {	
		return totalPage
			^(currentPage<<4)
			^(pageSize<<8)
			^(int)totalRecord^0x238F;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this==obj){
			return true;
		}
		if(obj instanceof Page){
			@SuppressWarnings("unchecked")
			Page<T> p = (Page<T>)obj;
			return totalPage==p.totalPage
					&&currentPage==p.currentPage
					&&pageSize==p.pageSize
					&&totalRecord==p.totalRecord;
		}
		return super.equals(obj);
	}*/
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Page [currentPage=").append(currentPage).append(", pageSize=").append(pageSize).append(", results=")
				.append(results).append(", totalPage=").append(totalPage).append(", totalRecord=").append(totalRecord)
				.append("]");
		return builder.toString();
	}
}