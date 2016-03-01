package com.meizu.simplify.aop;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
//import com.meizu.simplify.utils.log.util.DefaultLogManager;

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
	
//	private static final Logger LOGGER = DefaultLogManager.getLogger();
//	private static final Logger LOGGER = LoggerFactory.getLogger(AopClassFileTransformer.class);
	private class FilterMetaInfo {
		private String filterName;
		//默认不匹配
		private Boolean isMatch = false;
		public String getFilterName() {
			return filterName;
		}
		public void setFilterName(String filterName) {
			this.filterName = filterName;
		}
		public Boolean getIsMatch() {
			return isMatch;
		}
		public void setIsMatch(Boolean isMatch) {
			this.isMatch = isMatch;
		}
		
	}
    final static List<FilterMetaInfo> filterList = new ArrayList<>();
    private String injectionTargetClassPaths = null;
    public AopClassFileTransformer(){
        String methodStr = Config.getUtil().getProperty("cacheInfos");
        String[] it = methodStr.split(";");
    	for (String itor : it) {
    		FilterMetaInfo filterMetaInfo = new FilterMetaInfo();
    		filterMetaInfo.setFilterName(itor);
    		filterList.add(filterMetaInfo); 
		}
    	
    	injectionTargetClassPaths = Config.getUtil().getProperty("injectionTargetClassPaths");
    	if(injectionTargetClassPaths == null || injectionTargetClassPaths.equals("")) {
    		throw new RuntimeException("请检查aop.properties中injectionTargetClassPaths属性是否有设置");
    	}
    	
    }

	
	
    /**
     * 字节码加载到虚拟机前调用这个方法来修改字节码，达到aop织入
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
        
        for(FilterMetaInfo filterMetaInfo : filterList){
        	String classInfo = filterMetaInfo.getFilterName();
        	if (classInfo.startsWith(className)){
        		filterMetaInfo.setIsMatch(true);
//        		LOGGER.info("AOP：开始对类["+className+"]的相关方法进行逻辑切入");
        		System.out.println("AOP：开始对类["+className+"]的相关方法进行逻辑切入");
        		
        		String methodNameStr = classInfo.split(":")[1];
        		String[] methodArr = methodNameStr.split(",");
        		 try {
//        			CtClass对象调用writeFile()，toClass()或者toBytecode()转换成字节码，那么会冻结这个CtClass对象
//        			再设置ClassPool.doPruning=true，会在冻结对象的时候对这个对象进行精简
        			ClassPool.doPruning = true;//减少对象内存占用
//        			LOGGER.info("AOP：javasist开始精简["+className+"]对象字节码");
        			System.out.println("AOP：javasist开始精简["+className+"]对象字节码");
//        	                    通过类全路径名获取class字节码文件数据
        			ClassPool pool = ClassPool.getDefault();
//        			pool.insertClassPath(className);
//        			pool.insertClassPath(new ClassClassPath(this.getClass())); 
//        			pool.insertClassPath(new ByteArrayClassPath(name, b)); 
        			String[] targetClassPathArr = injectionTargetClassPaths.split(";");
        			for (String targetClassPath : targetClassPathArr) {
        				//Caused by: javassist.NotFoundException,注意：如果待修改的class字节码文件所依赖的其他字节码文件，如果不在classpath，会报这个异常，需要加入进来
        				//因为启动修改class文件时依赖他
        				pool.insertClassPath(targetClassPath); 
					}
//        			InputStream inputStream = null; 
//        			CtClass ctclass = pool.makeClass(inputStream); 
    	        	CtClass ctclass = pool.get(className);
//    	        	if(!ctclass.hasAnnotation(Bean.class)) {
//    	        		return ctclass;
//    	        	}
    		        for(String methodName : methodArr){
    		        	String methodFullName = className+":"+methodName;
//    		        	LOGGER.info("AOP：对方法["+methodFullName+"]进行逻辑切入");
    		        	System.out.println("AOP：对方法["+methodFullName+"]进行逻辑切入");
    	                    CtMethod ctmethod = ctclass.getDeclaredMethod(methodName);
    	                	ctmethod.addLocalVariable("startTime", CtClass.longType);
    	                	ctmethod.addLocalVariable("endTime", CtClass.longType);
//    	                	ctmethod.addParameter(type); //添加方法参数，并指定参数类型，可以是自定义类型
    	                	ctmethod.addLocalVariable("beforeObject",pool.get("java.lang.Object"));
//    	                	ctmethod.addLocalVariable("beforeObject",ctmethod.getReturnType());
    	                	ctmethod.addLocalVariable("ir",pool.get("com.meizu.simplify.aop.InterceptResult"));
    	                	//字节码植入，需要考虑分析 1.返回值转换的问题，2.是否有返回值的问题
    	                	String returnTypeName = ctmethod.getReturnType().getName();
    	                	StringBuilder builder = new StringBuilder();
    	                	builder.append("ir = new com.meizu.simplify.aop.InterceptResult();")
    	                		   .append("beforeObject = com.meizu.simplify.aop.IInterceptor.initBefore(\""+methodFullName+"\",ir,this,$args);");
							if(!returnTypeName.equals("void")) {
								builder.append("if(beforeObject != null) {")
								.append( "    return ("+returnTypeName+")beforeObject;")
							    .append( "}");
							}
							 
            			    ctmethod.insertBefore(builder.toString());
    	                	ctmethod.insertBefore("startTime = System.currentTimeMillis();");
    	                	ctmethod.insertAfter("com.meizu.simplify.aop.IInterceptor.initAfter(\""+methodFullName+"\",ir,this,$args);");
    	                	ctmethod.insertAfter("endTime = System.currentTimeMillis();");
    	                	ctmethod.insertAfter("System.out.println(\"方法 ["+methodFullName+"] 调用花费的时间:\" +(endTime - startTime) +\"ms.\");");
    		        }
    		        return ctclass;
    	        } catch (CannotCompileException e) {
    	            e.printStackTrace();
    	            System.out.println("framework:buildClazz");
    	        } catch (NotFoundException e) {
    	            e.printStackTrace();
    	            System.out.println("framework:NotFound(找不到相关class文件):1.请检查aop.properties中injectionTargetClassPaths属性是否有设置有误");
    	        }
        		break;
        	}
        }
//      printAopMappingInfo();
        return null;
	}

    
    /**
     * 
     * 方法用途: 在main函数执行前，执行本方法<br>
     * 操作步骤: 添加新的字节码转换器，来修改字节码<br>
     * @param agentArgs
     * @param ins
     */
    public static void premain(String agentArgs, Instrumentation ins) {
        ins.addTransformer(new AopClassFileTransformer());
      
    }
    
    /**
     * 
     * 方法用途: 打印Aop映射信息<br>
     * 操作步骤: 用于输出aop.properties文件中配置的切面类，是否被注入业务逻辑<br>
     */
    public static void printAopMappingInfo() {
        String messageFilter = "注意：以下信息在aop.properties中配置的拦截信息没有被注入，请检查是否有配置有误：\r\n";
        boolean isNoMatch = false;
        for(FilterMetaInfo filterMetaInfo : filterList){
        	if(!filterMetaInfo.getIsMatch()) {
        		isNoMatch = true;
        		messageFilter += filterMetaInfo.getFilterName();
        	}
        }
        if(isNoMatch) {
        	System.out.println(messageFilter);
        }
    }
   

}
