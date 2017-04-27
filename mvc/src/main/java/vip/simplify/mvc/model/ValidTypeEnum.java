package vip.simplify.mvc.model;

/**
 * <p><b>Title:</b><i>校验类型枚举</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月22日 下午4:58:08</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年7月22日 下午4:58:08</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public enum ValidTypeEnum {
    /**
     * 校验属性不能为空
     */
    NotNull("{}不能为空"),
    /**
     * 校验属性必须是数值类型
     */
    Number("属性{}必须是数值类型，结果其值为{}"),
    Custom("");
    private String message;
    /**
     * @param message 没有通过校验时的默认提示信息
     */
    ValidTypeEnum(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
    	return message;
    }
}
