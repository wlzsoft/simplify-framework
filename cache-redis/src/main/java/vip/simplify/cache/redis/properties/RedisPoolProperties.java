package vip.simplify.cache.redis.properties;
/**
  * <p><b>Title:</b><i>redis连接池配置信息实体</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月19日 下午2:24:02</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月19日 下午2:24:02</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class RedisPoolProperties {
	private Integer maxWaitMillis = 10000;
	private Integer maxIdle = 1000;
	private Integer maxTotal = 5000;
	private Integer minIdle = 20;
	private Boolean testOnBorrow = true;
	private Boolean testWhileIdle = false;
//	private Integer timeBetweenEvictionRunsMillis = 30000;
//	private Integer numTestsPerEvictionRun= 10000;
	public Integer getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(Integer maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	public Integer getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}
	public Integer getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}
	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}
	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	public Boolean getTestWhileIdle() {
		return testWhileIdle;
	}
	public void setTestWhileIdle(Boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}
	public Integer getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}
	@Override
	public String toString() {
		return "[maxWaitMillis=" + maxWaitMillis + ", maxIdle=" + maxIdle + ", maxTotal=" + maxTotal
				+ ", minIdle=" + minIdle + ", testOnBorrow=" + testOnBorrow + ", testWhileIdle=" + testWhileIdle + "]";
	}

}