package com.meizu.simplify.datasource.config.pojo;

import java.util.List;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月10日 上午11:55:49</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月10日 上午11:55:49</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class HostSwitch {
	
    private String name;
    
    private List<MasterHost> masterHostList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public List<MasterHost> getMasterHostList() {
		return masterHostList;
	}

	public void setMasterHostList(List<MasterHost> masterHostList) {
		this.masterHostList = masterHostList;
	}
    
}