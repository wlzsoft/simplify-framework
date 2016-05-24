package com.meizu.simplify.aop;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.CollectionUtil;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.utils.collection.IEqualCallBack;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * <p><b>Title:</b><i>字节码编辑植入处理类</i></p>
 * <p>Desc: java命令启用的参数： -javaagent:E:/workspace-new/simplify-framework/aop/target/weaving-1.0.1-SNAPSHOT.jar -Daop.properties=E:/workspace-new/simplify-framework/aop/src/main/resources/aop.properties</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月19日 上午10:13:47</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月19日 上午10:13:47</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class AopClassFileTransformer implements ClassFileTransformer {
	
//	private static final Logger LOGGER = DefaultLogManager.getLogger();
	private class FilterMetaInfo {
		private String filterClassName;
		private String[] methodNameArr;
		//默认不匹配
		private Boolean isMatch = false;
		public String getFilterClassName() {
			return filterClassName;
		}
		public void setFilterClassName(String filterClassName) {
			this.filterClassName = filterClassName;
		}
		public String[] getMethodNameArr() {
			return methodNameArr;
		}
		public void setMethodNameArr(String[] methodNameArr) {
			this.methodNameArr = methodNameArr;
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
    	//1.配置文件指定用于织入的方法
        String methodStr = AopConfig.getUtil().getProperty("filterInfos");
        if(StringUtil.isNotBlank(methodStr)) {
        	String[] it = methodStr.split(";");
        	for (String itor : it) {
        		FilterMetaInfo filterMetaInfo = new FilterMetaInfo();
        		String className = itor.split(":")[0];
        		String methodNameStr = itor.split(":")[1];
        		String[] methodArr = methodNameStr.split(",");
        		filterMetaInfo.setFilterClassName(className);
        		filterMetaInfo.setMethodNameArr(methodArr);
        		addFilterMethod(filterMetaInfo);
        	}
        }
    	
    	//2.需要扫描的用于织入的标准了注解的信息的方法
    	String injectionTargetAnnotation = AopConfig.getUtil().getProperty("injectionTargetAnnotation");
    	if(StringUtil.isBlank(injectionTargetAnnotation)) {
    		if(filterList.size()<1) {
    			throw new RuntimeException("请检查aop.properties中filterInfos和injectionTargetAnnotation属性的设置，两个属性必须设置其中一个");
    		}
    	}
    	String[] injectionTargetAnnotationArr = injectionTargetAnnotation.split(",");
    	List<Class<?>> listClazz = ClassUtil.findClasses("com.meizu.demo");//不要引入Classutil类了，直接通过模版形式，生成配置文件
    	for (Class<?> classInfo : listClazz) {
    		try {
    			List<String> methodTempStr = new ArrayList<>();
    			for (String injectionTargetAnno : injectionTargetAnnotationArr) {
    				Class<Annotation> annoClass = (Class<Annotation>) Class.forName(injectionTargetAnno);
    				Method[] methodArr = classInfo.getMethods();
    				for (Method method : methodArr) {
						Annotation anno = method.getAnnotation(annoClass);
						if(anno!=null) {
							methodTempStr.add(method.getName());
						}
					}
				}
    			if(methodTempStr.size()>0) {
    				FilterMetaInfo filterMetaInfo = new FilterMetaInfo();
    				filterMetaInfo.setFilterClassName(classInfo.getName());
    				filterMetaInfo.setMethodNameArr(methodTempStr.toArray(new String[methodTempStr.size()]));
    				addFilterMethod(filterMetaInfo);
    			}
    		} catch (ClassNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		}
    	
    	injectionTargetClassPaths = AopConfig.getUtil().getProperty("injectionTargetClassPaths");
    	if(injectionTargetClassPaths == null || injectionTargetClassPaths.equals("")) {
    		throw new RuntimeException("请检查aop.properties中injectionTargetClassPaths属性是否有设置");
    	}
    	
    }



	private void addFilterMethod(FilterMetaInfo filterMetaInfo) {
		boolean isContain = CollectionUtil.contains(filterList, filterMetaInfo,new IEqualCallBack<FilterMetaInfo,FilterMetaInfo>(){
			@Override
			public boolean equal(FilterMetaInfo o, FilterMetaInfo w) {
				if(o.getFilterClassName().equals(w.getFilterClassName())) {
					return true;
				}
				return false;
			}
			});
		if(!isContain) {
			filterList.add(filterMetaInfo); 
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
//        	 printAopMappingInfo();
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
        	if (filterMetaInfo.getFilterClassName().equals(className)){
        		filterMetaInfo.setIsMatch(true);
        		CtClass ctclass = embed(className, filterMetaInfo.getMethodNameArr());
        		return ctclass;
        	}
        }
        return null;
	}



	private CtClass embed(String className, String[] methodArr) {
		try {
//    		LOGGER.info("AOP：开始对类["+className+"]的相关方法进行逻辑切入");
    		System.out.println("AOP：开始对类["+className+"]的相关方法进行逻辑切入");
//        	CtClass对象调用writeFile()，toClass()或者toBytecode()转换成字节码，那么会冻结这个CtClass对象
//        	再设置ClassPool.doPruning=true，会在冻结对象的时候对这个对象进行精简
			ClassPool.doPruning = true;//减少对象内存占用
//        	LOGGER.info("AOP：javasist开始精简["+className+"]对象字节码");
			System.out.println("AOP：javasist开始精简["+className+"]对象字节码");
//        	通过类全路径名获取class字节码文件数据
			ClassPool pool = ClassPool.getDefault();
//        	pool.insertClassPath(new ClassClassPath(this.getClass())); 
//        	pool.insertClassPath(new ByteArrayClassPath(name, b)); 
//        	pool.insertClassPath("com.meizu.simplify.aop.InterceptResult");
			String[] targetClassPathArr = injectionTargetClassPaths.split(";");
			for (String targetClassPath : targetClassPathArr) {
				//Caused by: javassist.NotFoundException,注意：如果待修改的class字节码文件所依赖的其他字节码文件，如果不在classpath，会报这个异常，需要加入进来
				//因为启动修改class文件时依赖他
				pool.insertClassPath(targetClassPath); 
			}
//        	InputStream inputStream = null; 
//        	CtClass ctclass = pool.makeClass(inputStream); 
			CtClass ctclass = pool.get(className);
//    	    if(!ctclass.hasAnnotation(Bean.class)) {
//    	    	return ctclass;
//    	    }
		    for(String methodName : methodArr){
		    	String methodFullName = className+":"+methodName;
//    		    LOGGER.info("AOP：对方法["+methodFullName+"]进行逻辑切入");
		    	System.out.println("AOP：对方法["+methodFullName+"]进行逻辑切入");
		        CtMethod ctmethod = ctclass.getDeclaredMethod(methodName);
		    	ctmethod.addLocalVariable("startTime", CtClass.longType);
		    	ctmethod.addLocalVariable("endTime", CtClass.longType);
//    	        ctmethod.addParameter(type); //添加方法参数，并指定参数类型，可以是自定义类型
		    	ctmethod.addLocalVariable("beforeObject",pool.get("java.lang.Object"));
//    	        ctmethod.addLocalVariable("beforeObject",ctmethod.getReturnType());
		    	ctmethod.addLocalVariable("ir",pool.get("com.meizu.simplify.aop.InterceptResult"));
		    	//字节码植入，需要考虑分析 1.返回值转换的问题，2.是否有返回值的问题
		    	String returnTypeName = ctmethod.getReturnType().getName();
		    	StringBuilder builder = new StringBuilder();
		    	builder.append("ir = new com.meizu.simplify.aop.InterceptResult();")
		    		   .append("beforeObject = com.meizu.simplify.aop.IInterceptor.initBefore(\""+methodFullName+"\",ir,this,$args);");
				if(!returnTypeName.equals("void")) {
					builder.append("if(beforeObject != null) {")
					       .append("    return ("+returnTypeName+")beforeObject;")
				           .append("}");
				}
				 
			    ctmethod.insertBefore(builder.toString());
		    	ctmethod.insertBefore("startTime = java.time.Instant.now().getNano();");
		    	ctmethod.insertAfter("com.meizu.simplify.aop.IInterceptor.initAfter(\""+methodFullName+"\",ir,this,$args);");
		    	ctmethod.insertAfter("endTime = java.time.Instant.now().getNano();");
		    	ctmethod.insertAfter("System.out.println(\"方法 ["+methodFullName+"] 调用花费的时间:\" +(endTime - startTime)/10000000 +\"毫秒.\");");
		    }
		    return ctclass;
		} catch (CannotCompileException e) {
		    e.printStackTrace();
		    System.out.println("framework:buildClazz");
		} catch (NotFoundException e) {
		    e.printStackTrace();
		    System.out.println("framework:NotFound(找不到相关class文件):1.请检查aop.properties中injectionTargetClassPaths属性是否有设置有误");
		}
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
        AopClassFileTransformer.printAopMappingInfo();
      
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
        		messageFilter += filterMetaInfo.getFilterClassName();
        		String[] methodNameArr = filterMetaInfo.getMethodNameArr();
        		for (String methodName : methodNameArr) {
        			messageFilter += methodName;
				}
        	}
        }
        if(isNoMatch) {
        	System.out.println(messageFilter);
        }
    }
   

}
