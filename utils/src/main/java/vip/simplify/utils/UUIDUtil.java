package vip.simplify.utils;

import java.util.UUID;
/**
 * <p><b>Title:</b><i>UUID工具类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月1日 下午6:42:45</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月1日 下午6:42:45</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class UUIDUtil {
	public static String getRandomUUID() {
		UUID uuid = UUID.randomUUID();
		String result = (digits(uuid.getMostSignificantBits() >> 32, 8) + 
                digits(uuid.getMostSignificantBits() >> 16, 4) + 
                digits(uuid.getMostSignificantBits(), 4) + 
                digits(uuid.getLeastSignificantBits() >> 48, 4) +
                digits(uuid.getLeastSignificantBits(), 12));
		return result;
	}
	
	/** Returns val represented by the specified number of hex digits. */
    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }
}
