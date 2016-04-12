package com.meizu.simplify.stresstester;

import java.io.StringWriter;

import com.meizu.simplify.stresstester.core.SimpleResultFormater;
import com.meizu.simplify.stresstester.core.StressResult;
import com.meizu.simplify.stresstester.core.StressResultFormater;
import com.meizu.simplify.stresstester.core.StressTask;
import com.meizu.simplify.stresstester.core.StressTester;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年5月5日 下午4:05:24</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年5月5日 下午4:05:24</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class StressTestUtils {

	private static StressTester stressTester = new StressTester();
	private static SimpleResultFormater simpleResultFormater = new SimpleResultFormater();

	public static StressResult test(int concurrencyLevel, int totalTasks,
			StressTask stressTask) {
		return stressTester.test(concurrencyLevel, totalTasks, stressTask);
	}

	public static StressResult test(int concurrencyLevel, int totalTasks,
			StressTask stressTask, int warmUpTime) {
		return stressTester.test(concurrencyLevel, totalTasks, stressTask,
				warmUpTime);
	}

	/**
	 * 
	 * 方法用途: 打印测试结果<br>
	 * 操作步骤: TODO<br>
	 * @param concurrencyLevel 并发数
	 * @param totalTasks 任务数
	 * @param stressTask 具体需要处理的任务逻辑
	 */
	public static void testAndPrint(int concurrencyLevel, int totalTasks,
			StressTask stressTask) {
		testAndPrint(concurrencyLevel, totalTasks, stressTask, null);
	}

	/**
	 * 
	 * 方法用途: 打印测试结果<br>
	 * 操作步骤: TODO<br>
	 * @param concurrencyLevel  并发数
	 * @param totalTasks  任务数
	 * @param stressTask  具体需要处理的任务逻辑
	 * @param testName
	 */
	public static void testAndPrint(int concurrencyLevel, int totalTasks,
			StressTask stressTask, String testName) {
		StressResult stressResult = test(concurrencyLevel, totalTasks,
				stressTask);
		String str = format(stressResult);
		System.out.println(str);
	}

	/**
	 * 
	 * 方法用途: 打印测试结果<br>
	 * 操作步骤: TODO<br>
	 * @param concurrencyLevel
	 * @param totalTasks
	 * @param stressTask
	 * @param warmUpTime
	 * @param testName
	 */
	public static void testAndPrint(int concurrencyLevel, int totalTasks,
			StressTask stressTask, int warmUpTime, String testName) {
		StressResult stressResult = test(concurrencyLevel, totalTasks,
				stressTask, warmUpTime);
		String str = format(stressResult);
		System.out.println(str);
	}

	/**
	 * 
	 * 方法用途: 格式化<br>
	 * 操作步骤: TODO<br>
	 * @param stressResult
	 * @return
	 */
	public static String format(StressResult stressResult) {
		return format(stressResult, simpleResultFormater);
	}

	/**
	 * 
	 * 方法用途: 格式化<br>
	 * 操作步骤: TODO<br>
	 * @param stressResult
	 * @param stressResultFormater
	 * @return
	 */
	public static String format(StressResult stressResult,
			StressResultFormater stressResultFormater) {
		StringWriter sw = new StringWriter();
		stressResultFormater.format(stressResult, sw);
		return sw.toString();
	}

}
