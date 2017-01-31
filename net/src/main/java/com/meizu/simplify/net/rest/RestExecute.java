package com.meizu.simplify.net.rest;

import java.net.URL;

import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.net.rest.retry.IRetryStrategy;

/**
 * <p><b>Title:</b><i>rest执行器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月29日 下午6:03:46</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月29日 下午6:03:46</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class RestExecute {

	@Resource
    private IRetryStrategy retryStrategy;

    /**
     * 
     * 方法用途: 请求获取数据<br>
     * 操作步骤: TODO<br>
     * @param clazz
     * @param remoteUrl
     * @param retryTimes
     * @param retrySleepSeconds
     * @return
     */
    public <T> T get(Class<T> clazz, RestAddressInfo remoteUrl, int retryTimes, int retrySleepSeconds) {
        for (URL url : remoteUrl.getUrls()) {
        	RestCallBack<T> callback = new RestCallBack<T>(clazz, url);
                return retryStrategy.retry(callback, retryTimes, retrySleepSeconds);
        }
		return null;
    }


}
