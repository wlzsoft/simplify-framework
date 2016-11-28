package com.meizu.simplify.config.client.zookeeper.watch;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

import com.meizu.simplify.config.client.zookeeper.ZookeeperConnectionManager;
import com.meizu.simplify.config.client.zookeeper.ZookeeperConnectionWatcher;
import com.meizu.simplify.config.client.zookeeper.ZookeeperExecute;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月28日 下午5:27:59</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月28日 下午5:27:59</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class ZookeeperConfigWatcher implements Watcher {

    private ZookeeperConnectionManager connectionManager;
    
    private ZookeeperExecute execute;

    public ZookeeperConfigWatcher(String hosts) throws IOException, InterruptedException {
    	connectionManager = new ZookeeperConnectionManager();
        connectionManager.connect(hosts,new ZookeeperConnectionWatcher());
    }

    @Override
    public void process(WatchedEvent event) {

        if (event.getType() == EventType.NodeDataChanged) {
            try {
            	 String value = execute.getData("path", this, null);
                 System.out.printf("成功加载配置信息 %s as %s\n", "path", value);
            } catch (InterruptedException e) {
                System.err.println("线程中断退出");
                Thread.currentThread().interrupt();
            } catch (KeeperException e) {
                System.err.printf("配置加载异常: %s. Exiting.\n", e);
            }
        }

    }
}
