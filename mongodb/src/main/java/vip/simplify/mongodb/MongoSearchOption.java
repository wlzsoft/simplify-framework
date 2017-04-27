package vip.simplify.mongodb;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * <p>搜索条件</p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月25日 上午10:45:39</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月25日 上午10:45:39</p>
 * @author <a href="mailto:wanghaibin@meizu.com">wanghb</a>
 * @version Version 3.0
 *
 */
public class MongoSearchOption implements Serializable {

	private static final long serialVersionUID = 4648572853760287380L;
	private SearchLogic searchLogic = SearchLogic.must;
	private SearchType searchType = SearchType.querystring;
	private DataFilter dataFilter = DataFilter.exists;

	public enum SearchType {
		/* 按照quert_string搜索，搜索非词组时候使用 */
		querystring,
		range,//按照区间搜索 
		term,//完全匹配，即不进行分词器分析，文档中必须包含整个搜索的词汇
		match,//精度低
		matchphrase//完全匹配
		
	}

	public enum SearchLogic {
		/* 逻辑must关系 */
		must
		/* 逻辑should关系 */
		, should
	}

	public enum DataFilter {
		/* 只显示有值的 */
		exists
		/* 显示没有值的 */
		, notExists
		/* 显示全部 */
		, all
	}

	public MongoSearchOption(SearchType searchType, SearchLogic searchLogic, DataFilter dataFilter, float boost, int highlight) {
		this.setSearchLogic(searchLogic);
		this.setSearchType(searchType);
		this.setDataFilter(dataFilter);
	}

	public MongoSearchOption() {
	}

	public DataFilter getDataFilter() {
		return this.dataFilter;
	}

	public void setDataFilter(DataFilter dataFilter) {
		this.dataFilter = dataFilter;
	}

	public SearchLogic getSearchLogic() {
		return this.searchLogic;
	}

	public void setSearchLogic(SearchLogic searchLogic) {
		this.searchLogic = searchLogic;
	}

	public SearchType getSearchType() {
		return this.searchType;
	}

	public void setSearchType(SearchType searchType) {
		this.searchType = searchType;
	}
	public static long getSerialversionuid() {
		return MongoSearchOption.serialVersionUID;
	}

	public static String formatDate(Object object) {
		if (object instanceof java.util.Date) {
			return MongoSearchOption.formatDateFromDate((java.util.Date) object);
		}
		return MongoSearchOption.formatDateFromString(object.toString());
	}

	public static boolean isDate(Object object) {
		return object instanceof java.util.Date || Pattern.matches("[1-2][0-9][0-9][0-9]-[0-9][0-9].*", object.toString());
	}

	public static String formatDateFromDate(Date date) {
		SimpleDateFormat dateFormat_hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String result = dateFormat_hms.format(date);
			return result;
		} catch (Exception e) {
		}
		try {
			String result = dateFormat.format(date) + "00:00:00";
			return result;
		} catch (Exception e) {
		}
		return dateFormat_hms.format(new Date());
	}

	public static String formatDateFromString(String date) {
		SimpleDateFormat dateFormat_hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date value = dateFormat_hms.parse(date);
			return MongoSearchOption.formatDateFromDate(value);
		} catch (Exception e) {
		}
		try {
			Date value = dateFormat.parse(date);
			return MongoSearchOption.formatDateFromDate(value);
		} catch (Exception e) {
		}
		return dateFormat_hms.format(new Date());
	}
}