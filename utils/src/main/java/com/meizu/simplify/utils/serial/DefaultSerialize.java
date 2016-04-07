package com.meizu.simplify.utils.serial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.meizu.simplify.exception.UncheckedException;

/**
 * <p><b>Title:</b><i>jdk原生序列化</i></p>
 * <p>Desc: </p>
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
public class DefaultSerialize<T> implements ISerialize<T>{

	public DefaultSerialize() {
	}

	/**
	 * 
	 * 方法用途: 序列化<br>
	 * 操作步骤: Object转byte[]<br>
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public  byte[] serialize(T obj) {

		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		byte[] ret = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			ret = bos.toByteArray();
			return ret;
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
	}

	/**
	 * 方法用途: 反序列化对象<br>
	 * 操作步骤: byte[]转Object<br>
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public  T unserialize(byte[] bytes) {
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		Object ret = null;

		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			ret = ois.readObject();
			return (T) ret;
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
	}
}