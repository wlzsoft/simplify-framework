package com.meizu.simplify.config.client.zookeeper.watch;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.config.client.zookeeper.ZookeeperConnectionManager;
import com.meizu.simplify.config.client.zookeeper.ZookeeperConnectionWatcher;
import com.meizu.simplify.config.client.zookeeper.ZookeeperExecute;	

public class ZookeeperNodeWatcher implements Watcher {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperNodeWatcher.class);
    ZookeeperExecute execute;
    ZookeeperConnectionManager connectionManager;
    private String monitorPath = "";
    private String keyName = "";
    ZookeeperConnectionWatcher watcher = new ZookeeperConnectionWatcher();
    public ZookeeperNodeWatcher( String monitorPath, String keyName) {
        super();
        connectionManager = new ZookeeperConnectionManager();
        try {
			connectionManager.connect("127.0.0.1:2181", watcher);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
        execute = new ZookeeperExecute(connectionManager);
        this.monitorPath = monitorPath;
        this.keyName = keyName;
    }
    public void monitorMaster() {
        Stat stat = new Stat();
        try {
            execute.getData(monitorPath, this, stat);
        } catch (InterruptedException e) {
            LOGGER.info(e.toString());
        } catch (KeeperException e) {
            LOGGER.error("监听节点失败 " + monitorPath, e);
        }
        LOGGER.debug("监听节点: 成功添加==>>(" + monitorPath + "," + keyName + ","  + ")");
    }

    @Override
    public void process(WatchedEvent event) {

        // 结点更新时
        if (event.getType() == EventType.NodeDataChanged) {
            try {
                LOGGER.info("============连接节点数据被更新 " + event.toString() + ": (" + monitorPath + "," + keyName+ ")======================");
            } catch (Exception e) {
                LOGGER.error("监听节点失败异常. " + monitorPath, e);
            }
        }

        // 结点断开连接，这时不要进行处理
        if (event.getState() == KeeperState.Disconnected) {
           LOGGER.warn("============连接节点已经断开: " + event.toString() + ": (" + monitorPath + "," + keyName + ","  + ")======================");
        }
        
        if (event.getState() == KeeperState.Expired) {//会话超时，需要重新激活
            LOGGER.error("============会话超时：  " + event.toString() + ": (" + monitorPath + "," + keyName + ","  + ")======================");
            // 重新连接
            connectionManager.reconnect(watcher);
        }
    }
    
    public static void main(String[] args) {
		new ZookeeperNodeWatcher("/node10000000003", "key1").monitorMaster();
		while(true) {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
