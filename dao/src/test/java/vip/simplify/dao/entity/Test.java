package vip.simplify.dao.entity;

import vip.simplify.entity.BaseEntity;
import vip.simplify.entity.annotations.Entity;
import vip.simplify.entity.annotations.Table;
import vip.simplify.entity.annotations.Transient;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月28日 下午6:02:55</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月28日 下午6:02:55</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Entity
@Table(name="test_web")
@Transient({"delFlag","deleteflag"})
public class Test extends BaseEntity {

	private static final long serialVersionUID = 246628316050179125L;
	private String name;
	private String url;
	// mysql5.1.73开始的时候 type bit length=1，bit类型不同于int系列类型，是动态空间占用的，最大值64的长度，也就是64位，这个字段类型可以用于权限系统的存储
	private Boolean status;
	// mysql 字段类型为bit， length=3 其实只要length大于1，就是必须使用byte数组接收
    //select * from test_web where type = 7 or select * from test_web where type = b'111' 这样才能查询到值
	private byte[] type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public byte[] getType() {
        return type;
    }

    public void setType(byte[] type) {
        this.type = type;
    }
}
