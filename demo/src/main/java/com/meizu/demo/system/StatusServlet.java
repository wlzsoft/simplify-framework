package com.meizu.demo.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// @MultipartConfig
@WebServlet(urlPatterns = "/statusServlet", asyncSupported = true, initParams = {
		@WebInitParam(name = "type", value = "ALL") })
public class StatusServlet extends HttpServlet {
	private static final long serialVersionUID = -3818664573588631645L;
	ScheduledThreadPoolExecutor executor = null;

	@Override
	public final void init(final ServletConfig _config) throws ServletException {
		executor = new ScheduledThreadPoolExecutor(10);// 初始线程池10个线程处理请求
	}

	@Override
	public void destroy() {
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String type = getInitParameter("type");
			// 在新开的子线程中执行具体逻辑处理并输出响应，主线程继续执行直到结束
			AsyncContext ctx = request.startAsync();
			ctx.addListener(new AsyncListener() {
				public void onComplete(AsyncEvent asyncEvent) throws IOException {
					System.out.println(type);
				}

				@Override
				public void onTimeout(AsyncEvent event) throws IOException {
					// TODO Auto-generated method stub

				}

				@Override
				public void onError(AsyncEvent event) throws IOException {
					// TODO Auto-generated method stub

				}

				@Override
				public void onStartAsync(AsyncEvent event) throws IOException {
					// TODO Auto-generated method stub

				}
			});
			executor.execute(new AsyncWebService(ctx));// 异步处理
			response.setContentType("text/html;charset=UTF-8");
			out.println("开始收集信息统计：" + new Date() + ".");
			out.flush();
			new Thread(new AsyncWebService(ctx)).start();
			out.println("结束收集信息统计：" + new Date() + ".");
			out.flush();
		} finally {
			out.close();
		}
	}
	class AsyncWebService implements Runnable {
		AsyncContext ctx;
		public AsyncWebService(AsyncContext ctx) {
			this.ctx = ctx;
		}
		public void run() {
			try {
				TimeUnit.SECONDS.sleep(10);
				PrintWriter out = ctx.getResponse().getWriter();
				out.println("统计数据收集完毕：" + new Date() + ".");
				out.flush();
				ctx.complete();
			} catch (Exception e) {
				e.printStackTrace();
			}

			ctx.dispatch("/status.jsp");
		}
	}

}

