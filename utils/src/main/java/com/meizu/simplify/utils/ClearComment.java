package com.meizu.simplify.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p><b>Title:</b><i>清除注释</i></p>
 * <p>Desc: 支持格式：
        </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年4月3日 下午4:04:30</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年4月3日 下午4:04:30</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class ClearComment {

	/**
	 * 
	 * 方法用途: 清除指定目录下的所有文件的注释信息，包含程序源码<br>
	 * 操作步骤: 可清除源码中的注释信息<br>
	 * @param rootDir
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void deepDir(String rootDir) throws FileNotFoundException,
			UnsupportedEncodingException {
		File folder = new File(rootDir);
		if (folder.isDirectory()) {
			String[] files = folder.list();
			for (int i = 0; i < files.length; i++) {
				File file = new File(folder, files[i]);
				if (file.isDirectory() && file.isHidden() == false) {
					System.out.println(file.getPath());
					deepDir(file.getPath());
				} else if (file.isFile()) {
					clearFile(file.getPath());
				}
			}
		} else if (folder.isFile()) {
			clearFile(folder.getPath());
		}
	}

	/**
	 * 
	 * 方法用途: 清除指定文件的注释信息<br>
	 * 操作步骤: TODO<br>
	 * @param filePathAndName 当前文件名
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void clearFile(String filePathAndName)
			throws FileNotFoundException, UnsupportedEncodingException {
		StringBuffer buffer = new StringBuffer();
		String line = null; // 用来保存每行读取的内容
		InputStream is = new FileInputStream(filePathAndName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));
		try {
			line = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 读取第一行
		while (line != null) { // 如果 line 为空说明读完了
			buffer.append(line); // 将读到的内容添加到 buffer 中
			buffer.append("\r\n"); // 添加换行符
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			} // 读取下一行
		}
		try {
			reader.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String filecontent = buffer.toString();
		filecontent = clear(filecontent);
		// 再输出到原文件
		try {
			File f = new File(filePathAndName);
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			FileOutputStream out = new FileOutputStream(filePathAndName);
			byte[] bytes = filecontent.getBytes("UTF-8");
			out.write(bytes);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 方法用途: 清除注释<br>
	 * 操作步骤: 1.清除‘//’
	 *        2.清除‘\/**\/’
	 *        3.清除‘#’开头的
	 *        4.。。。。。<br>
	 * @param filecontent 
	 * @return 
	 */
	public static String clear(String filecontent) {
		// 1、清除单行的注释，如： //某某，正则为 ：\/\/.*
		// 2、清除单行的注释，如：/** 某某 */，正则为：\/\*\*.*\*\/
		// 3、清除单行的注释，如：/* 某某 */，正则为：\/\*.*\*\/
		// 4、清除多行的注释，如:
		// /* 某某1
		// 某某2
		// */
		// 正则为：.*/\*(.*)\*/.*
		// 5、清除多行的注释，如：
		// /** 某某1
		// 某某2
		// */
		// 正则为：/\*\*(\s*\*\s*.*\s*?)*
		// /** 
		// 某某2
		// */
		//正则为： /\\*(\\s*\\*\\s*.*\\s*?)*\\*\\/

		Map<String, String> patterns = new HashMap<String, String>();
		patterns.put("([^:])\\/\\/.*", "$1");// 匹配在非冒号后面的注释，此时就不到再遇到http://
		patterns.put("\\s+\\/\\/.*", "");// 匹配“//”前是空白符的注释 1。不会删除掉 http://的内容
//		patterns.put("//[\\s\\S]*?\\n", "");// 匹配“//”前是空白符的注释 2。会删除掉 http://的内容
		patterns.put("/\\*{1,2}[\\s\\S]*?\\*/", "");//匹配 /* */
		patterns.put("<!-[\\s\\S]*?-->", "");//删除xml和html注释：<!-[\s\S]*?--> <!-- -->
		patterns.put("^\\/\\/.*", "");
		patterns.put("^\\/\\*\\*.*\\*\\/$", "");
		patterns.put("\\/\\**(\\s*\\*\\s*.*\\s*?)*\\*\\/", "");
		//删除空白行：^\s*\n
		Iterator<String> keys = patterns.keySet().iterator();
		String key = null, value = "";
		while (keys.hasNext()) {
			// 经过多次替换
			key = keys.next();
			value = patterns.get(key);
			filecontent = StringUtils.replaceAll(filecontent, key, value);
		}
		return filecontent;
	}

}
