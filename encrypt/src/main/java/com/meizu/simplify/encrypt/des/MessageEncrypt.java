package com.meizu.simplify.encrypt.des;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.meizu.simplify.encrypt.md5.MD5MessageEncrypt;

/**
 * 
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 上午11:42:08</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 上午11:42:08</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public abstract class MessageEncrypt {
	private static Map<String, MessageEncrypt> algorithm = new HashMap<String, MessageEncrypt>();
	static {
		MessageEncrypt.put("MD5", new MD5MessageEncrypt());
		MessageEncrypt.put("cover", new CoverMessageEncrypt());
		MessageEncrypt.put("DES", new DESMessageEncrypt());
	}

	public String encode(String... input){
		StringBuilder sb = new StringBuilder();
		for(String s :input){
			sb.append(s);
		}
		return encode(sb.toString());
	}

	public String encode(String input){
		if(input != null) {
			byte[] b = null;
			try {
				b = encode(input.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if(b != null)
				return new String(b);
		}
		return null;
	}

	public String decode(String input) {
		if(input != null){
			byte[] b = null;
			try {
				b = decode(input.getBytes("UTF-8"));
				//b = decode(input.getBytes("GBK"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if(b != null)
				try {
					return new String(b, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					return new String(b);
				}
		}
		return null;
	}

	public abstract byte[] encode(byte[] input);

	public abstract byte[] decode(byte[] input);

	public static void put(String name, MessageEncrypt alg) {
		algorithm.put(name.toUpperCase(), alg);
	}

	public static MessageEncrypt getInstance(String name) {
		return algorithm.get(name.toUpperCase());
	}
}
