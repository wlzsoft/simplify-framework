package vip.simplify.message.core;

import java.lang.reflect.Method;
import java.util.List;

import vip.simplify.message.conn.MqConnection;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.resolver.BeanAnnotationResolver;
import vip.simplify.utils.ClassUtil;
import vip.simplify.utils.CollectionUtil;
import com.rabbitmq.client.Channel;

/**
 * <p>消息监听容器</p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月30日 上午10:27:12</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月30日 上午10:27:12</p>
 * @author <a href="mailto:wanghaibin@meizu.com" >wanghb</a>
 * @version Version 3.0
 *
 */
@Bean
public class MessageListennerContainer {

	public MessageListennerContainer() {
		try {
			List<Class<?>> allIpmlClass = ClassUtil.findClassesByParentClass(MqConsumerListenner.class, BeanAnnotationResolver.getClasspaths());
			if (CollectionUtil.isEmpty(allIpmlClass)) {
				return;
			}
			Channel channel = MqConnection.getChannel();
			for (Class<?> implClass : allIpmlClass) {
				Method[] methodArr = null;
				methodArr = implClass.getDeclaredMethods();
				for (Method method : methodArr) {
					if (method.isAnnotationPresent(MqConsumerMethod.class)) {
						MqConsumerMethod mqConsumerMethod = method.getAnnotation(MqConsumerMethod.class);
						Class<?> calss=mqConsumerMethod.target();
						String exchange=mqConsumerMethod.exchange();
						String routingKey=mqConsumerMethod.routingKey();
						String queueName=mqConsumerMethod.queue();
						QueueConsumer consumer = new QueueConsumer(channel, calss, implClass, method.getName(), exchange, routingKey, queueName);
						Thread consumerThread = new Thread(consumer);
						consumerThread.start();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
