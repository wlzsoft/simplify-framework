package vip.simplify.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p><b>Title:</b><i>zookeeper连接管理器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月28日 下午5:12:12</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月28日 下午5:12:12</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class ZookeeperConnectionManager {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperConnectionManager.class);

    private ZooKeeper zooKeeper;
    private CountDownLatch connectionCountDownLatch = new CountDownLatch(1);
    private String hosts = "";
    // 最大重试次数
    public static final int MAX_RETRIES = 3;
    // 每次重试超时时间
    public static final int RETRY_PERIOD_SECONDS = 2;
    public void connect(String hosts,Watcher watcher) throws IOException, InterruptedException {
        this.hosts = hosts;
        //连接成功后10000毫秒zookeeper的session会话有效时间
        zooKeeper = new ZooKeeper(hosts, 10_000, watcher);
        //3分钟之后，如果还未连接解锁，说明还未连接成功，那么一直等待带3秒后，超时连接失败,未解锁或是未超时之前，一直是等待状态
        connectionCountDownLatch.await(3000, TimeUnit.MILLISECONDS);
        LOGGER.info("zookeeper服务[" + hosts + "]已经连接成功");
    }

    /**
     * 
     * 方法用途: 重新连接<br>
     * 操作步骤: 重连失败情况，会一直重试，重试时间间隔可以设置，目前是固定数值<br>
     * @param watcher
     */
    public synchronized void reconnect(Watcher watcher) {

        while (true) {
        	if (!zooKeeper.getState().equals(States.CLOSED)) {
        		LOGGER.warn("zookeeper连接非关闭状态，无需重新连接");
                 break;
            }
            LOGGER.warn("zookeeper连接已丢失,再次启用连接");
            try {
				connect(hosts,watcher);
			} catch (IOException | InterruptedException e) {
				LOGGER.error("连接失败",e);
                try {
                    LOGGER.warn(RETRY_PERIOD_SECONDS+"秒后重试");
                    TimeUnit.SECONDS.sleep(RETRY_PERIOD_SECONDS);
                } catch (InterruptedException e1) {
                	e1.printStackTrace();
                }
			}
        }
    }

    /**
     * 
     * 方法用途: 应用程序必须调用它来释放zookeeper资源<br>
     * 操作步骤: TODO<br>
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        zooKeeper.close();
    }

    public ZooKeeper getZookeeper() {
        return zooKeeper;
    }

	/**
	 * 方法用途: 解锁连接，连接成功后解锁<br>
	 * 操作步骤: TODO<br>
	 */
	public void unlock() {
		connectionCountDownLatch.countDown();
	}
}
