package vip.simplify.zookeeper;

import vip.simplify.utils.StringUtil;

public class ZookeeperNodeTreeInfoPrintTest  {

    public static void main(String[] args) throws Exception {
    	args = new String[]{"127.0.0.1:2181"};
    	System.out.println( StringUtil.split("a/b/c/d"+"TODO", "/").length);
    	System.out.println(StringUtil.substringAfterLast("a/b/c", "/"));
        ZookeeperConnectionManager connectionManger = new ZookeeperConnectionManager();
        ZookeeperNodeTreeInfoPrint printZookeeperTree = new ZookeeperNodeTreeInfoPrint();
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
