package com.meizu.message.core;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface MqConsumerMethod {
	/**
	 * 方法用途: 数据实体entity,消息队列名称、交换机、路由默认为entity的类名<br>
	 * 操作步骤: <br>
	 * @return
	 */
	Class<?> target() default Object.class;//

	/**
	 * 三者关系与基本概念：
	 * queue（队列）是MQ的内部对象，用于存储消息
	 * exchange:生产者将消息Queue发送到Exchange（交换器），由Exchange将消息路由到一个或多个Queue中（或者丢弃）
	 * routing key:生产者在将消息发送给Exchange的时候，一般会指定一个routing key，来指定这个消息的路由规则
	 */
	String queue() default "";//队列名称
	String exchange() default "";//交换机
	String routingKey() default "";//路由key
}
