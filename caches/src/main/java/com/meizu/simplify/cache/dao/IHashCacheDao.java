package com.meizu.simplify.cache.dao;


import java.util.Map;

/**
 * <p><b>Title:</b><i>SET 操作集合</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午5:14:57</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午5:14:57</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IHashCacheDao {

    /**
     * 将哈希表key中的域field的值设为value。如果key不存在，一个新的哈希表被创建并进行hset操作。如果域field已经存在于哈希表中，旧值将被覆盖。
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean set(String key,  String field, Object value,int seconds);
    
    /**
     * 返回哈希表key中指定的field的值
     * @param key
     * @param field
     * @return
     */
    public Object get(String key,  String field);
    
    /**
     * 将哈希表key中的域field的值设置为value，当且仅当域field不存在。若域field已经存在，该操作无效。如果key不存在，一个新哈希表被创建并执行hsetnx命令。
     * @param key
     * @param field
     * @param value
     * @param seconds
     * @return
     */
    public boolean hsetnx(String key,  String field, Object value,int seconds);
    
    /**
     * 同时将多个field - value(域-值)对设置到哈希表key中。此命令会覆盖哈希表中已存在的域。如果key不存在，一个空哈希表被创建并执行hmset操作。
     * @param key
     * @param field
     * @param value
     * @param seconds
     * @return
     */
    public boolean hmset(String key, Map<String, Object> hash,int seconds);
   
    /**
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     * @param key
     * @param seconds
     * @param fields
     * @return
     */
    public long hdel(String key,int seconds,String ... fields);
    
    /**
     * 返回哈希表 key 中域的数量。
     * @param key
     * @return
     */
    public long hlen(String key);
    
    /**
     * 查看哈希表 key 中，给定域 field 是否存在。
     * @param key
     * @param field
     * @return
     */
    public boolean hexists(String key,String field);
    
}
