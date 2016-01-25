package com.meizu.aop;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.ioc.annotation.Bean;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * <p><b>Title:</b><i>字节码编辑植入处理类</i></p>
 * <p>Desc: java命令启用的参数： -javaagent:E:/workspace-new/simplify-framework/aop/target/aop-0.0.1-SNAPSHOT.jar -Daop.properties=E:/workspace-new/simplify-framework/aop/src/main/resources/aop.properties</p>
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

	private static final Logger LOGGER = LoggerFactory.getLogger(AopClassFileTransformer.class);
    final static List<String> filterList = new ArrayList<String>();
    public AopClassFileTransformer(){
        String methodStr = Config.getUtil().getProperty("cacheInfos");
        String[] it = methodStr.split(";");
    	for (String itor : it) {
    		filterList.add(itor); 
		}
    	//初始化连接池  TODO 需要迁移到非javaagent的位置
//    	ShardedJedisPool pool = RedisPool.init("redis_ref_hosts");
//    	for(int i=0; i<10; i++) {
//    		pool.getResource();
//    	}
//        LOGGER.info("当前redis连接池状态：NumActive:"+pool.getNumActive()+"NumIdle:"+pool.getNumIdle()+"NumWaiters:"+pool.getNumWaiters());
//        pool.returnResourceObject(resource);
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
			System.out.println("framework:transform");
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
		
		if (className == null || className.indexOf("/") == -1) {
			return null;
		}
		
        if(!className.startsWith("com/meizu")){
        	return null;
        }
        className = className.replaceAll("/", ".");
        
        for(String classInfo : filterList){
        	if (classInfo.startsWith(className)){
        		LOGGER.debug("AOP：开始对类["+className+"]的相关方法进行逻辑切入");
        		String methodNameStr = classInfo.split(":")[1];
        		String[] methodArr = methodNameStr.split(",");
        		 try {
//        			CtClass对象调用writeFile()，toClass()或者toBytecode()转换成字节码，那么会冻结这个CtClass对象
//        			再设置ClassPool.doPruning=true，会在冻结对象的时候对这个对象进行精简
        			ClassPool.doPruning = true;//减少对象内存占用
        			LOGGER.debug("AOP：javasist开始精简["+className+"]对象字节码");
//        	                    通过类全路径名获取class字节码文件数据
        			ClassPool pool = ClassPool.getDefault();
//        			pool.insertClassPath(className);
//        			pool.insertClassPath(new ClassClassPath(this.getClass())); 
//        			pool.insertClassPath("E:/workspace-new/demo/target/classes"); 
//        			pool.insertClassPath(new ByteArrayClassPath(name, b)); 
//        			InputStream ins = null; 
//        			CtClass ctclass = pool.makeClass(ins); 
    	        	CtClass ctclass = pool.get(className);
//    	        	if(!ctclass.hasAnnotation(Bean.class)) {
//    	        		return ctclass;
//    	        	}
    		        for(String methodName : methodArr){
    		        	String methodFullName = className+":"+methodName;
    		        	LOGGER.debug("AOP：对方法["+methodFullName+"]进行逻辑切入");
    	                    CtMethod ctmethod = ctclass.getDeclaredMethod(methodName);
    	                	ctmethod.addLocalVariable("startTime", CtClass.longType);
    	                	ctmethod.addLocalVariable("endTime", CtClass.longType);
    	                	ctmethod.insertBefore("com.meizu.aop.IInterceptor.initBefore(\""+methodFullName+"\",this,$args);");
    	                	ctmethod.insertBefore("startTime = System.currentTimeMillis();");
    	                	ctmethod.insertAfter("com.meizu.aop.IInterceptor.initAfter(\""+methodFullName+"\",this,$args);");
    	                	ctmethod.insertAfter("endTime = System.currentTimeMillis();");
    	                	ctmethod.insertAfter("System.out.println(\"方法 ["+methodFullName+"] 调用花费的时间:\" +(endTime - startTime) +\"ms.\");");
    		        }
    		        return ctclass;
    	        } catch (CannotCompileException e) {
    	            // TODO Auto-generated catch block
    	            e.printStackTrace();
    	            System.out.println("framework:buildClazz");
    	        } catch (NotFoundException e) {
    	            // TODO Auto-generated catch block
    	            e.printStackTrace();
    	        }
        		break;
        	}
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
