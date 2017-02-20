package com.meizu.simplify.server;


import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThreadPool extends ThreadGroup {
	private static final Logger log = LoggerFactory.getLogger(ThreadPool.class);
	private boolean isClosed = false; 
	private LinkedList<Runnable> workQueue; 
	private static int threadPoolID = 1; 

	public ThreadPool(int poolSize) { 
		super(threadPoolID + ""); // 指定ThreadGroup的名称
		setDaemon(true); // 继承到的方法，设置是否守护线程池
		workQueue = new LinkedList<>(); 
		for (int i = 0; i < poolSize; i++) {
			new WorkThread(i).start(); 
		}
	}

	/**
	 * 向工作队列中加入一个新任务,由工作线程去执行该任务
	 * @param task
	 */
	public synchronized void execute(Runnable task) {
		if (isClosed) {
			throw new IllegalStateException();
		}
		if (task != null) {
			workQueue.add(task);
			notify(); // 唤醒一个正在getTask()方法中待任务的工作线程
		}
	}

	/**
	 * 从工作队列中取出一个任务,工作线程会调用此方法
	 * @param threadid
	 * @return
	 * @throws InterruptedException
	 */
	private synchronized Runnable getTask(int threadid)
			throws InterruptedException {
		while (workQueue.size() == 0) {
			if (isClosed)
				return null;
			log.debug("工作线程" + threadid + "等待任务...");
			wait(); // 如果工作队列中没有任务,就等待任务
		}
		log.debug("工作线程" + threadid + "开始执行任务...");
		return (Runnable) workQueue.removeFirst(); // 反回队列中第一个元素,并从队列中删除
	}

	public synchronized void closePool() {
		if (!isClosed) {
			waitFinish(); 
			isClosed = true;
			workQueue.clear(); 
			interrupt(); // 中断线程池中的所有的工作线程,此方法继承自ThreadGroup类
		}
	}

	/**
	 * 等待工作线程把所有任务执行完毕
	 */
	public void waitFinish() {
		synchronized (this) {
			isClosed = true;
			notifyAll(); // 唤醒所有还在getTask()方法中等待任务的工作线程
		}
		Thread[] threads = new Thread[activeCount()]; // activeCount()
														// 返回该线程组中活动线程的估计值。
		int count = enumerate(threads); // enumerate()方法继承自ThreadGroup类，根据活动线程的估计值获得线程组中当前所有活动的工作线程
		for (int i = 0; i < count; i++) { // 等待所有工作线程结束
			try {
				threads[i].join(); // 等待工作线程结束
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 负责从工作队列中取出任务,并执行
	 */
	private class WorkThread extends Thread {
		private int id;

		public WorkThread(int id) {
			// 父类构造方法,将线程加入到当前ThreadPool线程组中
			super(ThreadPool.this, id + "");
			this.id = id;
		}

		public void run() {
			while (!isInterrupted()) {
				Runnable task = null;
				try {
					task = getTask(id); 
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				// 如果getTask()返回null或者线程执行getTask()时被中断，则结束此线程
				if (task == null) {
					return;
				}
				try {
					task.run(); 
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}
}