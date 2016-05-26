package com.meizu.simplify.ioc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.junit.Test;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月7日 下午6:30:19</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月7日 下午6:30:19</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class StartupTest {
	@Test
	public void startup() {
		Startup.start();
	}
	@Test
	public void stop() {
		try {
			TestRuntime.main(new String[]{"ping"});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * <p><b>Title:</b><i>测试Process在jdk1.8中对于进程的管理</i></p>
	 * <p>Desc: TODO</p>
	 * <p>source folder:{@docRoot}</p>
	 * <p>Copyright:Copyright(c)2014</p>
	 * <p>Company:meizu</p>
	 * <p>Create Date:2016年5月20日 下午4:18:39</p>
	 * <p>Modified By:luchuangye-</p>
	 * <p>Modified Date:2016年5月20日 下午4:18:39</p>
	 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
	 * @version Version 0.1
	 *
	 */
	static class TestRuntime {
//		java -jar newProcessTest.jar java -version
		public static void main(String[] args) throws IOException {
			Process process = Runtime.getRuntime().exec(args[0]);
			exeProcess(process);
			System.out.println("===============ProcessBuilder redirectErrorStream(false)======================");
			Process process2 = new ProcessBuilder().redirectErrorStream(false).command(Arrays.asList(args)).start();
			exeProcess(process2);
			System.out.println("===============ProcessBuilder redirectErrorStream(true)======================");
			Process process3 = new ProcessBuilder().redirectErrorStream(true).command(Arrays.asList(args)).start();
			exeProcess(process3);
		}

		public static void exeProcess(Process process) throws IOException {
			BufferedReader isr = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			while ((line = isr.readLine()) != null) {
				System.out.println(line);
			}
			BufferedReader isr1 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String line1 = "";
			while ((line1 = isr1.readLine()) != null) {
				System.out.println("error:" + line1);
			}
			process.destroy();
		}
	}
}
