package com.meizu.simplify.config.client.zookeeper;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.utils.StringUtil;

public class ZookeeperTreeInfoPrint  {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperTreeInfoPrint.class);

    public void tree(ZookeeperConnectionManager connectionManger,String path,StringBuffer sb) throws KeeperException, InterruptedException {
        try {
            if (path != "/") {
//            	sb.append("\t");
            	int pathLength = StringUtil.split(path+"TODO", "/").length;
            	for (int i = 0; i < pathLength - 1; ++i) {
            		sb.append("\t");
            	}
                String node = StringUtil.substringAfterLast(path, "/");
                sb.append("|-" + node);
                Stat stat = new Stat();
                byte[] data = connectionManger.getZookeeper().getData(path, null, stat);

                if (data != null) {
                    sb.append("\t" + new String(data, Charset.forName("UTF-8")));
                }
                if (stat != null) {
                    sb.append("\t" + stat.getEphemeralOwner());
                }
            } else {
                sb.append(path);
            }
            sb.append("\n");
//            System.out.print(sb.toString());
            //继续递归下个节点数据
            List<String> children = connectionManger.getZookeeper().getChildren(path, false);
            for (String child : children) {
                if (path != "/") {
                    tree(connectionManger,path + "/" + child,sb);
                } else {
                    tree(connectionManger,path + child,sb);
                }
            }

        } catch (KeeperException.NoNodeException e) {
            LOGGER.info("节点 %s 不存在\n", path);
        }
    }

    public static void main(String[] args) throws Exception {
    	args = new String[]{"127.0.0.1:2181"};
    	System.out.println( StringUtil.split("a/b/c/d"+"TODO", "/").length); 
    	System.out.println(StringUtil.substringAfterLast("a/b/c", "/"));
        ZookeeperConnectionManager connectionManger = new ZookeeperConnectionManager();
        ZookeeperTreeInfoPrint printZookeeperTree = new ZookeeperTreeInfoPrint();
        connectionManger.connect(args[0],new ZookeeperConnectionWatcher());
        Thread.sleep(2000);
        System.out.println("==================");
        StringBuffer sb = new StringBuffer();
        printZookeeperTree.tree(connectionManger,"/",sb);
        System.out.println(sb.toString());
        System.out.println("==================");
        connectionManger.close();
    }
}
