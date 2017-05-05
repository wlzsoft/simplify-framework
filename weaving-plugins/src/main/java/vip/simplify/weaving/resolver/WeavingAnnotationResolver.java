package vip.simplify.weaving.resolver;

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

import vip.simplify.Constants;
import vip.simplify.classload.SimplifyClassLoaderExecuter;
import vip.simplify.ioc.annotation.Init;
import vip.simplify.ioc.enums.InitTypeEnum;
import vip.simplify.ioc.resolver.BeanAnnotationResolver;
import vip.simplify.ioc.resolver.IAnnotationResolver;
import vip.simplify.utils.ClassUtil;
import vip.simplify.utils.ClassUtil.ICallbackClass;
import vip.simplify.utils.CollectionUtil;
import vip.simplify.utils.collection.IEqualCallBack;
import vip.simplify.utils.enums.EncodingEnum;
import vip.simplify.utils.enums.SpecialCharacterEnum;
import vip.simplify.weaving.AopClassFileTransformer;

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
public class WeavingAnnotationResolver implements IAnnotationResolver<Class<?>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeavingAnnotationResolver.class);
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		AopClassFileTransformer aft = new AopClassFileTransformer();
		String packageName = Constants.packagePrefix;
		String packagePath = packageName.replace(".", SpecialCharacterEnum.BACKSLASH.toString());
		Enumeration<URL> packageUrls = null;
		try {
			packageUrls = Thread.currentThread().getContextClassLoader().getResources(packagePath);
		} catch (IOException e) {
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
					e.printStackTrace();
				}
				List<Class<?>> clazzList = ClassUtil.getClassFromDir(new File(dirPath),new ICallbackClass<Class<?>>() {
					@Override
					public Class<?> call(File file,Object... params) {
//						System.out.println("classFileName:"+file.getName());
						try {
							InputStream fis = new FileInputStream(file);
							String className = "";
							try {
								CtClass ctClass = aft.pool.makeClass(fis);
								className = ctClass.getName();
								CtClass targetClassByteCode = aft.embed(className, ctClass);
								if(targetClassByteCode!=null/*&&className.equals("vip.simplify.demo.mvc.service.TestService")*/) {
									System.out.println("classFileName-aop:"+file.getName());
//									Class<?> clazz = SimplifyClassLoaderExecuter.getByteCodeClassLoader().defineClass(targetClassByteCode.toBytecode());
									Class<?> clazz = SimplifyClassLoaderExecuter.getByteCodeClassLoader().load(className,false,targetClassByteCode.toBytecode());
									return clazz;
								}
							} catch (IOException e) {
								e.printStackTrace();
								LOGGER.error("无效的Class格式异常:待织入的类"+className+"字节码的格式错误");
							} catch (Exception e) {
								e.printStackTrace();
							}
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						return null;
					}
				});
				if(CollectionUtil.isNotEmpty(clazzList)) {
					System.out.println("weaving-list:"+clazzList);
					String packageNamesStr = CollectionUtil.listToStringBySplit(BeanAnnotationResolver.getClasspaths(), "", "",",");
					List<Class<?>> classAllList = ClassUtil.getClassListMap().get(packageNamesStr);
					CollectionUtil.replace(classAllList, clazzList, new IEqualCallBack<Class<?>, Class<?>>() {
						@Override
						public boolean equal(Class<?> clazz, Class<?> weavingClazz) {
							if(weavingClazz.getName().equals(clazz.getName())) {
								return true;
							}
							return false;
						}
					});
				}
			}
		}
		
		AopClassFileTransformer.printAopMappingInfo();
		LOGGER.info("已启用织入功能");
	}
}
