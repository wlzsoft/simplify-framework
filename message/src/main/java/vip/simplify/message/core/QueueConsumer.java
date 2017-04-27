package vip.simplify.message.core;

import java.io.IOException;
import java.lang.reflect.Method;

import vip.simplify.utils.StringUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 
 * <p> 消息消费处理类</p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月26日 上午10:26:24</p>
 * <p>Modified By:meizu-</p>
 * @author <a href="mailto:wanghaibin@meizu.com" >wanghb</a>
 * @version Version 3.0
 *
 */
public class QueueConsumer implements Runnable, Consumer {

	private Class<?> calss;//消息队列
	private Class<?> implClass;//当前类
	private String implClassMethod;
	private String queueName;
	private String exchange;
	private String routingKey;
	private Channel channel;

	public QueueConsumer(Channel channel,Class<?> calss,Class<?> implClass, String method, String exchange, String routingKey, String queueName) throws IOException {
		this.channel=channel;
		this.calss=calss;
		this.implClass = implClass;
		this.implClassMethod = method;
		this.exchange = exchange;
		this.routingKey = routingKey;
		this.queueName = queueName;
	}

	public void run() {
		try {
			boolean isClass=false;
			String implClassName=implClass.getSimpleName();
			String className=calss.getSimpleName();
			if(!calss.getSimpleName().equals("Object")){
				isClass=true;
			}
			if (StringUtil.isBlank(exchange)) {
				this.exchange = implClassName;
				if(isClass){
					this.exchange = className;
				}
			}
			if (StringUtil.isBlank(routingKey)) {
				this.routingKey = implClassName;
				if(isClass){
					this.routingKey = className;
				}
			}
			if (StringUtil.isBlank(queueName)) {
				this.queueName = implClassName;
				if(isClass){
					this.queueName = className;
				}
			}
			channel.exchangeDeclare(exchange, "direct", true);
			channel.queueDeclare(queueName, true, false, false, null);
			channel.queueBind(queueName, exchange, routingKey);
			channel.basicConsume(queueName, true, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleConsumeOk(String consumerTag) {
		System.out.println("启动监听" + consumerTag + " registered");
	}

	public void handleCancel(String consumerTag) {
	}

	public void handleCancelOk(String consumerTag) {
	}

	public void handleRecoverOk(String consumerTag) {
	}

	public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1) {
	}

	public void handleDelivery(String arg0, Envelope arg1, com.rabbitmq.client.AMQP.BasicProperties arg2, byte[] body) throws IOException {
		String str = new String(body);
		try {
			Class<?> clazz = Class.forName(implClass.getName());
			Object bObj = clazz.newInstance();
			Method bMethod = clazz.getDeclaredMethod(this.implClassMethod, new Class[] { String.class });
			bMethod.invoke(bObj, str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Class<?> getCalss() {
		return calss;
	}

	public void setCalss(Class<?> calss) {
		this.calss = calss;
	}

	public Class<?> getImplClass() {
		return implClass;
	}

	public void setImplClass(Class<?> implClass) {
		this.implClass = implClass;
	}

	public String getImplClassMethod() {
		return implClassMethod;
	}

	public void setImplClassMethod(String implClassMethod) {
		this.implClassMethod = implClassMethod;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}