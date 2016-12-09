package com.meizu.simplify.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZookeeperConnectionWatcher implements Watcher {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperConnectionWatcher.class);
    private ZookeeperConnectionManager connectionManager = new ZookeeperConnectionManager();

    /* 
     * 连接成功会回调(并且是异步回调)
     * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
     */
    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            connectionManager.unlock();
            LOGGER.info("zookeeper已经连接成功");
        } else if (event.getState().equals(KeeperState.Disconnected)) {
            LOGGER.warn("zookeeper连接被断开，等待重连，如果一直未重连，那么手动重启应用");
        } else if (event.getState().equals(KeeperState.Expired)) {
            // zookeeper处于连接状态，但是会话过期，可以重连
            LOGGER.error("zookeeper会话已过期,正在重连");
            connectionManager.reconnect(this);
        } else if (event.getState().equals(KeeperState.AuthFailed)) {
            LOGGER.error("zookeeper授权失败(AuthFailed)");
        }
    }

}
