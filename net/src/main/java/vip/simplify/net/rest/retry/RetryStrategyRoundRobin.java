package vip.simplify.net.rest.retry;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.net.rest.RestCallBack;

/**
 * <p><b>Title:</b><i>重试策略-轮循</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月29日 下午5:45:16</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月29日 下午5:45:16</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class RetryStrategyRoundRobin implements IRetryStrategy{

    protected static final Logger LOGGER = LoggerFactory.getLogger(RetryStrategyRoundRobin.class);
    public <T> T retry(RestCallBack<T> callback, int retryTimes, int retrySleepSeconds)  {
        for (int i = 0; i < retryTimes; i++) {
            try {
                return callback.call();
            } catch (Exception e) {
                LOGGER.warn("第 " + i + "次重试失败" + e.toString());
                try {
                    TimeUnit.SECONDS.sleep(retrySleepSeconds);
                } catch (InterruptedException e1) {
                	e1.printStackTrace();
                }
            }
        }
		return null;
    }
}
