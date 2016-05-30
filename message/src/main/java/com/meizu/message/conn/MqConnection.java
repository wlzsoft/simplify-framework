package com.meizu.message.conn;


import com.meizu.simplify.utils.PropertieUtil;
import com.meizu.simplify.utils.StringUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MqConnection {
	
	public static Connection conn;
	
	private MqConnection(){
		
	}
	
	public static Connection getConnection(){
		PropertieUtil propertieUtil = new PropertieUtil("properties/rabbitmq.properties");
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(propertieUtil.getString("rabbitmq.host"));
		factory.setPort(propertieUtil.getInteger("rabbitmq.port"));
		factory.setUsername(propertieUtil.getString("rabbitmq.username"));
		factory.setPassword(propertieUtil.getString("rabbitmq.password"));
		String vhost=propertieUtil.getString("rabbitmq.virtualHost");
		if (StringUtil.isNotBlank(vhost)) {
			factory.setVirtualHost(vhost);
		} else {
			factory.setVirtualHost("/");
		}
		try {
//			ExecutorService es = Executors.newFixedThreadPool(20);//线程池
//			conn=factory.newConnection(es);
			conn=factory.newConnection();
			return conn;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}

	public static Connection getConnectionUrl() {
		try {
			PropertieUtil propertieUtil = new PropertieUtil("properties/rabbitmq.properties");
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri(propertieUtil.getString("rabbitmq.url"));
			conn=factory.newConnection();
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static synchronized Channel getChannel() {
		try {
			if (null == conn) {
				getConnection();
			}
			if (null == conn) {
				getConnectionUrl();
			}
			Channel channel = conn.createChannel();
			return channel;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
