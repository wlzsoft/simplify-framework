package vip.simplify.datasource.config;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import vip.simplify.datasource.config.pojo.HostSwitch;
import vip.simplify.datasource.config.pojo.MasterHost;
import vip.simplify.datasource.config.pojo.SlaveHost;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月10日 上午11:56:15</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月10日 上午11:56:15</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class HostRouteConfigResolverForYamlTest {
	
	@Test
    public  void test1() {
        try {
//        	while(true) 
        	{
	            Yaml yaml = new Yaml();
	            URL url = HostRouteConfigResolverForYamlTest.class.getClassLoader().getResource("host-switch.yaml");
	            if (url != null) {
	                Object obj = yaml.load(new FileInputStream(url.getFile()));
	                System.out.println(obj);
	            }
//	            TimeUnit.SECONDS.sleep(5);
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test2() {
        try {
            HostSwitch h1 = new HostSwitch();
            h1.setName("host1");
            MasterHost masterHost = new MasterHost();
            masterHost.setName("master1");
            masterHost.setUrl("localhost:3306");
            masterHost.setUserName("root");
            masterHost.setPassword("123456");
            List<SlaveHost> slaveHostList = new ArrayList<>();
            SlaveHost slaveHost = new SlaveHost();
            slaveHost.setName("slave1");
            slaveHost.setUrl("localhost:8087");
            slaveHost.setUserName("root");
            slaveHost.setPassword("");
            slaveHostList.add(slaveHost);
            masterHost.setSlaveHostList(slaveHostList);
            List<MasterHost> masterHostList = new ArrayList<>();
            masterHostList.add(masterHost);
            h1.setMasterHostList(masterHostList);
            Yaml yaml = new Yaml();
            yaml.dump(h1, new FileWriter("hostSwitchList.yaml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        test2();
    }
}