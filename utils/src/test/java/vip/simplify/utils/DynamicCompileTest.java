package vip.simplify.utils;
/**
  * <p><b>Title:</b><i>java动态编译</i></p>
 * <p>Desc: 可用于系统启动时的框架级修改，启动成功后的修改，除非是插件模式的实现，只为集成插件库</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月4日 下午4:45:49</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月4日 下午4:45:49</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
 
public class DynamicCompileTest {
     
    public static void main(String[] args) throws IOException, URISyntaxException {
    	DynamicCompileTest.execute("public void test() {System.out.println(\"dynamicComplieTest\");}");
    }
 
    public static String execute(String code) throws IOException,
            URISyntaxException {
        String source = "public class Main {"+code+"}";
 
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(
                null, null, null);
        StringSourceJavaObject sourceObject = new DynamicCompileTest.StringSourceJavaObject("Main", source);
        List<StringSourceJavaObject> fileObjects = Arrays.asList(sourceObject);
        CompilationTask task = compiler.getTask(null, fileManager, null, null,
                null, fileObjects);
 
        boolean result = task.call();
        if (result) {
            System.out.println("Compile succeeded!");
        } else {
            System.out.println("Compile failed!");
        }
 
        // 运行程序
        Runtime run = Runtime.getRuntime();
        Process process = run.exec("java -cp ./ Main");
        InputStream in = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String info = "";
        StringBuffer bu = new StringBuffer();
        while ((info = reader.readLine()) != null) {
            bu.append(info);
        }
         
        return bu.toString();
    }
 
    static class StringSourceJavaObject extends SimpleJavaFileObject {
 
        private String content = null;
 
        public StringSourceJavaObject(String name, String content)
                throws URISyntaxException {
            super(URI.create("string:///" + name.replace('.', '/')
                    + Kind.SOURCE.extension), Kind.SOURCE);
            this.content = content;
        }
 
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors)
                throws IOException {
            return content;
        }
    }
}