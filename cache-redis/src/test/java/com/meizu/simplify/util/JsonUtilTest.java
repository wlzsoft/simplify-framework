package com.meizu.simplify.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.JSONWriter;

/**
  * <p><b>Title:</b><i>测试fastjson基于stream的api对于超大文本的处理速度</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月31日 下午3:14:56</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月31日 下午3:14:56</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class JsonUtilTest {

	@Test
	public void testSerialForStreamApi() throws IOException {
		JSONWriter writer = new JSONWriter(new FileWriter("/tmp/huge.json"));
		  writer.startArray();
		  for (int i = 0; i < 1000 * 1000; ++i) {
		        writer.writeValue(new BigVo());
		  }
		  writer.endArray();
		  writer.close();
	}
	
	@Test
	public void testSerialForStreamApi2() throws IOException {
		 JSONWriter writer = new JSONWriter(new FileWriter("/tmp/huge.json"));
		  writer.startObject();
		  for (int i = 0; i < 1000 * 1000; ++i) {
		        writer.writeKey("x" + i);
		        writer.writeValue(new BigVo());
		  }
		  writer.endObject();
		  writer.close();
	}
	
	@Test
	public void testUnSerialForStreamApi() throws FileNotFoundException {
		JSONReader reader = new JSONReader(new FileReader("/tmp/huge.json"));
		  reader.startArray();
		  while(reader.hasNext()) {
		        BigVo vo = reader.readObject(BigVo.class);
		        // handle vo ...
		  }
		  reader.endArray();
		  reader.close();
	}
	
	@Test
	public void testUnSerialForStreamApi2() throws FileNotFoundException {
		JSONReader reader = new JSONReader(new FileReader("/tmp/huge.json"));
		  reader.startObject();
		  while(reader.hasNext()) {
		        String key = reader.readString();
		        BigVo vo = reader.readObject(BigVo.class);
		        // handle vo ...
		  }
		  reader.endObject();
		  reader.close();
	}
}
