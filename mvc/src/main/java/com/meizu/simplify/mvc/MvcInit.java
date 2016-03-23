package com.meizu.simplify.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.exception.CacheException;
import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.exception.StartupErrorException;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.BeanContainer;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.mvc.annotation.RequestParam;
import com.meizu.simplify.mvc.dto.ControllerAnnotationInfo;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.ObjectUtil;
import com.meizu.simplify.webcache.web.CacheBase;


/**
 * 
 * <p><b>Title:</b><i> Mvc框架初始化</i></p>
 * <p>Desc: </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 上午11:47:59</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 上午11:47:59</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class MvcInit {
	
	

}
