package com.meizu.simplify.utils;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <p><b>Title:</b><i>哈希一致性算法</i></p>
 * <p>Desc: 封装了机器节点的信息 ，如name、password、ip、port等</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 下午12:36:38</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 下午12:36:38</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class ConsistentHashShard<T> {
	private TreeMap<Long, T> nodes;
	private List<T> shards; 

	private final int NODE_NUM = 100; 

	public ConsistentHashShard(List<T> shards) {
		super();
		this.shards = shards;
		init();
	}

	private void init() { 
		nodes = new TreeMap<Long, T>();
		for (int i = 0; i != shards.size(); ++i) { 
			final T shardInfo = shards.get(i);

			for (int n = 0; n < NODE_NUM; n++)
				
				nodes.put(hash("SHARD-" + i + "-NODE-" + n), shardInfo);

		}
	}

	public T getShardInfo(String key) {
		SortedMap<Long, T> tail = nodes.tailMap(hash(key)); 
		if (tail.size() == 0) {
			return nodes.get(nodes.firstKey());
		}
		return tail.get(tail.firstKey()); 
	}
	
	private Long hash(String key){
		return HashGen.hash(key);
	}

}
