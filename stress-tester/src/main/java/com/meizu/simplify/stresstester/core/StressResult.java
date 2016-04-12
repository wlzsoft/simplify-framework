package com.meizu.simplify.stresstester.core;

import java.util.List;

/**
 * <p><b>Title:</b><i> 测试结果</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年5月5日 下午4:07:30</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年5月5日 下午4:07:30</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class StressResult {

	private int concurrencyLevel;// 并发线程数
	private int totalTasks;// 总任务次数

	private long testsTakenTime;// 总耗时
	private int failedTasks;// 失败任务次数

	private List<Long> allTimes;// 每次请求的耗时

	private List<StressThreadWorker> workers;

	public long getTestsTakenTime() {
		return testsTakenTime;
	}

	public int getConcurrencyLevel() {
		return concurrencyLevel;
	}

	public void setConcurrencyLevel(int concurrencyLevel) {
		this.concurrencyLevel = concurrencyLevel;
	}

	public int getTotalTasks() {
		return totalTasks;
	}

	public void setTotalTasks(int totalTasks) {
		this.totalTasks = totalTasks;
	}

	public void setTestsTakenTime(long testsTakenTime) {
		this.testsTakenTime = testsTakenTime;
	}

	public int getFailedTasks() {
		return failedTasks;
	}

	public void setFailedTasks(int failedTasks) {
		this.failedTasks = failedTasks;
	}

	public List<Long> getAllTimes() {
		return allTimes;
	}

	public void setAllTimes(List<Long> allTimes) {
		this.allTimes = allTimes;
	}

	public List<StressThreadWorker> getWorkers() {
		return workers;
	}

	public void setWorkers(List<StressThreadWorker> workers) {
		this.workers = workers;
	}

}
