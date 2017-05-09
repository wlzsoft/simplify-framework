package vip.simplify.config.client.zookeeper.watch;

import java.util.concurrent.TimeUnit;	

public class ZookeeperNodeWatcherTest {
    public static void main(String[] args) {
		new ZookeeperNodeWatcher("/node10000000003","localpath","127.0.0.1:2181").watch(null);
		while(true) {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
