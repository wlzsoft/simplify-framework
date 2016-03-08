package com.meizu.simplify.encrypt.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.meizu.simplify.encrypt.base64.ByteHexUtil;
import com.meizu.simplify.encrypt.des.MessageEncrypt;

/**
 * 
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 上午11:42:03</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 上午11:42:03</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class MD5MessageEncrypt extends MessageEncrypt {

	@Override
	public byte[] decode(byte[] input) {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] encode(byte[] input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input);
			return  ByteHexUtil.bytes2Hex(md.digest()).getBytes();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
