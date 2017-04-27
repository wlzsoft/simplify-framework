package vip.simplify.datasource.config.pojo;

import java.util.List;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月10日 上午11:55:53</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月10日 上午11:55:53</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class MasterHost {
	
    private String name;
    
    private String url;
    
    private String userName;
    
    private String password;
    
    private List<SlaveHost> slaveHostList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public List<SlaveHost> getSlaveHostList() {
		return slaveHostList;
	}

	public void setSlaveHostList(List<SlaveHost> slaveHostList) {
		this.slaveHostList = slaveHostList;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}