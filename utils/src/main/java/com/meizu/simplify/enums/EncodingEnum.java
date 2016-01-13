package com.meizu.simplify.enums;

/**
 * <p><b>Title:</b><i>常用编码格式常量</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 下午2:38:24</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 下午2:38:24</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 */
public enum EncodingEnum {
	UTF_8("UTF-8",1),
	GBK("GBK",2);
	
	private EncodingEnum(String name, int index) {
		this.name = name;
        this.index = index;
	}
	
	private String name;
	
    private int index;
    
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
    public String getName() {
    	return name;
    }
    
    public String toString() {
		return name;
    }
    
    /**
     * 
     * 方法用途: 通过索引值获取编码名称<br>
     * 操作步骤: TODO<br>
     * @param index
     * @return
     */
    public static String getName(int index) {
        for (EncodingEnum c : EncodingEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
   
}
 