package com.meizu.simplify.utils.collection;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * 
 * <p><b>Title:</b><i> First In First Out Map</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 上午11:41:33</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 上午11:41:33</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 * @param <K>
 * @param <V>
 */
public class FiFoMap<K, V> extends HashMap<K, V> {
	private static final long serialVersionUID = 8408346551202788401L;
	private int count = 100; // 列队限制
	private LinkedList<K> keyCount = new LinkedList<K>();

	private ICollectionCallBack<V> callBack = null;

	public FiFoMap(int count) {
		this.count = count;
	}
	
	public FiFoMap(int count, ICollectionCallBack<V> callBack) {
		this.count = count;
		this.callBack = callBack;
	}

	@Override
	public synchronized V put(K key, V value) {
		// 检查长度是否超过
		if (keyCount.size() >= count) {
			K rkey = keyCount.removeFirst();
			super.remove(rkey);
			if (callBack != null) {
				callBack.exec(rkey);
			}
		}
		keyCount.add(key);
		return super.put(key, value);
	};
}
