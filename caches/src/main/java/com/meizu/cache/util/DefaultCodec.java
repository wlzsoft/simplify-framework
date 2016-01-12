package com.meizu.cache.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DefaultCodec {

	/**
	 * 序列化
	 * 
	 * @param obj
	 * @return
	 */
	public byte[] encode(Object obj) throws IOException {
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		byte[] bytesJava = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			bytesJava = bos.toByteArray();
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (bos != null)
				bos.close();
			if (oos != null)
				oos.close();
		}
		return bytesJava;
	}

	/**
	 * 反序列化对象
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object decode(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		Object ret = null;

		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			ret = ois.readObject();
		} catch (IOException e) {
			throw new IOException(e);
		} catch (ClassNotFoundException ex) {
			throw new ClassNotFoundException("",ex);
		} finally {
			if (ois != null)
				ois.close();
		}

		return ret;
	}

}
