package com.meizu.simplify.utils.serial;

import org.nustaq.serialization.FSTConfiguration;

/**
 * <p><b>Title:</b><i>Fst序列化实现，兼容性很好-推荐使用</i></p>
 * <p>Desc: 性能最优优于Kryo,但是压缩率低于Kryo，差距不大
 * </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 下午8:17:32</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 下午8:17:32</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class FstSerialize<T> implements ISerialize<T>{

	public FstSerialize() {
	}

	static FSTConfiguration configuration = FSTConfiguration
			// .createDefaultConfiguration();
			.createStructConfiguration();

	@Override
	public  byte[] serialize(T obj) {
		return configuration.asByteArray(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  T unserialize(byte[] sec) {
		return (T) configuration.asObject(sec);
	}

}