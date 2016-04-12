package com.meizu.simplify.stresstester.core;

import java.util.List;

/**
 * <p><b>Title:</b><i>统计工具类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年5月5日 下午4:06:38</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年5月5日 下午4:06:38</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class StatisticsUtils {

	public static long getTotal(List<Long> times) {
		long total = 0;
		for (Long time : times) {
			total = total + time;
		}
		return total;
	}

	public static float getAverage(List<Long> allTimes) {
		long total = getTotal(allTimes);
		return getAverage(total, allTimes.size());
	}

	public static float getAverage(long total, int size) {
		return total / (float) size;
	}

	public static float getTps(float ms, int concurrencyLevel) {
		return concurrencyLevel / ms * 1000;
	}

	public static float toMs(long nm) {
		return nm / 1000000f;
	}

	public static float toMs(float nm) {
		return nm / 1000000f;
	}
}
