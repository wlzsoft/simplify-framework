package com.meizu.simplify.stresstester.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p><b>Title:</b><i>测试工作线程</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年5月5日 下午4:09:07</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年5月5日 下午4:09:07</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
class StressThreadWorker implements Runnable {

	private static Logger log = LoggerFactory.getLogger(StressThreadWorker.class);
	private StressTask service;
	private CyclicBarrier threadStartBarrier;
	private CountDownLatch threadEndLatch;
	private AtomicInteger failedCounter = null;
	private int count;

	private List<Long> everyTimes;

	public StressThreadWorker(StressContext stressContext, int count) {
		super();
		this.threadStartBarrier = stressContext.getThreadStartBarrier();
		this.threadEndLatch = stressContext.getThreadEndLatch();
		this.failedCounter = stressContext.getFailedCounter();
		this.count = count;

		everyTimes = new ArrayList<Long>(count);

		this.service = stressContext.getTestService();
	}

	public List<Long> getEveryTimes() {
		return everyTimes;
	}

	@Override
	public void run() {
		try {
			threadStartBarrier.await();
			doRun();
		} catch (Exception e) {
			log.error("Test exception", e);
		}
	}

	protected void doRun() throws Exception {
		// 记录单次调用时间
		// 10000次测试工具耗时2ms
		for (int i = 0; i < count; i++) {
			long start = System.nanoTime();
			try {
				// Object result = service.test();
				service.doTask();
			} catch (Throwable e) {
				failedCounter.incrementAndGet();
				log.error("Test exception", e);
				// throw e;
			} finally {
				long stop = System.nanoTime();
				long limit = stop - start;
				everyTimes.add(limit);
			}
		}
		threadEndLatch.countDown();
	}

}
