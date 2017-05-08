package vip.simplify.dto;

import vip.simplify.ioc.enums.BeanTypeEnum;

import java.lang.reflect.Field;
import java.util.List;

/**
 * <p><b>Title:</b><i>Bean对象属性的元数据信息承载</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月24日 下午5:36:46</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月24日 下午5:36:46</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class AttributeMetaDTO {

    /**
     *
     * 用于指定具体实现类的实例名
     */
    private String name = "";

    /**
     *
     * 用于指定具体实现类的类型
     */
    private Class<?> type  = Object.class;

    /**
     *
     * 描述信息，用于描述具体bean的功能
     */
    private String desc = "";

    private Field field;

    /**
     *
     * 方法用途: 用于指定具体实现类的实例名<br>
     * 操作步骤: TODO<br>
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * 方法用途: 用于指定具体实现类的类型<br>
     * 操作步骤: TODO<br>
     * @return
     */
    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    /**
     *
     * 方法用途: 描述信息，用于描述具体bean的功能<br>
     * 操作步骤: TODO<br>
     * @return
     */
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     *
     * 方法用途: 获取Bean待注入的Field<br>
     * 操作步骤: 赛选出包含@Inject注解的属性或是BeanConfig注解中指定的属性 <br>
     * @return
     */
    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
