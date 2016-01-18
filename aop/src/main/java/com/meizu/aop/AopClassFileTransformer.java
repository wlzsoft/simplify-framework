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
 * 字节码编辑植入处理类
 * @author Administrator
 *java命令启用的参数： -javaagent:E:/workspace-new/simplify-framework/aop/target/aop-0.0.1-SNAPSHOT.jar -Daop.conf=E:/workspace-new/simplify-framework/aop/src/main/resources/aop.conf
 */
public class AopClassFileTransformer implements ClassFileTransformer {

    final static List<String> methodList = new ArrayList<String>();
    public AopClassFileTransformer(){
        String methodStr = Config.getUtil().getProperty("methodList");
        String[] it = methodStr.split(";");
    	for (String itor : it) {
    		methodList.add(itor); 
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
	        for(String method : methodList){
	            if (method.startsWith(className)){
                    String methodName = method.split(":")[1];
                    CtMethod ctmethod = ctclass.getDeclaredMethod(methodName);
                	ctmethod.addLocalVariable("startTime", CtClass.longType);
                	ctmethod.addLocalVariable("endTime", CtClass.longType);
                	ctmethod.insertBefore("startTime = System.currentTimeMillis();");
                	ctmethod.insertBefore("System.out.println(\"记录日志1\");");
                	ctmethod.insertAfter("endTime = System.currentTimeMillis();");
                	ctmethod.insertAfter("System.out.println(\"this method "+methodName+" cost:\" +(endTime - startTime) +\"ms.\");");
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
     * 在main方法执行后，执行本方法
     * @param agentArgs
     * @param inst
     */
    public static void agentmain (String agentArgs, Instrumentation inst) {
    	System.out.println("main方法启动了");
    }
    
    /**
     * 在main函数执行前，执行本方法
     * 添加新的字节码转换器，来修改字节码
     * @param options
     * @param ins
     */
    public static void premain(String options, Instrumentation ins) {
        ins.addTransformer(new AopClassFileTransformer());
    }
    
   

}
