package vip.simplify.message.core;

/**
 * <p>消息队列发送模板</p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月27日 下午4:08:49</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月27日 下午4:08:49</p>
 * @author <a href="mailto:wanghaibin@meizu.com" >wanghb</a>
 * @version Version 3.0
 *
 */
public interface SendMessageTemplate {

	/**
	 * 方法用途: 发送消息<br>
	 * 操作步骤: TODO<br>
	 * @param <T>
	 * @param t 消息实体
	 */
	<T> void send(T t);
	/**
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param <T>
	 * @param routingKey 路由key
	 * @param t
	 */
	<T> void send(String routingKey,T t);

	/**
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param <T>
	 * @param queueName 队列名称
	 * @param exchange 交换机
	 * @param routingKey 路由key
	 * @param t 数据
	 */
	<T> void send(String queueName,String exchange, String routingKey,T t);

}
