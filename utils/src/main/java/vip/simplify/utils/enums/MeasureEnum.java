package vip.simplify.utils.enums;

/**
 * <p><b>Title:</b><i>计算机计量用的常量</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月27日 下午1:04:30</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年4月27日 下午1:04:30</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public enum MeasureEnum {
	
	/** 
	 *  K字节数  K = 1024 bytes 
	 *  1024 = 2的10次方
	 */
	K ,
	
	/** 
	 *  M字节数  M = 1024 K bytes
	 *  M = K * 1024
	 *  注意：乘法计算的过程是在启动时计算的，没有频繁计算，否则需要使用位运算的位移来计算来提高性能
	 */
	M {
		@Override
		public Integer getValue() {
			return K.getValue() << 10;
		}
	},
	
	/** 
	 *  G字节数 
	 *  G = M * 1024
	 */
	G {
		@Override
		public Integer getValue() {
			return M.getValue() << 10;
		}
	},
	
	/** 
	 *  T字节数
	 *  T = G * 1024 
	 */
	T {
		@Override
		public Integer getValue() {
			return G.getValue() << 10;
		}
	};
	
	private  Integer value = 1024;
	public Integer getValue(){
		return value;
	}
}
