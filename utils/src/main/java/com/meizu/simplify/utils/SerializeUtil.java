package com.meizu.simplify.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.nustaq.serialization.FSTConfiguration;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.meizu.simplify.exception.UncheckedException;

/**
 * <p><b>Title:</b><i>序列化工具类</i></p>
 * <p>Desc: TODO</p>
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
public class SerializeUtil {

	public SerializeUtil() {
	}

	static FSTConfiguration configuration = FSTConfiguration
			// .createDefaultConfiguration();
			.createStructConfiguration();

	public static byte[] serialize(Object obj) {
		return configuration.asByteArray(obj);
	}

	public static Object unserialize(byte[] sec) {
		return configuration.asObject(sec);
	}

	public static byte[] kryoSerizlize(Object obj) {
		Kryo kryo = new Kryo();
		byte[] buffer = new byte[2048];
		try (Output output = new Output(buffer);) {

			kryo.writeClassAndObject(output, obj);
			return output.toBytes();
		} catch (Exception e) {
		}
		return buffer;
	}

	static Kryo kryo = new Kryo();

	public static Object kryoUnSerizlize(byte[] src) {
		try (Input input = new Input(src);) {
			return kryo.readClassAndObject(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kryo;
	}

	/**
	 * 
	 * 
	 * 方法用途: 序列化-jdk原生序列换方案<br>
	 * 操作步骤: Object转byte[]<br>
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] jdkSerialize(Object obj) {

		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		byte[] bytesJava = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			bytesJava = bos.toByteArray();
		} catch (IOException e) {
			throw new UncheckedException(e);
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return bytesJava;
	}

	/**
	 * 方法用途: 反序列化对象-jdk原生序列换方案<br>
	 * 操作步骤: byte[]转Object<br>
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object jdkDeserialize(byte[] bytes) {
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		Object ret = null;

		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			ret = ois.readObject();
		} catch (IOException e) {
			throw new UncheckedException(e);
		} catch (ClassNotFoundException ex) {
			throw new UncheckedException("", ex);
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return ret;
	}
}