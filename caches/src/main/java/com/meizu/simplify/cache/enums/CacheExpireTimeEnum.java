package com.meizu.simplify.cache.enums;

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
public enum CacheExpireTimeEnum {
	
	/**
	 * 缓存时效 5分钟钟
	 */
	CACHE_EXP_SENDCONDs(60*5),
	
	/** 
	 * 缓存时效 30分钟 
	 */  
	CACHE_EXP_MINUTES(7200),
	
	/** 
     * 缓存时效 1小时 
     */  
    CACHE_EXP_HOUR(3600 * 1),
    
	/** 
     * 缓存时效 1天 
     */  
    CACHE_EXP_DAY(3600 * 24), 
  
    /** 
     * 缓存时效 1周 
     */  
    CACHE_EXP_WEEK(3600 * 24 * 7), 
  
    /** 
     * 缓存时效 1月 
     */  
    CACHE_EXP_MONTH(3600 * 24 * 30), 
  
    /** 
     * 缓存时效 永久 
     */  
    CACHE_EXP_FOREVER(0), 
  
    /** 
     * 冲突延时 1秒 
     */  
    MUTEX_EXP(1);
	
	CacheExpireTimeEnum(int timesanmp) {
		this.timesanmp = timesanmp;
	}
	
	private final int timesanmp;
	
	/**
	 * 
	 * 方法用途: 获取枚举对应时间秒数<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public int timesanmp(){
		return timesanmp;
	}
  
}

