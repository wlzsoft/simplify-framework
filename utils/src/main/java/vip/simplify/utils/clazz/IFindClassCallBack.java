
package vip.simplify.utils.clazz;

import java.util.List;

/**
 * <p><b>Title:</b><i>Class查找结果回调接口</i></p>
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
public interface IFindClassCallBack<T> {
    /**
     *
     * 方法用途: Class查找结果解析<br>
     * 操作步骤: TODO<br>
     * @param c
     * @return
     */
    public List<T> resolve(Class<?> c);
}
