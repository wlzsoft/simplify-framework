package vip.simplify.mvc.model;

/**
 * <p><b>Title:</b><i>基础表单模型</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月29日 下午4:49:47</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月29日 下午4:49:47</p>
 * @author <a href="mailto:luchuangye@meizu.com">luchuangye</a>
 * @version Version 0.1
 *
 */
public class BaseModel extends Model {

	/**
	 * 排序字段名
	 */
	private String sort;
	/**
	 * 是否倒序
	 */
	private Boolean isDesc = true;
	private Integer currentPage = 1;
	private Integer pageSize = 10;
	/**
	 * 当前请求表单的地址参数:可用于分页地址，获取其他需要携带上次请求地址参数的其他功能,一般用于模版页面中
	 * 目前只支持get请求，如果post请求会丢失
	 */
	private String urlParam;
	
	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Boolean getIsDesc() {
		return isDesc;
	}

	public void setIsDesc(Boolean isDesc) {
		this.isDesc = isDesc;
	}

	public String getUrlParam() {
		return urlParam;
	}

	public void setUrlParam(String urlParam) {
		this.urlParam = urlParam;
	}
	
}
