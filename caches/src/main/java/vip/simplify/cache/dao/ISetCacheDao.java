package vip.simplify.cache.dao;

import java.util.Set;

/**
 * <p><b>Title:</b><i>SET 操作集合</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午5:15:41</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午5:15:41</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface ISetCacheDao {

    /**
     * 增加元素
     *
     * @param key
     * @param value
     * @return
     */
    public boolean sadd(String key, Object value);
    
    /**
     * 增加元素
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public boolean sadd(String key, Object value,int seconds);

    /**
     * 删除某个元素
     *
     * @param key
     * @param member
     * @return
     */
    public boolean srem(String key, Object member);

    /**
     * 获取整个集合
     *
     * @param key
     * @return
     */
    public Set<Object> smembers(String key);
    
    /**
     * 元素是否存在于集合内
     *
     * @param key
     * @param member
     * @return
     */
    public boolean sismember(String key, Object member);

    /**
     * 集合大小
     *
     * @param key
     * @return
     */
    public long scard(String key);
    
    /**
     * 移除并返回集合中的一个随机元素。
     *
     * @param key
     * @return
     */
    public Object spop(String key);

    /**
     * 返回集合中的一个随机元素。
     *
     * @param key
     * @return
     */
    public Object srandmember(String key);
    
}
