package com.meizu.cache;

/**
 * 
 * <p><b>Title:</b><i>缓存时间常量</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月12日 下午4:35:00</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月12日 下午4:35:00</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class Constants {
	/**
	 * 缓存时效 5分钟钟
	 */
	public static final int CACHE_EXP_SENDCONDs = 60*5; 
	/** 
	 * 缓存时效 30分钟 
	 */  
	public static final int CACHE_EXP_MINUTES = 7200; 
	/** 
     * 缓存时效 1小时 
     */  
    public static final int CACHE_EXP_HOUR = 3600 * 1; 
	/** 
     * 缓存时效 1天 
     */  
    public static final int CACHE_EXP_DAY = 3600 * 24;  
  
    /** 
     * 缓存时效 1周 
     */  
    public static final int CACHE_EXP_WEEK = 3600 * 24 * 7;  
  
    /** 
     * 缓存时效 1月 
     */  
    public static final int CACHE_EXP_MONTH = 3600 * 24 * 30;  
  
    /** 
     * 缓存时效 永久 
     */  
    public static final int CACHE_EXP_FOREVER = 0;  
  
    /** 
     * 冲突延时 1秒 
     */  
    public static final int MUTEX_EXP = 1;  
    /** 
     * 冲突键 
     */  
    public static final String MUTEX_KEY_PREFIX = "MUTEX_";  
  
    

 	// 生成主键KEY后缀
 	public static final String _INDEX_UUID = ":index_UUID";

 	// 主键生成REDIS库地址
 	public static final String REDIS_INDEX_MOD = "redis_index_hosts";

 	
 	public static final int CACHE_LIST_TIME = 60 * 60;
 	public static final int CACHE_PAGE_TIME = 10 * 60;
 	public static final int CACHE_TIME = 60 * 60;
 	public static final int LONGSIZE = 8;
  
}

