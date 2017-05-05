package vip.simplify.utils.clazz;

import java.util.List;

/**
 * <p><b>Title:</b><i>Class对象相关的信息</i></p>
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
public class ClassInfo<T> {
    private List<T> infoList;
    private Class<?> clazz;

    public List<T> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<T> infoList) {
        this.infoList = infoList;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
