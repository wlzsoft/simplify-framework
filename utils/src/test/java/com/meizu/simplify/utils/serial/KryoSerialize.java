package com.meizu.simplify.utils.serial;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * <p><b>Title:</b><i>Kryo序列化实现，兼容性不是很好</i></p>
 * <p>Desc: 压缩率最低低于Fst，但是性能低于Fst，两者差距不大
 *          Kryo优于Fst的一个地方是，有对反序列化时对数据完整性做校验，不完整会抛异常</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 下午8:17:32</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 下午8:17:32</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class KryoSerialize<T> implements ISerialize<T>{

	public KryoSerialize() {
	}
	static Kryo kryo = new Kryo();

	@Override
	public  byte[] serialize(T obj) {
		Kryo kryo = new Kryo();
		byte[] buffer = new byte[2048];
		try (Output output = new Output(buffer);) {

			kryo.writeClassAndObject(output, obj);
			return output.toBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public  T unserialize(byte[] src) {
		try (Input input = new Input(src);) {
			return (T) kryo.readClassAndObject(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}