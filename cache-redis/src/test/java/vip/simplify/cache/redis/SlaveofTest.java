package vip.simplify.cache.redis;


import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/** 
 * slaveof 和 slaveofNoOne方法可以在运行中动态设置服务器为Master或Slave 
 */  
public class SlaveofTest {  
  
    protected static Logger logger = LoggerFactory.getLogger(SlaveofTest.class);  
  
    /**
     * 测试主从复制 注意设置Slave之后需要时间复制数据 
     */  
    @Test  
    public void test() throws InterruptedException {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // timeout设置为0，解决JedisConnectionException
        JedisPool pool1 = new JedisPool(poolConfig, "127.0.0.1", 6379, 0);
        JedisPool pool2 = new JedisPool(poolConfig, "127.0.0.1", 6380, 0);

        Jedis jedis_M = pool2.getResource();
        Jedis jedis_S = pool1.getResource();


        jedis_M = new Jedis("127.0.0.1",6379,0);//主机
        jedis_S = new Jedis("127.0.0.1",6380,0);//从机

        //遵循“配从不配主”的模式
        jedis_S.slaveof("192.168.248.129",6379);

        jedis_M.set("class", "8888");//主机去写

        //内存中读写太快，防止读在写之前先完成而出现null的情况，这里做一下延迟
        Thread.sleep(2000);

        String result = jedis_S.get("class");//从机去读
        System.out.println(result);



        // 设置 Redis2为Redis1的slave  
        jedis_M.slaveofNoOne();
        jedis_S.slaveof("192.168.3.98", 6379);
  
        try {
            jedis_S.set("key", "value");
        } catch (Exception e) {  
            logger.warn("由于设置的原因slave为read only");  
        }  
  
        // 主从复制 Redis1中保存数据Redis2中可以读取  
        logger.info("jedis_M.set mykey1 myvalue1 : " + jedis_M.set("mykey1", "myvalue1"));
        try {  
            Thread.sleep(1000);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
        logger.info("jedis_S.get mykey1: " + jedis_S.get("mykey1"));
  
        // 单点问题解决思路(思路启用Redis2为Master，Redis1为SlaveOf Redis1)  
        // 提升Redis2为Master，读取刚才的数据，重启Redis1，将Redis2设置为Redis1的Master  
        jedis_S.slaveofNoOne();
        // 测试值仍然存在  
        logger.info("jedis_S.get again : " + jedis_S.get("mykey1"));
        // Redis2中存入数据  
        logger.info("jedis_S.set mykey2 myvalue2 : "+jedis_S.set("mykey2", "myvalue2"));
        // Redis1设置为Redis2的slave  
        jedis_M.slaveof("192.168.3.98", 6380);
        try {  
            Thread.sleep(1000);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
        // 测试jedis_M中读取数据
        logger.info("jedis_M.get mykey2 : " + jedis_M.get("mykey2"));

        jedis_M.slaveofNoOne();
        jedis_M.del("mykey1", "mykey2");
        jedis_S.del("mykey1", "mykey2");
    }  
  
}  