package com.meizu.simplify.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p><b>Title:</b><i>创建异常性能测试</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月3日 下午2:43:54</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月3日 下午2:43:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class NewExceptionTest {

	private static final int LOOP = 1000000; // 单次循环数量
	private static final int THREADS = 100; // 并发线程数量

	private static final List<Long> newObjectTimes = new ArrayList<Long>(THREADS);
	private static final List<Long> newExceptionTimes = new ArrayList<Long>(THREADS);
	private static final List<Long> newExtExceptionTimes = new ArrayList<Long>(THREADS);

	private static final ExecutorService POOL = Executors.newFixedThreadPool(30);

	public static void main(String[] args) throws Exception {
		List<Callable<Boolean>> all = new ArrayList<Callable<Boolean>>();
		all.addAll(tasks(new NewObject()));
		all.addAll(tasks(new NewException()));
		all.addAll(tasks(new NewExtException()));

		POOL.invokeAll(all);

		System.out.println("普通正常对象:\t\t" + total(newObjectTimes));
		System.out.println("普通异常对象:\t\t" + total(newExceptionTimes));
		System.out.println("优化后的异常对象:\t\t" + total(newExtExceptionTimes));

		POOL.shutdown();
	}

	private static List<Callable<Boolean>> tasks(Callable<Boolean> c) {
		List<Callable<Boolean>> list = new ArrayList<Callable<Boolean>>(THREADS);
		for (int i = 0; i < THREADS; i++) {
			list.add(c);
		}
		return list;
	}

	private static long total(List<Long> list) {
		long sum = 0;
		for (Long v : list) {
			sum += v;
		}
		return sum;
	}

	public static class NewObject implements Callable<Boolean> {

		@Override
		public Boolean call() throws Exception {
			long start = System.currentTimeMillis();
			for (int i = 0; i < LOOP; i++) {
				new CustomObject("");
			}
			newObjectTimes.add(System.currentTimeMillis() - start);
			return true;
		}

	}

	public static class NewException implements Callable<Boolean> {

		@Override
		public Boolean call() throws Exception {
			long start = System.currentTimeMillis();
			for (int i = 0; i < LOOP; i++) {
				new CustomException("");
			}
			newExceptionTimes.add(System.currentTimeMillis() - start);
			return true;
		}

	}

	public static class NewExtException implements Callable<Boolean> {

		@Override
		public Boolean call() throws Exception {
			long start = System.currentTimeMillis();
			for (int i = 0; i < LOOP; i++) {
				new ExtCustomException("");
			}
			newExtExceptionTimes.add(System.currentTimeMillis() - start);
			return true;
		}

	}

	/**
	 * 自定义java对象.
	 */
	public static class CustomObject extends HashMap {

		private static final long serialVersionUID = 567393976548105L;

		private String message;

		public CustomObject(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

	/**
	 * 自定义普通的Exception对象
	 */
	public static class CustomException extends Exception {

		private static final long serialVersionUID = -6879298763723247455L;

		private String message;

		public CustomException(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

	/**
	 * <pre>
	 * 自定义改进的Exception对象 重写 fillInStackTrace方法
	 * 1. 不填充stack
	 * 2. 取消同步
	 * </pre>
	 */
	public static class ExtCustomException extends Exception {

		private static final long serialVersionUID = -6879298763723247455L;

		private String message;

		public ExtCustomException(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		@Override
		public Throwable fillInStackTrace() {
			return this;
		}
	}
}