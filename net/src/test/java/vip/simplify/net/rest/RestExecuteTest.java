package vip.simplify.net.rest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import vip.simplify.dto.Result;
import vip.simplify.dto.ResultObject;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.test.SimplifyJUnit4ClassRunner;

@Bean
@RunWith(SimplifyJUnit4ClassRunner.class)
public class RestExecuteTest {
	
	@Inject
	private RestExecute restExecute;
	
	@Test
	public void test() {
		List<String> hostList = new ArrayList<>();
		hostList.add("http://127.0.0.1:8080");
		String tokenId = "12343";
		RestAddressInfo restAddressInfo = new RestAddressInfo("/sys/checkLogin.json?tokenId="+tokenId , hostList);
		Result result = restExecute.get(ResultObject.class, restAddressInfo, 3, 2);
		System.out.println(result);
	}
}
