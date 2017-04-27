package vip.simplify.config.client.zookeeper.watch;

import java.io.IOException;
import java.net.SocketException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.config.ConfigAnnotationResolver;
import vip.simplify.config.api.entity.ConfigAppEntity;
import vip.simplify.config.client.ConfigClientAnnotationResolver;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.utils.PropertieUtil;
import vip.simplify.utils.ip.IpUtil;
import vip.simplify.zookeeper.ZookeeperConnectionManager;
import vip.simplify.zookeeper.ZookeeperConnectionWatcher;
import vip.simplify.zookeeper.ZookeeperExecute;

public class ZookeeperNodeWatcher implements Watcher {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperNodeWatcher.class);
    private ZookeeperExecute execute;
    private ZookeeperConnectionManager connectionManager;
    private String watchPath = "";
    /**
     * 本地的配置文件路径
     */
    private String configPath;
    ZookeeperConnectionWatcher watcher = new ZookeeperConnectionWatcher();
    public ZookeeperNodeWatcher(String watchPath,String configPath) {
    	this.configPath = configPath;
        connectionManager = new ZookeeperConnectionManager();
        try {
			connectionManager.connect("127.0.0.1:2181", watcher);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
        execute = new ZookeeperExecute(connectionManager);
        this.watchPath = watchPath;
    }
    public void watch(String nodeValue) {
        Stat stat = new Stat();
        try {
        	String value = execute.getData(watchPath, this, stat);
        	if(nodeValue == null) {
        		nodeValue = value;
        	}
            try {
				execute.createEphemeralNode(watchPath+"/"+ IpUtil.getLocalIp()+":8080", nodeValue);
			} catch (SocketException e) {
				e.printStackTrace();
			}
        } catch (InterruptedException e) {
            LOGGER.info(e.toString());
        } catch (KeeperException e) {
            LOGGER.error("监听节点失败 " + watchPath, e);
        }
        LOGGER.debug("监听节点: 成功添加==>>(" + watchPath +  ","  + ")");
    }

    @Override
    public void process(WatchedEvent event) {

        // 结点数据被更新时
        if (event.getType() == EventType.NodeDataChanged) {
        	ConfigClientAnnotationResolver resolver = BeanFactory.getBean(ConfigClientAnnotationResolver.class);
        	ConfigAppEntity app = resolver.loadConfigMeta();
        	PropertieUtil propertieUtil = ConfigAnnotationResolver.propertiesMap.get(configPath);
        	resolver.updateConfig(app, configPath,propertieUtil);
        	LOGGER.info("监听连接节点数据被更新 " + event.toString() + ": (" + watchPath +  "," +  ")");
        }

        // zookeeper客户端断开连接，这时不要进行处理
        if (event.getState() == KeeperState.Disconnected) {
        	LOGGER.warn("节点连接已经断开: " + event.toString() + ": (" + watchPath +  ","  + ")");
        }
        
        if (event.getState() == KeeperState.Expired) {//会话超时，需要重新激活
            LOGGER.error("会话超时：  " + event.toString() + ": (" + watchPath  + ","  + ")");
            // 重新连接
            connectionManager.reconnect(watcher);
        }
    }
    
}
