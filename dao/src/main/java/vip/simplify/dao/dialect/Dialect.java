package vip.simplify.dao.dialect;

/**
 * 
 * <p><b>Title:</b><i>数据库方言基础实现类，此类不支持分页处理</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午5:31:35</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午5:31:35</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class Dialect implements IDialect {

	@Override
	public boolean supportsLimit(){
    	return false;
    }

	@Override
    public boolean supportsLimitOffset() {
    	return supportsLimit();
    }
    
	@Override
    public String getLimitString(String sql, int offset, int limit) {
    	return getLimitString(sql,offset,Integer.toString(offset),limit,Integer.toString(limit));
    }
    
    /**
     * 
     * 方法用途: 将sql变成分页sql语句,提供将offset及limit使用占位符(placeholder)替换<br>
     * 操作步骤: 需要由子类重写此方法实现sql语句分页处理拼装以及占位符替换
     * <pre>
     * 如mysql
     * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
     * select * from user limit :offset,:limit
     * </pre><br>
     * @param sql
     * @param offset
     * @param offsetPlaceholder
     * @param limit
     * @param limitPlaceholder
     * @return 包含占位符的分页sql
     */
    protected String getLimitString(String sql, int offset,String offsetPlaceholder, int limit,String limitPlaceholder) {
    	throw new UnsupportedOperationException("paged queries not supported");
    } 

}
