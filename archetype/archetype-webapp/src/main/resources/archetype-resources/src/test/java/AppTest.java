package $package;

import org.junit.*;
import org.junit.runner.RunWith;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.test.SimplifyJUnit4ClassRunner;

@Bean
@RunWith(SimplifyJUnit4ClassRunner.class)
public class AppTest {
	
	@Before
    public void init( String testName ) {
    }

	@Test
    public void testApp() {
        Assert.assertTrue( true );
    }
}
