package com.meizu.simplify.config.client.zookeeper.watch;

import java.util.concurrent.TimeUnit;	

public class ZookeeperNodeWatcherTest {
    public static void main(String[] args) {
		new ZookeeperNodeWatcher("/node10000000003", "key1").watch();
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
