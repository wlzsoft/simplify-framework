package com.meizu.aop;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * <p><b>Title:</b><i>字节码编辑植入处理类</i></p>
 * <p>Desc: java命令启用的参数： -javaagent:E:/workspace-new/simplify-framework/aop/target/aop-0.0.1-SNAPSHOT.jar -Daop.conf=E:/workspace-new/simplify-framework/aop/src/main/resources/aop.properties</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月19日 上午10:13:47</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月19日 上午10:13:47</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class AopClassFileTransformer implements ClassFileTransformer {

    final static List<String> filterList = new ArrayList<String>();
    public AopClassFileTransformer(){
        String methodStr = Config.getUtil().getProperty("cacheInfos");
        String[] it = methodStr.split(";");
    	for (String itor : it) {
    		filterList.add(itor); 
		}
    }
	
    /**
     * 字节码加载到虚拟机前会进入这个方法
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
    	CtClass ctclass = buildClazz(className);
        try {
			return ctclass.toBytecode();
		} catch (IOException | CannotCompileException e) {
			e.printStackTrace();
		}
        
        return null;
    }


	/**
	 * 
	 * 方法用途: 第三方逻辑切入<br>
	 * 操作步骤: TODO<br>
	 * @param className
	 * @return
	 */
	public CtClass buildClazz(String className) {
		System.out.println(className);
		
        if(!className.startsWith("com/meizu/aop/service")){
        	return null;
        }
        
        if (className != null && className.indexOf("/") != -1) {
            className = className.replaceAll("/", ".");
        }
        try {
//        	通过类全路径名获取class字节码文件数据
        	CtClass ctclass = ClassPool.getDefault().get(className);
	        for(String method : filterList){
	            if (method.startsWith(className)){
                    String methodName = method.split(":")[1];
                    CtMethod ctmethod = ctclass.getDeclaredMethod(methodName);
                	ctmethod.addLocalVariable("startTime", CtClass.longType);
                	ctmethod.addLocalVariable("endTime", CtClass.longType);
                	ctmethod.insertBefore("startTime = System.currentTimeMillis();");
//                	ctmethod.insertBefore("System.out.println(\"记录日志1\");");
                	ctmethod.insertBefore("com.meizu.aop.IInterceptor.initBefore(\""+method+"\",this,$args);");
                	ctmethod.insertAfter("com.meizu.aop.IInterceptor.initAfter(\""+method+"\",this,$args);");
                	ctmethod.insertAfter("endTime = System.currentTimeMillis();");
                	ctmethod.insertAfter("System.out.println(\"方法 ["+className+":"+methodName+"] 调用花费的时间:\" +(endTime - startTime) +\"ms.\");");
	            }
	        }     
	        return ctclass;
        } catch (CannotCompileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
	}

    /**
     * 
     * 方法用途: 在main方法执行后，执行本方法<br>
     * 操作步骤: TODO<br>
     * @param agentArgs
     * @param inst
     */
    public static void agentmain (String agentArgs, Instrumentation inst) {
    	System.out.println("main方法启动了");
    }
    
    /**
     * 
     * 方法用途: 在main函数执行前，执行本方法<br>
     * 操作步骤: 添加新的字节码转换器，来修改字节码<br>
     * @param options
     * @param ins
     */
    public static void premain(String options, Instrumentation ins) {
        ins.addTransformer(new AopClassFileTransformer());
    }
    
   

}
