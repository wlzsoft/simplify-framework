package com.meizu.simplify.aop;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import com.meizu.simplify.Constants;
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
 * <p>Desc: java命令启用的参数： -javaagent:E:/workspace-new/simplify-framework/aop/target/weaving-1.2.0-SNAPSHOT.jar -Daop.properties=E:/workspace-new/simplify-framework/aop/src/main/resources/aop.properties</p>
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
	
	//private static final Logger LOGGER = DefaultLogManager.getLogger();
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
	public ClassPool pool = ClassPool.getDefault();
    final static List<FilterMetaInfo> filterList = new ArrayList<>();
    private String injectionTargetClassPaths = null;
    private String[] injectionTargetAnnotationArr = {Constants.packagePrefix+".simplify.cache.annotation.CacheDataSearch",
										    		Constants.packagePrefix+".simplify.cache.annotation.CacheDataDel",
										    		Constants.packagePrefix+".simplify.cache.annotation.CacheDataAdd",
										    		Constants.packagePrefix+".simplify.dao.annotations.Transation"};
    
    /**
     * 默认构建方法：javaagent只绑定一个实例,方法只调用一次
     */
    public AopClassFileTransformer(){
//    	一.配置读取
    	//1.配置文件指定用于织入的方法
        String methodStr = AopConfig.getUtil().getProperty("filterInfos");
        if(StringUtil.isNotBlank(methodStr)) {
        	String[] it = methodStr.split(";");
        	for (String itor : it) {
        		FilterMetaInfo filterMetaInfo = new FilterMetaInfo();
        		String className = itor.split(":")[0];
        		String methodNameStr = itor.split(":")[1];
        		String[] methodArr = methodNameStr.split(",");
        		List<String> methodList = CollectionUtil.duplicate(methodArr);
        		methodArr = methodList.toArray(new String[methodList.size()]);
        		filterMetaInfo.setFilterClassName(className);
        		filterMetaInfo.setMethodNameArr(methodArr);
        		addFilterMethod(filterMetaInfo);
        	}
        }
        
        //2.需要扫描的用于织入的标准了注解的信息的方法
    	String injectionTargetAnnotation = AopConfig.getUtil().getProperty("injectionTargetAnnotation");
    	if(injectionTargetAnnotation != null) {
    		injectionTargetAnnotationArr = injectionTargetAnnotation.split(",");
    	}
    	
    	injectionTargetClassPaths = AopConfig.getUtil().getProperty("injectionTargetClassPaths");
    	try {
    		//二.织入初始化
			//0.对类进行精简
			//CtClass对象调用writeFile()，toClass()或者toBytecode()转换成字节码，那么会冻结这个CtClass对象
			//再设置ClassPool.doPruning=true，会在冻结对象的时候对这个对象进行精简
			ClassPool.doPruning = true;//减少对象内存占用
			//1.设置切入类的classpath
			if(StringUtil.isBlank(injectionTargetClassPaths)) {
				String jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
				jarPath = jarPath.replace("weaving", "aop");
				pool.insertClassPath(jarPath); 
			} else {
				String[] targetClassPathArr = injectionTargetClassPaths.split(";");
				for (String targetClassPath : targetClassPathArr) {//通过类全路径名[包名.类名]获取class字节码文件数据
					//Caused by: javassist.NotFoundException,注意：如果待修改的class字节码文件所依赖的其他字节码文件，如果不在classpath，会报这个异常，需要加入进来,因为启动修改class文件时依赖他
					pool.insertClassPath(targetClassPath); 
				}
			}
		} catch (NotFoundException e) {
		    e.printStackTrace();
		    System.out.println("framework:NotFound(找不到相关class文件):1.请检查aop.properties中injectionTargetClassPaths属性是否有设置有误");
		}
    }

    /**
	 * 方法用途: 添加需要过滤的方法到集合中，并且去重<br>
	 * 操作步骤: TODO<br>
	 * @param filterMetaInfo
	 */
	private void addFilterMethod(FilterMetaInfo filterMetaInfo) {
		boolean isContain = isContainForFilterList(filterMetaInfo.getFilterClassName());
		if(!isContain) {
			filterList.add(filterMetaInfo); 
		}
	}
	
    /**
     * 字节码加载到虚拟机前调用这个方法来修改字节码，达到aop织入：    这个方法被多次调用，一个class元数据对象会调用一次这个方法，如果放回null，那么字节码不会被修改
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
    	CtClass ctClass = buildClazz(className,classfileBuffer);
        try {
			return ctClass.toBytecode();
		} catch (IOException | CannotCompileException e) {
			e.printStackTrace();
			System.err.println("framework:transform：字节码编译失败");
		}
        return null;
    }

	/**
	 * 
	 * 方法用途: 第三方逻辑切入<br>
	 * 操作步骤: TODO<br>
	 * @param className
	 * @param classfileBuffer 
	 * @return
	 */
	public CtClass buildClazz(String className, byte[] classfileBuffer) {
		if (className == null || className.indexOf("/") == -1) {
			return null;
		}
        if(!className.startsWith(Constants.packagePrefix.replace(".", "/"))){
        	return null;
        }
        className = className.replaceAll("/", ".");
        CtClass ctClass = embed(className,classfileBuffer);
		return ctClass;
	}

	/**
	 * 方法用途: 过滤掉不需要aop注入的实例对象<br>
	 * 操作步骤: TODO<br>
	 * @param ctClass
	 */
	private boolean filterBean(CtClass ctClass) {
		try {
			Object[] obj = ctClass.getAnnotations();
			for (Object object : obj) {
				Annotation anno = (Annotation) object;
				String annoName = anno.annotationType().getName();
				if(annoName.equals(Constants.packagePrefix+".simplify.ioc.annotation.Bean")) {
					return true;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 方法用途: 待嵌入的类<br>
	 * 操作步骤: TODO<br>
	 * @param className
	 * @param classfileBuffer Class的字节数组
	 * @return
	 */
	private CtClass embed(String className, byte[] classfileBuffer) {
		//2.获取CtClass之前，要确保调用pool.insertClassPath来设置需要获取类的classpath
		InputStream inputStream = new ByteArrayInputStream(classfileBuffer); 
		CtClass ctClass = null;
		try {
			ctClass = pool.makeClass(inputStream);
		    //ctClass = pool.get(className);//使用javaagent的字节码进行编译，避免了NOTFound的异常，无需再扫描class文件，减少insertClassPath方法的调用
		} catch (IOException | RuntimeException e) {
			e.printStackTrace();
		} 
		if(ctClass == null) {
			return null;
		}
		return embed(className, ctClass);
	}

	/**
	 * 方法用途: 待嵌入的类<br>
	 * 操作步骤: TODO<br>
	 * @param className
	 * @param ctClass Class的ctClass对象
	 * @return
	 */
	public CtClass embed(String className, CtClass ctClass) {
		try {
			
			//3.过滤掉不需要aop注入的实例对象
			boolean isFilterBean = filterBean(ctClass);
			if(!isFilterBean) {
				return null;
			}
			boolean isInvokeEmbedMethod = false;
			//5.是否包含配置文件指定的方法的切入操作
			for(FilterMetaInfo filterMetaInfo : filterList){
	        	if (!filterMetaInfo.getFilterClassName().equals(className)){
	        		continue;
	        	}
        		filterMetaInfo.setIsMatch(true);
        		String[] methodArr = filterMetaInfo.getMethodNameArr();
        		for(String methodName : methodArr){
        			CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);
        			embedMethod(pool, className, ctMethod);
        			isInvokeEmbedMethod = true;
        		}
        	}
			//6.是否包含注解标注的方法的切入操作
			boolean isContainForFilterList = isContainForFilterList(className);//针对类级别的去重，如果需要使用注解识别，那么需要在配置文件中删掉配置的重复信息
			if(isContainForFilterList) {//跳过filterList中存在的方法的数据处理
				printAopEmbedMethodSuccessInfo(className, ctClass, isInvokeEmbedMethod);
				return ctClass;//返回之前成功织入的方法
			}
			CtMethod[] methodArr = ctClass.getMethods();
			for (CtMethod ctMethod : methodArr) {
				boolean isContainsAnno = isContainsAnno(className, pool, ctMethod);
				if(!isContainsAnno){//跳过不包含指定配置文件中注解的方法
					continue;
				}
				embedMethod(pool, className, ctMethod);
				isInvokeEmbedMethod = true;
			}
			printAopEmbedMethodSuccessInfo(className, ctClass, isInvokeEmbedMethod);
		    return ctClass;
		} catch (CannotCompileException e) {
		    e.printStackTrace();
		    System.out.println("framework:buildClazz");
		} catch (NotFoundException e) {
		    e.printStackTrace();
		    System.out.println("framework:NotFound(找不到相关class文件):1.请检查aop.properties中injectionTargetClassPaths属性是否有设置有误");
		}
		return null;
	}

	private void printAopEmbedMethodSuccessInfo(String className, CtClass ctClass, boolean isInvokeEmbedMethod) {
		if(isInvokeEmbedMethod) {
			System.out.println("AOP: 包含Bean注解的对象["+ctClass.getName()+"]已启用aop功能");
			System.out.println("AOP：字节码操作工具启用精简["+className+"]对象字节码");
			System.out.println("AOP：已对类["+className+"]的相关方法进行逻辑切入");
		}
	}

	/**
	 * 方法用途: 进行注解方法切入之前，需要先过滤掉filterList已经处理过的方法<br>
	 * 操作步骤: TODO<br>
	 * @param methodName
	 * @return
	 */
	private boolean isContainForFilterList(String methodName) {
		boolean isContainForFilterList = CollectionUtil.contains(filterList, methodName,new IEqualCallBack<FilterMetaInfo,String>(){
			@Override
			public boolean equal(FilterMetaInfo o, String w) {
				if(o.getFilterClassName().equals(w)) {
					return true;
				}
				return false;
			}
			});
		return isContainForFilterList;
	}

	/**
	 * 方法用途: 判读是否包含配置文件中指定的注解<br>
	 * 操作步骤: TODO<br>
	 * @param className
	 * @param pool
	 * @param method
	 * @return
	 * @throws ClassNotFoundException
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	private boolean isContainsAnno(String className, ClassPool pool, CtMethod method)
			throws CannotCompileException, NotFoundException {
		Object[] annos = new Object[]{};
		try {
			annos = method.getAnnotations();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		for (Object methodAnnoObj : annos) {
			Annotation methodAnno = (Annotation) methodAnnoObj;
			for (String injectionTargetAnno : injectionTargetAnnotationArr) {
				if(methodAnno.annotationType().getName().equals(injectionTargetAnno)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 方法用途: 方法的嵌入<br>
	 * 操作步骤: TODO<br>
	 * @param pool
	 * @param className
	 * @param ctmethod
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	private void embedMethod(ClassPool pool, String className, CtMethod ctmethod)
			throws CannotCompileException, NotFoundException {
		String methodFullName = className+":"+ctmethod.getName();
    	System.out.println("AOP：对方法["+methodFullName+"]进行逻辑切入");
		ctmethod.addLocalVariable("startTime", CtClass.longType);
		ctmethod.addLocalVariable("endTime", CtClass.longType);
		//ctmethod.addParameter(type); //添加方法参数，并指定参数类型，可以是自定义类型
		ctmethod.addLocalVariable("beforeObject",pool.get("java.lang.Object"));
		//ctmethod.addLocalVariable("beforeObject",ctmethod.getReturnType());
		ctmethod.addLocalVariable("ir",pool.get(Constants.packagePrefix+".simplify.aop.InterceptResult"));
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
		ctmethod.insertAfter(Constants.packagePrefix+".simplify.aop.IInterceptor.initAfter(\""+methodFullName+"\",ir,this,$args);");
		ctmethod.insertAfter("endTime = java.time.Instant.now().getNano();");
		ctmethod.insertAfter("System.out.println(\"方法 ["+methodFullName+"] 调用花费的时间:\" +(endTime - startTime)/10000000 +\"毫秒.\");");
	}

    /**
     * 方法用途: 在main函数执行前，执行本方法<br>
     * 操作步骤: 添加新的字节码转换器，来修改字节码<br>
     * @param agentArgs
     * @param ins
     */
    public static void premain(String agentArgs, Instrumentation ins) {
        ins.addTransformer(new AopClassFileTransformer());
//        printAopMappingInfo();
    }
    
    /**
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
        			messageFilter += methodName+",";
				}
        	}
        }
        if(isNoMatch) {
        	System.out.println(messageFilter);
        }
    }
}
