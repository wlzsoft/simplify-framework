package com.meizu.message.core;


import com.alibaba.fastjson.JSON;
import com.meizu.message.conn.MqConnection;
import com.meizu.simplify.ioc.annotation.Bean;
import com.rabbitmq.client.Channel;

@Bean
public class RabbitTemplate implements SendMessageTemplate {
	/**
	 * 基本概念：
	 * Queue（队列）是RabbitMQ的内部对象，用于存储消息
	 * Exchange:生产者将消息Queue发送到Exchange（交换器），由Exchange将消息路由到一个或多个Queue中（或者丢弃）
	 * routing key:生产者在将消息发送给Exchange的时候，一般会指定一个routing key，来指定这个消息的路由规则
	 */
	private  String queueName="DEFAULT_QUEUE";
	private  String exchange="DEFAULT_EXCHANGE";
	private  String routingKey="DEFAULT_ROUTINGKEY";
	
	
	protected <T> void doSend(Channel channel, String queueName,String exchange, String routingKey,T t) throws Exception {
		if (exchange == null) {
			exchange = this.exchange;
		}
		if (routingKey == null) {
			routingKey = this.routingKey;
		}
		if(queueName==null){
			queueName = this.queueName;
		}
		channel.exchangeDeclare(exchange, "direct", true);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchange, routingKey);
		channel.basicPublish(exchange, routingKey, null,JSON.toJSONBytes(t));
	}
	

	@Override
	public <T> void send(T t) {
		this.queueName = t.getClass().getSimpleName();
		this.exchange = t.getClass().getSimpleName();
		this.routingKey = t.getClass().getSimpleName();
		send(queueName, exchange, this.routingKey, t);
	}


	@Override
	public <T> void send(String routingKey, T t) {
		this.queueName = t.getClass().getSimpleName();
		this.routingKey = routingKey;
		this.exchange = t.getClass().getSimpleName();
		send(queueName, exchange,routingKey, t);
	}


	@Override
	public <T> void send(String queueName, String exchange, String routingKey, T t) {
		try {
			this.doSend(MqConnection.getChannel(),queueName,exchange, routingKey, t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
