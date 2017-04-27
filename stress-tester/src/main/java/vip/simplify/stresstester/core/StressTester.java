package vip.simplify.stresstester.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p><b>Title:</b><i>压力测试器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年5月5日 下午4:08:54</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年5月5日 下午4:08:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class StressTester {
	private static Logger log = LoggerFactory.getLogger(StressTester.class);

	private int defaultWarmUpTime = 1700;

	private StressTask emptyTestService = new StressTask() {
		@Override
		public Object doTask() throws Exception {
			// ignore
			return null;
		}

	};

	static {
		warnSelf();
	}

	protected static void warnSelf() {
		for (int i = 0; i < 50; i++) {
			StressTester benchmark = new StressTester();
			benchmark.test(10, 100, null, 0);
		}
	}

	// warm up
	protected void warmUp(int warmUpTime, StressTask stressTask) {
		for (int i = 0; i < warmUpTime; i++) {
			try {
				stressTask.doTask();
				// benchmarkWorker.doRun();
			} catch (Exception e) {
				log.error("Test exception", e);
			}
		}
	}

	public StressResult test(int concurrencyLevel, int totalTasks,
			StressTask stressTask) {
		return test(concurrencyLevel, totalTasks, stressTask,
				defaultWarmUpTime);
	}

	public StressResult test(int concurrencyLevel, int totalTasks,
			StressTask stressTask, int warmUpTime) {

		if (null == stressTask) {
			stressTask = emptyTestService;
		}
		warmUp(warmUpTime, stressTask);
		int everyThreadCount = totalTasks / concurrencyLevel;
		CyclicBarrier threadStartBarrier = new CyclicBarrier(concurrencyLevel);
		CountDownLatch threadEndLatch = new CountDownLatch(concurrencyLevel);
		AtomicInteger failedCounter = new AtomicInteger();

		StressContext stressContext = new StressContext();
		stressContext.setTestService(stressTask);
		stressContext.setEveryThreadCount(everyThreadCount);
		stressContext.setThreadStartBarrier(threadStartBarrier);
		stressContext.setThreadEndLatch(threadEndLatch);
		stressContext.setFailedCounter(failedCounter);

		ExecutorService executorService = Executors
				.newFixedThreadPool(concurrencyLevel);

		List<StressThreadWorker> workers = new ArrayList<StressThreadWorker>(
				concurrencyLevel);
		for (int i = 0; i < concurrencyLevel; i++) {
			StressThreadWorker worker = new StressThreadWorker(stressContext,
					everyThreadCount);
			workers.add(worker);
		}

		// long start = System.nanoTime();
		for (int i = 0; i < concurrencyLevel; i++) {
			StressThreadWorker worker = workers.get(i);
			executorService.submit(worker);
		}

		try {
			threadEndLatch.await();
		} catch (InterruptedException e) {
			log.error("InterruptedException", e);
		}

		// executorService.shutdown();
		executorService.shutdownNow();

		// long limit = end - start;s
		// long startLimit = testContext.getStartTime() - start;
		int realTotalTasks = everyThreadCount * concurrencyLevel;
		int failedTasks = failedCounter.get();
		StressResult stressResult = new StressResult();

		SortResult sortResult = getSortedTimes(workers);
		List<Long> allTimes = sortResult.allTimes;

		stressResult.setAllTimes(allTimes);
		List<Long> trheadTimes = sortResult.trheadTimes;
		long totalTime = trheadTimes.get(trheadTimes.size() - 1);

		stressResult.setTestsTakenTime(totalTime);
		stressResult.setFailedTasks(failedTasks);
		stressResult.setTotalTasks(realTotalTasks);
		stressResult.setConcurrencyLevel(concurrencyLevel);
		stressResult.setWorkers(workers);

		return stressResult;

	}

	protected SortResult getSortedTimes(List<StressThreadWorker> workers) {
		List<Long> allTimes = new ArrayList<Long>();
		List<Long> trheadTimes = new ArrayList<Long>();
		for (StressThreadWorker worker : workers) {
			List<Long> everyWorkerTimes = worker.getEveryTimes();

			long workerTotalTime = StatisticsUtils.getTotal(everyWorkerTimes);
			trheadTimes.add(workerTotalTime);

			for (Long time : everyWorkerTimes) {
				allTimes.add(time);
			}
		}
		Collections.sort(allTimes);
		Collections.sort(trheadTimes);
		SortResult result = new SortResult();
		result.allTimes = allTimes;
		result.trheadTimes = trheadTimes;
		return result;
	}

	class SortResult {
		List<Long> allTimes;
		List<Long> trheadTimes;
	}

}
