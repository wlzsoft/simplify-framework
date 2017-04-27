package vip.simplify.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p><b>Title:</b><i>抛异常性能测试</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月3日 下午2:44:07</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月3日 下午2:44:07</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class ThrowExceptionTest {

	private static final int LOOP = 10000000;
	private static final int THREADS = 200;

	private static final List<Long> tryCatchTimes = new ArrayList<Long>(THREADS);
	private static final List<Long> ifElseTimes = new ArrayList<Long>(THREADS);

	private static final ExecutorService POOL = Executors.newFixedThreadPool(40);

	public static void main(String[] args) throws Exception {
		List<Callable<Boolean>> all = new ArrayList<Callable<Boolean>>();
		all.addAll(tasks(new TryCatch()));
		all.addAll(tasks(new IfElse()));

		POOL.invokeAll(all);

		System.out.println("tc:\t\t" + total(tryCatchTimes));
		System.out.println("ie:\t\t" + total(ifElseTimes));

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

	public static class TryCatch implements Callable<Boolean> {

		@Override
		public Boolean call() throws Exception {
			long start = System.currentTimeMillis();
			for (int i = 0; i < LOOP; i++) {
				try {
					exception();
					//
				} catch (ExtCustomException e) {
					//
				}
			}
			tryCatchTimes.add(System.currentTimeMillis() - start);
			return true;
		}

		private void exception() throws ExtCustomException {
			throw new ExtCustomException("");
		}

	}

	public static class IfElse implements Callable<Boolean> {

		@Override
		public Boolean call() throws Exception {
			long start = System.currentTimeMillis();
			for (int i = 0; i < LOOP; i++) {
				Exception e = exception();
				if (e instanceof ExtCustomException) {
					//
				}
			}
			ifElseTimes.add(System.currentTimeMillis() - start);
			return true;
		}

		private Exception exception() {
			return new ExtCustomException("");
		}

	}

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