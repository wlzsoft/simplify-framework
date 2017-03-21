package com.meizu.message.remoting.server;

import com.meizu.message.core.MqConsumerListenner;
import com.meizu.message.core.MqConsumerMethod;



public class MyConsumer implements MqConsumerListenner {

	/**
	 * 方法用途:  数据实体entity<br>
	 * 操作步骤: queue消息队列名称、exchange交换机、routingKey消息路由key默认为entity的类名<br>
	 * @param message
	 */
	@MqConsumerMethod(target = TestEntity.class,routingKey="t1")
	public void test(String message) {
		System.out.println("tt1" + message);
	}
	@MqConsumerMethod(target = TestEntity.class,routingKey="t2")
	public void test5(String message) {
		System.out.println("tt2" + message);
	}

	@MqConsumerMethod(queue="TestEntity",exchange="TestEntity",routingKey="TestEntity")
	public void test1(String message) {
		System.out.println("22222222222" + message);
	}
	/**
	 * 方法用途: <br>
	 * 操作步骤: 注解参数为空时，queue、exchange、routingKey默认值为当前类名（MyConsumer）<br>
	 * @param message
	 */
	@MqConsumerMethod
	public void test2(String message) {
		System.out.println("33333333333" + message);
	}
}
