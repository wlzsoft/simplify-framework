package $package;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vip.simplify.dto.Result;
import vip.simplify.dto.ResultFactory;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.mvc.annotation.RequestMap;
import vip.simplify.mvc.controller.BaseController;
import vip.simplify.mvc.model.Model;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2017年5月11日 上午10:36:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2017年5月11日 上午10:36:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class TestController extends BaseController<Model>{
	@RequestMap
    public Result test(HttpServletRequest request, HttpServletResponse response, Model model) {
         return ResultFactory.success("测试成功!");
    }
}
