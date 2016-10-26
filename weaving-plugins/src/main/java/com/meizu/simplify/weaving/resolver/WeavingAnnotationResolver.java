package com.meizu.simplify.weaving.resolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.Constants;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.ClassUtil.ICallbackClass;
import com.meizu.simplify.utils.enums.EncodingEnum;
import com.meizu.simplify.utils.enums.SpecialCharacterEnum;
import com.meizu.simplify.weaving.AopClassFileTransformer;

import javassist.CtClass;

/**
  * <p><b>Title:</b><i>织入解析器</i></p>
 * <p>Desc: 用于替代javaagent的Instrumentation的方式</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 上午11:08:36</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 上午11:08:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Init(InitTypeEnum.WEAVING)
public class WeavingAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(WeavingAnnotationResolver.class);
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		AopClassFileTransformer aft = new AopClassFileTransformer();
		/*List<Class<?>> classList = ClassUtil.findClassesByAnnotationClass(Bean.class,Constants.packagePrefix);
		for (Class<?> clazz : classList) {
			String className = clazz.getName();
			try {
				CtClass ctClass = aft.pool.makeClass(className);
				CtClass targetClassByteCode = aft.embed(className, ctClass);
				LOGGER.info("test:"+targetClassByteCode);
			} catch (Exception e) {
//			} catch (IllegalClassFormatException e) {
				e.printStackTrace();
				LOGGER.error("无效的Class格式异常:待织入的类"+className+"字节码的格式错误");
			}
		}*/
		
		String packageName = Constants.packagePrefix;
		String packagePath = packageName.replace(".", SpecialCharacterEnum.BACKSLASH.toString());
		Enumeration<URL> packageUrls = null;
		try {
			packageUrls = Thread.currentThread()
					.getContextClassLoader().getResources(packagePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (packageUrls.hasMoreElements()) {
			URL packageUrl = packageUrls.nextElement();
			if (packageUrl.getProtocol().equals("jar")) {
				//classNames.addAll(getClassNamesFromJar(packageUrl,packageName));
			} else {
				//classNames.addAll(getClassNamesFromDir(packageUrl,packageName));
				String dirPath = null;
				try {
					dirPath = URLDecoder.decode(packageUrl.getFile(), EncodingEnum.UTF_8.toString());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<byte[]> clazzList = ClassUtil.getClassFromDir(new File(dirPath),new ICallbackClass<byte[]>() {
					@Override
					public byte[] call(File file,Object... params) {
						System.out.println("file_test:"+file.getName());
						try {
							InputStream fis = new FileInputStream(file);
//							byte[] classCode = new byte[fis.available()];
//							fis.read(classCode);
//							fis.close();
							String className = "";
							try {
								CtClass ctClass = aft.pool.makeClass(fis);
								className = ctClass.getName();
								CtClass targetClassByteCode = aft.embed(className, ctClass);
								LOGGER.info("test:"+targetClassByteCode);
								LOGGER.info("test:"+ctClass);
							} catch (Exception e) {
//							} catch (IllegalClassFormatException e) {
								e.printStackTrace();
								LOGGER.error("无效的Class格式异常:待织入的类"+className+"字节码的格式错误");
							}
//							return classCode;
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}
				});
				System.out.println("test_weaving:"+clazzList);
			}
		}
		
		AopClassFileTransformer.printAopMappingInfo();
		LOGGER.info("已启用织入功能");
	}
}
