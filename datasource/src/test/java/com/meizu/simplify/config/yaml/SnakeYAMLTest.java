package com.meizu.simplify.config.yaml;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class SnakeYAMLTest {
	
	@Test
    public  void test1() {
        try {
//        	while(true) 
        	{
	            Yaml yaml = new Yaml();
	            URL url = SnakeYAMLTest.class.getClassLoader().getResource("route.yaml");
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
            User c1 = new User("test1", 1, Arrays.asList(
                    new com.meizu.simplify.config.yaml.Test("test1", "1"),
                    new com.meizu.simplify.config.yaml.Test("test2", "2")));
            User c2 = new User("test2", 23, Arrays.asList(
                    new com.meizu.simplify.config.yaml.Test("test1", "3"),
                    new com.meizu.simplify.config.yaml.Test("test2", "4")));
            List<?> contacts = Arrays.asList(c1, c2);
            Yaml yaml = new Yaml();
            yaml.dump(contacts, new FileWriter("test.yaml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        test2();
    }
}