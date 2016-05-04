package com.meizu.authfilter.interfacefilter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.config.annotation.Config;
import com.meizu.simplify.config.info.Message;
import com.meizu.simplify.encrypt.sign.md5.MD5Encrypt;
import com.meizu.simplify.mvc.controller.BaseController;
import com.meizu.simplify.mvc.model.Model;
import com.meizu.simplify.utils.DateUtil;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.utils.enums.DateFormatEnum;

/**
 * <p><b>Title:</b><i>app接口认证</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月19日 上午11:30:10</p>
 * <p>Modified By:Administrator-</p>
 * <p>Modified Date:2016年4月19日 上午11:30:10</p>
 * @author <a href="mailto:wanglizong@meizu.com" title="邮箱地址">wanglizong</a>
 * @version Version 0.1
 *
 */
public class AppInterfaceAuth <T extends Model> extends BaseController<T> {

	/**
	 * key的默认值：KISEIKSDHIFEK*$*#23
	 */
	@Config
	private String authkey;
	@Config
	private Boolean isAuth;
	@Override
	public boolean checkPermission(HttpServletRequest request, HttpServletResponse response, T t)
			throws ServletException, IOException {
		if(isAuth == null || !isAuth) {
			return true;
		}
		String rosAuth = request.getHeader("ros-auth");
		if(StringUtil.isEmpty(rosAuth)) {
			response.setStatus(403);
			Message.error("没有授权");
		}
		String key = "";
		String reqTime = request.getHeader("reqTime");
		String reqTime2 = request.getHeader("reqTime2");
		if(StringUtil.isEmpty(reqTime)) {
			if(StringUtil.isEmpty(reqTime2)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
				String dateStr = sdf.format(new Date());
				key+=dateStr;
			} else {
				key+=reqTime2;
			}
		} else {
			String dateStr = DateUtil.parseAndFormat(reqTime, DateFormatEnum.YEAR_TO_SECOND, DateFormatEnum.YEAR_TO_MINUTE_N);
			dateStr = MD5Encrypt.sign(dateStr + authkey.substring(5));
			key+=dateStr;
		}
		String modSec = MD5Encrypt.sign(key+authkey);
		
		if(!rosAuth.equalsIgnoreCase(modSec)) {//md5((md5（所有请求参数拼接 +精确到分钟的时间搓（yyyy-MM-dd HH:mm））。substring(0,26))+32位key),如果无任何请求参数，那么使用“dros”值代替 ，key 不能对外泄露。 这个方式用于防止数据篡改。
			                                   //注意参数之间使用‘|’符号拼接，注意时间搓，是long类型的
			response.setStatus(403);
			Message.error("授权失败:"+" 传递的序列-"+rosAuth+",_本地构建序列-"+modSec);
		}
		return true;
	}
	
}
