package vip.simplify.utils;

import java.util.HashMap;


/**
 * <p><b>Title:</b><i>通过文件字节流，获取文件类型</i></p>
 * <p>Desc: 应用场景：1.可用于取上传文件字节流的文件类型
 *          TODO 暂未适用到,正式使用</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月25日 下午6:23:25</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月25日 下午6:23:25</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class FileMetaUtil {
	
	private final static HashMap<Long, String> filetypes = new HashMap<Long, String>();
	
	public final static long T_JPG = -503326465;
	public final static long T_GIF = 944130375;
	public final static long T_PNG = 1196314761;
	public final static long T_TXT = -1346972457;
	public final static long T_EXE = 9460301;
	
	static {
		filetypes.put(T_JPG, "jpg");
		filetypes.put(T_GIF, "gif");
		filetypes.put(T_PNG, "png");
		filetypes.put(T_TXT, "txt");
		filetypes.put(T_EXE, "exe");
	}

	private static long bytesToHexString(byte[] src) {
		long result = 0;
		if (src == null || src.length <= 0) { return -1; }
		for ( int i = 0; i < src.length; i++ ) {
			result |= (src[i] & 0xFF) << (i * 8);
		}
		return result;
	}

	/**
	 * 方法用途: 根据文件流取类型<br>
	 * 操作步骤: TODO<br>
	 * @param byteArr
	 * @return
	 */
	public final static String getType(byte[] byteArr) {
		byte[] fileByte = new byte[4];
		System.arraycopy(byteArr, 0, fileByte, 0, fileByte.length);
		return filetypes.get(bytesToHexString(fileByte));
	}
}
