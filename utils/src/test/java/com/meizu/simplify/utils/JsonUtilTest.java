package com.meizu.simplify.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

	private static String path = null;
	@BeforeClass
	public static void init() {
		path  = JsonUtilTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	}
	@Test
	public void testSerialForStreamApi() throws IOException {
		FileWriter fw = new FileWriter(path+"tmp/huge2.json");
		JSONWriter writer = new JSONWriter(fw);
		  writer.startArray();
		  for (int i = 0; i < 1000 * 1000; ++i) {
		        writer.writeValue(new BigVo(i));
		  }
		  writer.endArray();
		  writer.close();
		  fw.close();
		  FileUtil.deleteFile(path+"tmp/huge2.json");
	}
	
	@Test
	public void testSerialForStreamApi2() throws IOException {
		FileWriter fw = new FileWriter(path+"tmp/huge2.json");
		 JSONWriter writer = new JSONWriter(fw);
		  writer.startObject();
		  for (int i = 0; i < 1000 * 1000; ++i) {
		        writer.writeKey("x" + i);
		        writer.writeValue(new BigVo(i));
		  }
		  writer.endObject();
		  writer.close();
		  fw.close();
		  FileUtil.deleteFile(path+"tmp/huge2.json");
	}
	
	@Test
	public void testUnSerialForStreamApi() throws FileNotFoundException {
		JSONReader reader = new JSONReader(new FileReader(path+"tmp/huge.json"));
		  reader.startArray();
		  while(reader.hasNext()) {
		        BigVo vo = reader.readObject(BigVo.class);
		        vo.getAuc();
		  }
		  reader.endArray();
		  reader.close();
	}
	
	/**
	 * 方法用途: 数据格式必须是json对象<br>
	 * 操作步骤: 废弃，huge.json是数组格式<br>
	 * @throws FileNotFoundException
	 */
//	@Test
	public void testUnSerialForStreamApi2() throws FileNotFoundException {
		JSONReader reader = new JSONReader(new FileReader(path+"tmp/huge.json"));
		  reader.startObject();
		  while(reader.hasNext()) {
//		        String key = reader.readString();
		        BigVo vo = reader.readObject(BigVo.class);
		        vo.getAuc();
		  }
		  reader.endObject();
		  reader.close();
	}
	
	@Test
	public void testArray() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		ja.add("2016-10-01 10:00:00");
		ja.add("2016-10-01 12:00:00");
		jo.put("1", ja);
		JSONArray ja2 = new JSONArray();
		ja2.add("2016-10-01 14:00:00");
		ja2.add("2016-10-01 15:00:00");
		jo.put("2-3", ja2);
		System.out.println(jo.toJSONString());
		JSONObject o = JsonUtil.stringToJSONObject(jo.toJSONString());
		System.out.println(o);
	}
}
