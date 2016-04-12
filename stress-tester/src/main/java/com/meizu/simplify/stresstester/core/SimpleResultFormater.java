package com.meizu.simplify.stresstester.core;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p><b>Title:</b><i>测试结果格式化类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年5月5日 下午4:05:36</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年5月5日 下午4:05:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class SimpleResultFormater implements StressResultFormater {
	public static final Logger log = LoggerFactory.getLogger(SimpleResultFormater.class);

	public void format(StressResult stressResult, Writer writer) {
		long testsTakenTime = stressResult.getTestsTakenTime();
		int totalTasks = stressResult.getTotalTasks();
		int concurrencyLevel = stressResult.getConcurrencyLevel();

		float takes = StatisticsUtils.toMs(testsTakenTime);

		List<Long> allTimes = stressResult.getAllTimes();
		long totaleTimes = StatisticsUtils.getTotal(allTimes);

		// float tps = (totalTasks * 1000) / takes;
		float tps = 1000 * 1000000 * (concurrencyLevel * (totalTasks / (float) totaleTimes));

		float averageTime = StatisticsUtils.getAverage(totaleTimes,
				totalTasks);
		/** 理论单线程请求响应时间 */
		float onTheadAverageTime = averageTime / concurrencyLevel;

		int count_50 = totalTasks / 2;
		int count_66 = totalTasks * 66 / 100;
		int count_75 = totalTasks * 75 / 100;
		int count_80 = totalTasks * 80 / 100;
		int count_90 = totalTasks * 90 / 100;
		int count_95 = totalTasks * 95 / 100;
		int count_98 = totalTasks * 98 / 100;
		int count_99 = totalTasks * 99 / 100;

		long longestTask = allTimes.get(allTimes.size() - 1);
		long shortestTask = allTimes.get(0);

		StringBuilder view = new StringBuilder();

		// if (StringUtils.isNotBlank(serviceName)) {
		// view.append(" Service Name:\t").append(serviceName);
		// view.append("\r\n");
		// }
		
		
		Properties properties = new Properties();
//		properties.setProperty("ConcurrencyLevel", "Concurrency Level");
//		properties.setProperty("TimeTakenForTests", "Time taken for tests");
//		properties.setProperty("CompleteTasks", "Complete Tasks");
//		properties.setProperty("FailedTasks", "Failed Tasks");
//		properties.setProperty("TasksPerSecond", "Tasks per second");
//		properties.setProperty("TimePerTask", "Time per task");
//		properties.setProperty("TimePerTaskForAllConcurrent", "across all concurrent tasks");
//		properties.setProperty("ShortestTask", "Shortest task");
//		properties.setProperty("resultinfo", "Percentage of the tasks served within a certain time");
//		properties.setProperty("longestTask", "longest task");
//		properties.setProperty("time", "ms");
		properties.setProperty("ConcurrencyLevel", "并发用户数");
		properties.setProperty("TimeTakenForTests", "所有请求处理完成花费的总时间");
		properties.setProperty("CompleteTasks", "完成任务数");
		properties.setProperty("FailedTasks", "失败任务数");
		properties.setProperty("TasksPerSecond", "每秒完成任务数");
		properties.setProperty("TimePerTask", "每个任务花费时间");
		properties.setProperty("TimePerTaskForAllConcurrent", "访问所有并行任务");
		properties.setProperty("ShortestTask", "花费时间最少的任务");
		properties.setProperty("resultinfo", "一定时间内的任务百分比");
		properties.setProperty("longestTask", "最后一个任务完成时间");
		properties.setProperty("time", "毫秒");
		
		
		view.append(" ").append(properties.get("ConcurrencyLevel")).append(":\t").append(concurrencyLevel);
		view.append("\r\n ").append(properties.get("TimeTakenForTests")).append(":\t").append(takes).append(" ").append(properties.get("time"));
		view.append("\r\n ").append(properties.get("CompleteTasks")).append(":\t").append(totalTasks);
		view.append("\r\n ").append(properties.get("FailedTasks")).append(":\t\t").append(
				stressResult.getFailedTasks());
		view.append("\r\n ").append(properties.get("TasksPerSecond")).append(":\t").append(tps);
		view.append("\r\n ").append(properties.get("TimePerTask")).append(":\t\t")
				.append(StatisticsUtils.toMs(averageTime)).append(" ").append(properties.get("time"));
		view.append("\r\n ").append(properties.get("TimePerTask")).append(":\t\t")
				.append(StatisticsUtils.toMs(onTheadAverageTime))
				.append(" ").append(properties.get("time")).append(" (").append(properties.get("TimePerTaskForAllConcurrent")).append(")");
		view.append("\r\n ").append(properties.get("ShortestTask")).append(":\t\t")
				.append(StatisticsUtils.toMs(shortestTask)).append(" ").append(properties.get("time"));

		StringBuilder certainTimeView = view;
		certainTimeView
				.append("\r\n ").append(properties.get("resultinfo")).append(" (").append(properties.get("time")).append(")");
		certainTimeView.append("\r\n  50%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_50)));
		certainTimeView.append("\r\n  66%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_66)));
		certainTimeView.append("\r\n  75%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_75)));
		certainTimeView.append("\r\n  80%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_80)));
		certainTimeView.append("\r\n  90%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_90)));
		certainTimeView.append("\r\n  95%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_95)));
		certainTimeView.append("\r\n  98%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_98)));
		certainTimeView.append("\r\n  99%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_99)));
		certainTimeView.append("\r\n 100%\t")
				.append(StatisticsUtils.toMs(longestTask))
				.append(" (").append(properties.get("longestTask")).append(")");

		try {
			writer.write(view.toString());
		} catch (IOException e) {
			log.error("IOException:", e);
		}

	}


}
