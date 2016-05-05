package com.meizu.simplify.cache.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

/**
 * <p><b>Title:</b><i>redis性能测试</i></p>
 * <p>Desc: 
Transaction	Pipeline	耗时（s）
测试一：	N		N		311
测试二：	N		Y		10
测试三：	Y		Y		7
测试四：	Y		N		10
 
结论：Pipeline + Transaction方式是几种插入方式中性能最好的。测试多次仍然是这个结论。</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月5日 下午2:37:02</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月5日 下午2:37:02</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class RedisTransactionTest {

	private static String host = "127.0.0.1";

	private static JedisPool pool = new JedisPool(new JedisPoolConfig(), host);

	private long rowCount = 1000000; // 100万

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		new RedisTransactionTest().noTransactionNoPipeline();
		System.out.println("noTransactionNoPipeline use " + (System.currentTimeMillis() - start) / 1000);

		pool = new JedisPool(new JedisPoolConfig(), host);
		start = System.currentTimeMillis();
		new RedisTransactionTest().pipelineWithoutTransaction();
		System.out.println("pipelineWithoutTransaction use " + (System.currentTimeMillis() - start) / 1000);

		pool = new JedisPool(new JedisPoolConfig(), host);
		start = System.currentTimeMillis();
		new RedisTransactionTest().pipelineWithTransaction();
		System.out.println("pipelineWithTransaction use " + (System.currentTimeMillis() - start) / 1000);

		pool = new JedisPool(new JedisPoolConfig(), host);
		start = System.currentTimeMillis();
		new RedisTransactionTest().transactionNoPipeline();
		System.out.println("transactionNoPipeline use " + (System.currentTimeMillis() - start) / 1000);
		System.out.println(System.currentTimeMillis()-start);

	}

	public void pipelineWithoutTransaction() {
		Jedis jedis = pool.getResource();
		try {
			Pipeline p = jedis.pipelined();
			for (int i = 0; i < rowCount; i++) {
				String key = RandomStringUtils.randomAlphabetic(8);
				p.set(key, RandomStringUtils.randomNumeric(5));
				p.expire(key, 5 * 60);
			}
			p.sync();
		} catch (Exception e) {
			pool.returnResource(jedis);
		}
	}

	public void pipelineWithTransaction() {
		Jedis jedis = pool.getResource();
		try {
			Pipeline p = jedis.pipelined();
			p.multi();
			for (int i = 0; i < rowCount; i++) {
				String key = RandomStringUtils.randomAlphabetic(8);
				p.set(key, RandomStringUtils.randomNumeric(5));
				p.expire(key, 5 * 60);
			}
			p.exec();
			p.sync();
		} catch (Exception e) {
			pool.returnResource(jedis);
		}
	}

	public void noTransactionNoPipeline() {
		Jedis jedis = pool.getResource();
		try {
			for (int i = 0; i < rowCount; i++) {
				String key = RandomStringUtils.randomAlphabetic(8);
				jedis.set(key, RandomStringUtils.randomNumeric(5));
				jedis.expire(key, 5 * 60);
			}
		} catch (Exception e) {
			pool.returnResource(jedis);
		}
	}

	public void transactionNoPipeline() {
		Jedis jedis = pool.getResource();
		try {
			Transaction tx = jedis.multi();
			for (int i = 0; i < rowCount; i++) {
				String key = RandomStringUtils.randomAlphabetic(8);
				tx.set(key, RandomStringUtils.randomNumeric(5));
				tx.expire(key, 5 * 60);
			}
			tx.exec();
		} catch (Exception e) {
			pool.returnResource(jedis);
		}
	}


}
