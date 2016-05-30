package com.meizu.message.core;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface MqConsumerMethod {
	Class<?> target() default Object.class;

	String queue() default "";

	String exchange() default "";

	String routingKey() default "";
}
