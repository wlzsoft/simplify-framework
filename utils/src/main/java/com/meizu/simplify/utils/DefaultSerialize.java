package com.meizu.simplify.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * <p><b>Title:</b><i>序列化工具类</i></p>
* <p>Desc: TODO</p>
* <p>source folder:{@docRoot}</p>
* <p>Copyright:Copyright(c)2014</p>
* <p>Company:meizu</p>
* <p>Create Date:2016年1月13日 下午2:03:09</p>
* <p>Modified By:luchuangye-</p>
* <p>Modified Date:2016年1月13日 下午2:03:09</p>
* @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
* @version Version 0.1
*
*/
public class DefaultSerialize {


	/**
	 * 
	 * 
	 * 方法用途: 序列化<br>
	 * 操作步骤: Object转byte[]<br>
	 * @param obj
	 * @return
	 */
	public static byte[] encode(Object obj) throws IOException {
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
		//
//		byte[] bytes = null;
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		try {
//			ObjectOutputStream oos = new ObjectOutputStream(bos);
//			oos.writeObject(obj);
//			oos.flush();
//			bytes = bos.toByteArray();
//			oos.close();
//			bos.close();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//		return bytes;
	}

	/**
	 * 方法用途: 反序列化对象<br>
	 * 操作步骤: byte[]转Object<br>
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object decode(byte[] bytes) throws IOException,
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
		//
//		Object obj = null;
//		try {
//			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//			ObjectInputStream ois = new ObjectInputStream(bis);
//			obj = ois.readObject();
//			ois.close();
//			bis.close();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		} catch (ClassNotFoundException ex) {
//			ex.printStackTrace();
//		}
//		return obj;
	}

}
