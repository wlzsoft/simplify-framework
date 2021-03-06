package vip.simplify.zookeeper;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.utils.StringUtil;

public class ZookeeperNodeTreeInfoPrint  {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperNodeTreeInfoPrint.class);

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
}
