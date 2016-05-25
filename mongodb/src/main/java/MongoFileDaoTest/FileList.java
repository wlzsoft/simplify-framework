package MongoFileDaoTest;

import com.mongodb.Block;
import com.mongodb.client.gridfs.model.GridFSFile;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月25日 上午10:20:33</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月25日 上午10:20:33</p>
 * @author <a href="mailto:meizu@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 0.1
 *
 */
public class FileList implements Block<GridFSFile> {

	@Override
	public void apply(GridFSFile t) {
		
	}

}
