package com.meizu.simplify.zookeeper;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.utils.StringUtil;

/**
 * <p><b>Title:</b><i>Zookeeper执行器</i></p>
 * <p>Desc: 执行zookeeper相关的命令</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月28日 下午5:21:19</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月28日 下午5:21:19</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class ZookeeperExecute  {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperExecute.class);

    private ZookeeperConnectionManager connectionManger;
    public ZookeeperExecute(ZookeeperConnectionManager connectionManger) {
    	this.connectionManger = connectionManger;
    }
    
    /**
     * 
     * 方法用途: 创建一个结点，如果节点存在不做任何处理<br>
     * 操作步骤: TODO<br>
     * @param dir
     * @param data
     */
    public void writeIfAbsent(String dir, String data) {

        try {
            boolean isExists = exists(dir);
            if (!isExists) {
                write(dir, data);
            }
        } catch (KeeperException e) {
            LOGGER.error("节点创建失败: " + dir, e);
        } catch (Exception e) {
            LOGGER.error("节点创建失败: " + dir, e);
        }
    }
    
    /**
     * 
     * 方法用途: 持久化写入节点数据，如果节点存在，那么更新节点数据<br>
     * 操作步骤: 非递归创建，如果创建节点的父节点不存储，会报异常，提示节点不存在，无法创建<br>
     * @param path
     * @param value
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void writeRecursion(String path, String value) throws InterruptedException, KeeperException {
    	String[] pathNodeArr = path.split("/");
    	String pathNode = "";
    	for (int i = 0; i < pathNodeArr.length; i++) {
    		if(StringUtil.isBlank(pathNodeArr[i])) {
    			continue;
    		}
			pathNode += "/"+pathNodeArr[i];
			Stat stat = connectionManger.getZookeeper().exists(pathNode, false);
			if(stat == null) {
				if(i < pathNodeArr.length-1) {
					write(pathNode,"");
					continue;
				}
				write(pathNode,value);
			}
		}
    }
    
    /**
     * 
     * 方法用途: 持久化写入节点数据，如果节点存在，那么更新节点数据<br>
     * 操作步骤: TODO<br>
     * @param path
     * @param value
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void write(String path, String value) throws InterruptedException, KeeperException {

        int retries = 0;
        while (true) {
            try {
                Stat stat = connectionManger.getZookeeper().exists(path, false);
                if (stat == null) {
                	connectionManger.getZookeeper().create(path, value.getBytes(Charset.forName("UTF-8")), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                } else {
                	connectionManger.getZookeeper().setData(path, value.getBytes(Charset.forName("UTF-8")), stat.getVersion());
                }
                break;
            } catch (KeeperException.SessionExpiredException e) {
                throw e;
            } catch (KeeperException e) {
                LOGGER.warn("write方法执行：连接丢失重试第 " + retries + "次\t" + e.toString());
                if (retries++ == ZookeeperConnectionManager.MAX_RETRIES) {
                    throw e;
                }
                int sec = ZookeeperConnectionManager.RETRY_PERIOD_SECONDS * retries;
                LOGGER.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }
    }

    /**
     * 
     * 方法用途: 创建一个临时结点，如果节点存在那么更新节点数据<br>
     * 操作步骤: TODO<br>
     * @param path
     * @param value
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    public String createEphemeralNode(String path, String value)
        throws InterruptedException, KeeperException {

        int retries = 0;
        while (true) {
            try {
                Stat stat = connectionManger.getZookeeper().exists(path, false);
                if (stat == null) {
                    return connectionManger.getZookeeper().create(path, value.getBytes(Charset.forName("UTF-8")), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } else {
                    if (value != null) {
                    	connectionManger.getZookeeper().setData(path, value.getBytes(Charset.forName("UTF-8")), stat.getVersion());
                    }
                }
                return path;
            } catch (KeeperException.SessionExpiredException e) {
                throw e;
            } catch (KeeperException e) {
                LOGGER.warn("createEphemeralNode方法执行：连接丢失重试第 " + retries + "次\t" + e.toString());
                if (retries++ == ZookeeperConnectionManager.MAX_RETRIES) {
                    throw e;
                }
                int sec = ZookeeperConnectionManager.RETRY_PERIOD_SECONDS * retries;
                LOGGER.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }
    }

    public boolean exists(String path) throws InterruptedException, KeeperException {

        int retries = 0;
        while (true) {
            try {
                Stat stat = connectionManger.getZookeeper().exists(path, false);
                if (stat == null) {
                    return false;
                } else {
                    return true;
                }
            } catch (KeeperException.SessionExpiredException e) {
                throw e;
            } catch (KeeperException e) {
                LOGGER.warn("exists方法执行：连接丢失重试第 " + retries + "次\t" + e.toString());
                if (retries++ == ZookeeperConnectionManager.MAX_RETRIES) {
                    LOGGER.error("达到最大重连次数"+retries+",连接失败");
                    throw e;
                }
                int sec = ZookeeperConnectionManager.RETRY_PERIOD_SECONDS * retries;
                LOGGER.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }
    }

    /**
     * 
     * 方法用途: 获取某路径节点数据<br>
     * 操作步骤: TODO<br>
     * @param path
     * @param watcher 监听器
     * @param stat 统计信息对象
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    public String getData(String path, Watcher watcher, Stat stat) throws InterruptedException, KeeperException {

        byte[] data = connectionManger.getZookeeper().getData(path, watcher, stat);
        return new String(data, Charset.forName("UTF-8"));
    }
    
    /**
     * 
     * 方法用途: 获取某路径节点数据<br>
     * 操作步骤: TODO<br>
     * @param path
     * @param isWatcher 是否开启监控
     * @param stat 统计信息对象
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    public String getData(String path, boolean isWatcher, Stat stat) {

		try {
			 byte[] data = connectionManger.getZookeeper().getData(path, isWatcher, stat);
			 return new String(data, Charset.forName("UTF-8"));
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
    }

    public List<String> getChildren() {

        List<String> children = new ArrayList<String>();
        try {
            children = connectionManger.getZookeeper().getChildren("/", false);
        } catch (KeeperException e) {
            LOGGER.error(e.toString());
        } catch (InterruptedException e) {
            LOGGER.error(e.toString());
        }
        return children;
    }

    public void delete(String path) {
        try {
        	connectionManger.getZookeeper().delete(path, -1);
        } catch (KeeperException.NoNodeException e) {
            LOGGER.error("删除路径节点失败: " + path, e);
        } catch (InterruptedException e) {
            LOGGER.warn(e.toString());
        } catch (KeeperException e) {
            LOGGER.error("删除路径节点失败 " + path, e);
        }
    }

}
