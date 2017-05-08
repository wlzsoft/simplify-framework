package vip.simplify.dto;

import vip.simplify.ioc.enums.BeanTypeEnum;

import java.lang.reflect.Field;
import java.util.List;

/**
 * <p><b>Title:</b><i>Bean对象元数据信息承载</i></p>
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
public class BeanMetaDTO {


    private BeanTypeEnum type = BeanTypeEnum.SINGLE;

    private String value = "";

    private String sourceName;

    private List<AttributeMetaDTO> attributeMetaDTOList;

    /**
     *
     * 方法用途: 元数据来源：注解，配置(xml.json.yml,properties等等)，类对象<br>
     * 操作步骤: TODO<br>
     * @return
     */
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    /**
     *
     * 方法用途: 指定bean的类型<br>
     * 操作步骤: 目前有单例和多例两种方式<br>
     * @return
     */
    public BeanTypeEnum getType() {
        return type;
    }

    public void setType(BeanTypeEnum type) {
        this.type = type;
    }

    /**
     *
     * 方法用途: 指定创建的唯一标识，bean的标识名<br>
     * 操作步骤: 创建bean的时候标识起来，可用于获取bean对象<br>
     * @return
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<AttributeMetaDTO> getAttributeMetaDTOList() {
        return attributeMetaDTOList;
    }

    public void setAttributeMetaDTOList(List<AttributeMetaDTO> attributeMetaDTOList) {
        this.attributeMetaDTOList = attributeMetaDTOList;
    }
}
