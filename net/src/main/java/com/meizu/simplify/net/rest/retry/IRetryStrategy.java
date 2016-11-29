package com.meizu.simplify.net.rest.retry;

import com.meizu.simplify.net.rest.RestCallBack;

/**
 * <p><b>Title:</b><i>重试策略</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月29日 下午5:45:35</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月29日 下午5:45:35</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IRetryStrategy {
    /**
     * 
     * 方法用途: TODO<br>
     * 操作步骤: TODO<br>
     * @param callback
     * @param times
     * @param secounds
     * @return
     */
    public <T> T retry(RestCallBack<T> callback, int times, int secounds);
}
