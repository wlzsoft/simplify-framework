package com.meizu.message.remoting.server;

import com.meizu.message.core.MqConsumerListenner;
import com.meizu.message.core.MqConsumerMethod;


public class MyConsumer implements MqConsumerListenner {

	@MqConsumerMethod(target = TestEntity.class)
	public void test(String message) {
		System.out.println("ttttttttttttt" + message);
	}

	
}
