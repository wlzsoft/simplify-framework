package vip.simplify.entity;

import java.io.Serializable;

import vip.simplify.entity.annotations.Key;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年6月3日 上午11:10:11</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年6月3日 上午11:10:11</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 * @param <T>
 * @param <PK>
 */
public class IdEntity<T,PK extends Serializable> implements Serializable,AutoCloseable{
	//@Transient
	private static final long serialVersionUID = -3333906406099241984L;
//	@NotNull
//	@Column("id")
	@Key
	private Integer fid;
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	@Override
	public void close() throws Exception {//TODO 需要做严格测试，是否被调用
		System.out.println(this.getClass().getName()+":"+this.fid+">> 已经销毁");
	}
	
}
