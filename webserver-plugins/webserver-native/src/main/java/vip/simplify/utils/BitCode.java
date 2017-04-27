package vip.simplify.utils;

import java.io.Serializable;

import vip.simplify.exception.UncheckedException;

/**
 * <p><b>Title:</b><i>位编码对象。位编码对象以指定位上的0和1表示是否具有某种特性</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月23日 下午3:41:05</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月23日 下午3:41:05</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class BitCode implements Serializable {
	private static final long serialVersionUID = 4469841880135184426L;
	public static final Integer DEFAULT_LENGTH = 300;
	private String code;

	/**
	 * 构造一个默认长度的位编码对象
	 */
	public BitCode() {
		this(DEFAULT_LENGTH);
	}

	/**
	 * 构造一个位编码对象
	 * @param code 位编码字符串
	 */
	public BitCode(String code) {
		if(StringUtil.isBlank(code)) {
			throw new UncheckedException("位编码字符串不能为空字符串");
		}
		this.code = code;
	}

	/**
	 * 构造指定长度（以基本长度为基数）的位编码对象
	 * @param length 长度
	 */
	public BitCode(Integer length) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			builder.append("0");
		}
		code = builder.toString();
	}

	/**
	 * 
	 * 方法用途: 设置编码<br>
	 * 操作步骤: TODO<br>
	 * @param code 编码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 
	 * 方法用途: 设置指定编码位（从1开始）的值<br>
	 * 操作步骤: TODO<br>
	 * @param index 编码位
	 * @param value 值
	 */
	public void setValue(Integer index, Boolean value) {
		char[] chars = code.toCharArray();
		chars[index - 1] = value ? '1' : '0';
		code = new String(chars);
	}

	/**
	 * 
	 * 方法用途: 以0表示false，1表示true，判断指定位上是否为true<br>
	 * 操作步骤: TODO<br>
	 * @param index 位数
	 * @return 返回指定位上是否为true
	 */
	public boolean isTrue(Integer index) {
		return code.charAt(index - 1) == '1';
	}

	/**
	 * 
	 * 方法用途: 以0表示false，1表示true，判断指定位上是否为false<br>
	 * 操作步骤: TODO<br>
	 * @param index 位数
	 * @return 返回指定位上是否为false
	 */
	public boolean isFalse(Integer index) {
		return code.charAt(index - 1) == '0';
	}

	/**
	 * 
	 * 方法用途: 与指定的位编码对象进行“或”运算，返回“或”运算结果<br>
	 * 操作步骤: TODO<br>
	 * @param bitCode 位编码对象
	 * @return 返回“或”运算结果
	 */
	public BitCode or(BitCode bitCode) {
		return new BitCode(or(code, bitCode.toString()));
	}

	/**
	 * 
	 * 方法用途: 与指定的位编码对象进行“与”运算，返回“与”运算结果<br>
	 * 操作步骤: TODO<br>
	 * @param bitCode 位编码对象
	 * @return 返回“与”运算结果
	 */
	public BitCode and(BitCode bitCode) {
		return new BitCode(and(code, bitCode.toString()));
	}

	/**
	 * 
	 * 方法用途: 获得位编码长度<br>
	 * 操作步骤: TODO<br>
	 * @return 返回位编码长度
	 */
	public int length() {
		return code.length();
	}

	/**
	 * 
	 * 方法用途: 获取模糊查询编码<br>
	 * 操作步骤: TODO<br>
	 * @return 返回模糊查询编码
	 */
	public BitCode getQueryBitCode() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length(); i++) {
			builder.append("_");
		}
		return new BitCode(builder.toString());
	}

	/**
	 * 
	 * 方法用途: 获取指定true和false位数的模糊查询编码<br>
	 * 操作步骤: TODO<br>
	 * @param trueBits 为true的位数集合
	 * @param falseBits 为false的位数集合
	 * @return 返回指定true和false位数的模糊查询编码
	 */
	public BitCode getQueryBitCode(Integer[] trueBits, Integer[] falseBits) {
		BitCode queryBitCode = getQueryBitCode();
		for (Integer trueBit : trueBits) {
			queryBitCode.setValue(trueBit, true);
		}
		for (Integer falseBit : falseBits) {
			queryBitCode.setValue(falseBit, false);
		}
		return queryBitCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		BitCode other = (BitCode) obj;
		if (code == null) {
			if (other.toString() != null) {
				return false;
			}
		} else if (!code.equals(other.toString())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return code;
	}

	/**
	 * 
	 * 方法用途: 对两个等长的位编码字符串进行“或”运算<br>
	 * 操作步骤: TODO<br>
	 * @param code1 位编码字符串
	 * @param code2 位编码字符串
	 * @return 返回“或”运算后的位编码字符串
	 */
	private String or(String code1, String code2) {
		if(StringUtil.isBlank(code1)) {
			throw new UncheckedException("位编码字符串不能为空字符串");
		}
		if(StringUtil.isBlank(code2)) {
			throw new UncheckedException("位编码字符串不能为空字符串");
		}
		if(code1.length() == code2.length()) {
			throw new UncheckedException("进行或运算的位编码长度必须一致");
		}

		char[] chars1 = code1.toCharArray();
		char[] chars2 = code2.toCharArray();

		for (int i = 0; i < chars1.length; i++) {
			chars1[i] = (char) ((int) chars1[i] | (int) chars2[i]);
		}
		return new String(chars1);
	}

	/**
	 * 
	 * 方法用途: 对两个等长的位编码字符串进行“与”运算<br>
	 * 操作步骤: TODO<br>
	 * @param code1 位编码字符串
	 * @param code2 位编码字符串
	 * @return 返回“与”运算后的位编码字符串
	 */
	private String and(String code1, String code2) {
		
		if(StringUtil.isBlank(code1)) {
			throw new UncheckedException("位编码字符串不能为空字符串");
		}
		if(StringUtil.isBlank(code2)) {
			throw new UncheckedException("位编码字符串不能为空字符串");
		}
		if(code1.length() == code2.length()) {
			throw new UncheckedException("进行与运算的位编码长度必须一致");
		}
		

		char[] chars1 = code1.toCharArray();
		char[] chars2 = code2.toCharArray();

		for (int i = 0; i < chars1.length; i++) {
			chars1[i] = (char) ((int) chars1[i] & (int) chars2[i]);
		}
		return new String(chars1);
	}
}
