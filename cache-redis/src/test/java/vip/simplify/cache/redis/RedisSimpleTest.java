package vip.simplify.cache.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 下午1:39:53</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 下午1:39:53</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class RedisSimpleTest {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1",6379,1000);
        System.out.println("Server is running: "+jedis.ping());
        jedis.quit();
        jedis.disconnect();
    }

    private Jedis jedis;

    @Before
    public  void testBeforeClass(){
        jedis = new Jedis("127.0.0.1",6379,1000);
        jedis.clientSetname("client1");
        jedis.flushAll();
    }

    @After
    public void release(){
        jedis.quit();
        jedis.disconnect();
    }

    @Test
    public void testKillClient(){

        String clientGetname=jedis.clientGetname();
        System.out.println("clientGetname="+clientGetname);

        String clientList=jedis.clientList();
        System.out.println("clientList="+clientList);
    }

}
