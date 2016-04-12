
package com.meizu.simplify.utils.collection;

/**
 * <p><b>Title:</b><i>两个对象比较回调接口</i></p>
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
 * @param <T>
 * @param <W>
 */
public interface IEqualCallBack<T,W> {
    /**
     * 
     * 方法用途: 对象比较<br>
     * 操作步骤: TODO<br>
     * @param o
     * @param w
     * @return
     */
    public boolean equal(T o,W w);
}
