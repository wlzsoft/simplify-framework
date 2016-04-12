package com.meizu.simplify.dao.datasource;
/**
 * <p><b>Title:</b><i>动态数据源处理类</i></p>
 * <p>Desc: 用法：需要在具体的dao实现类中按情况调用DynamicDataSourceHolder.setMaster();或是setSlave(),或是setDataSourceName("master")
 *          注意：目前并在代码中使用，暂时没有这块需求，需要话，可以通过运维来做一主多从，或是使用第三方中间件来实现</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年4月28日 下午2:16:20</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年4月28日 下午2:16:20</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class DynamicDataSourceHolder {

    private static final ThreadLocal<String> holder = new ThreadLocal<String>();
    
    /**
	 * 
     * 方法用途: 绑定当前线程数据源路由的名称<br>
     * 操作步骤: 2.使用完成后必须调用setClear()方法删除<br>
     * @param name
     */
    public static void setDataSourceName(String name){
        holder.set(name);
    }
    
    /**
     * 
     * 方法用途: 获取当前线程的数据源的名称<br>
     * 操作步骤: TODO<br>
     * @return
     */
    public static String getDataSourceName(){
        return holder.get();
    }

	public static void setSlave() {  
		setDataSourceName("slave");  
    } 
	public static void setMaster() {  
		setDataSourceName("master");  
	} 
	
	/**
	 * 
	 * 方法用途: 删除当前线程数据源的名称<br>
	 * 操作步骤: TODO<br>
	 */
	public static void setClear() {  
		holder.remove();  
    } 
	
}