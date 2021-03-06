package vip.simplify.utils.enums;

/**
 * <p><b>Title:</b><i>特殊字符常量</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 下午2:37:57</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 下午2:37:57</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public enum SpecialCharacterEnum {
	
	/**
	 * 可以是file的目录分割符，也可以是http等请求连接协议的目录分割符
	 */
	BACKSLASH("/"),
	/**
	 * 双斜线,可用于匹配windows下的File的目录分隔符
	 */
	DOUBLE_SLASH("\\"),
	FILE_SEPARATOR("file.separator"),
	LF("\n"),
	CR("\r"),
	COMMA(","),
	EQUAL("="),
	SEMICOLON(";"),
	COLON(":"),
	DOT("."),
	BAR("|"),
	UNDERLINE("_");

	private SpecialCharacterEnum(String name){
		this.name = name;
	}
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}
