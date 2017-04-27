package vip.simplify.config.api.eums;
/**
  * <p><b>Title:</b><i>配置类型枚举</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年12月8日 下午4:08:48</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年12月8日 下午4:08:48</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public enum ConfigTypeEnum {
	File(0),
	Property(1);
	
	private int type;
	ConfigTypeEnum(int type) {
		this.type = type;
	}
	
	public int getValue() {
		return type;
	}
}
