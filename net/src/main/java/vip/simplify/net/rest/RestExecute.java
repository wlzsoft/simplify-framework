package vip.simplify.net.rest;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.net.rest.retry.IRetryStrategy;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(RestExecute.class);
	
	@Inject
    private IRetryStrategy retryStrategy;

    /**
     * 
     * 方法用途: 请求获取数据<br>
     * 操作步骤: TODO<br>
     * @param clazz
     * @param remoteUrl 请求地址
     * @param retryTimes 重试次数
     * @param retrySleepSeconds 等待时间多长时间后重试，单位是秒
     * @return
     */
    public <T> T get(Class<T> clazz, RestAddressInfo remoteUrl, int retryTimes, int retrySleepSeconds) {
        for (URL url : remoteUrl.getUrls()) {
        	LOGGER.info("远程请求地址："+url.toString());
        	RestCallBack<T> callback = new RestCallBack<T>(clazz, url);
            return retryStrategy.retry(callback, retryTimes, retrySleepSeconds);
        }
		return null;
    }


}
