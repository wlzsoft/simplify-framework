package vip.simplify.rpc.Enum;
/**
 * <p>负载均衡</p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月12日 上午11:02:57</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月12日 上午11:02:57</p>
 * @author <a href="王海斌@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 3.0.0
 *
 */
public enum LoadbalanceEnum {
	/**
	 * 随机
	 */
	RANDOM("random"),
	/**
	 * 轮询
	 */
	ROUNDROBIN("roundrobin"), 
	/**
	 * 最少活跃调用
	 */
	LEASTACTIVE("leastactive"), 
	/**
	 * 一致性hash
	 */
	CONSISTENTHASH("consistenthash");
	
	public final String value;

	LoadbalanceEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
