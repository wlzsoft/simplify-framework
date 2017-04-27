package vip.simplify.dao.orm.base;

import vip.simplify.utils.StringUtil;

/**
 * <p><b>Title:</b><i>公用的sql构建器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月31日 下午5:45:25</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月31日 下午5:45:25</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class CommonSqlBuilder {
	
	/**
	 * 
	 * 方法用途: 构建查询语句结尾sql，包含分页，排序等等<br>
	 * 操作步骤: TODO<br>
	 * @param currentRecord
	 * @param pageSize
	 * @param sort
	 * @param isDesc
	 * @return
	 */
	public static StringBuilder buildEndSearchSql(Integer currentRecord, Integer pageSize, String sort, Boolean isDesc) {
		StringBuilder type = new StringBuilder();
		if(!StringUtil.isBlank(sort)) {
			//不能用于SQL中的非法字符（主要用于排序字段名） 
			String[] ILLEGAL_CHARS_FOR_SQL = {",", ";", " ", "\"", "%"};
			// 排序字段不为空，过滤其中可能存在的非法字符
			sort = filterIllegalChars(sort, ILLEGAL_CHARS_FOR_SQL);
			
			String sortMethod = "desc";
			if(!isDesc) {
				sortMethod = "asc";
			}
			type.append(" order by ").append(sort).append(" ").append(sortMethod);
		}
		if(pageSize != null) {
			type.append(" limit ").append(currentRecord).append(",").append(pageSize);
		}
		return type;
	}
	
	/**
	 * 
	 * 方法用途: 从给定字符串中将指定的非法字符串数组中各字符串过滤掉。<br>
	 * 操作步骤: TODO<br>
	 * @param str 待过滤的字符串
	 * @param filterChars 指定的非法字符串数组
	 * @return 过滤后的字符串
	 */
	private static String filterIllegalChars(String str, String[] filterChars) {
		String rs = str;
		if (rs != null && filterChars != null) {
			for (String fc : filterChars) {
				if (fc != null && fc.length() > 0) {
					str = str.replaceAll(fc, ""); 
				}
			}
		}
		return rs;
	}
}
