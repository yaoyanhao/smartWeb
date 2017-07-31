package com.vector.smartWeb;

import com.vector.smartWeb.bean.Data;
import com.vector.smartWeb.bean.Handler;
import com.vector.smartWeb.bean.Param;
import com.vector.smartWeb.bean.View;
import com.vector.smartWeb.helper.BeanHelper;
import com.vector.smartWeb.helper.ConfigHelper;
import com.vector.smartWeb.helper.ControllerHelper;
import com.vector.smartWeb.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vector01.yao on 2017/7/30.
 * 请求转发器
 */
@WebServlet(urlPatterns = "/",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{
    private static final Logger LOGGER= LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求方法和请求路径
        String requestMethod=req.getMethod().toLowerCase();
        String requestPath=req.getRequestURI();

        Handler handler= ControllerHelper.getHandler(requestMethod,requestPath);
        if (handler!=null){
            //获取Controller类及其实例
            Class<?> controllerClass=handler.getControllerClass();
            Object controllerBean= BeanHelper.getBean(controllerClass);

            //创建请求参数对象
            Map<String,Object> paramMap=new HashMap<String,Object>();
            Enumeration<String> parameterNames=req.getParameterNames();
            while (parameterNames.hasMoreElements()){
                String parameterName=parameterNames.nextElement();//参数名
                String parameterValue=req.getParameter(parameterName);//参数值
                paramMap.put(parameterName,parameterValue);

                //请求体参数
                String body= EncodeUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
                if (StringUtil.isNotEmpty(body)){
                    String[] params=body.split("&");
                    if (ArrayUtil.isNotEmpty(params)){
                        for (String param:params){
                            String[] array=param.split("=");
                            if (ArrayUtil.isNotEmpty(array)&&array.length==2){
                                String paramName=array[0];
                                String paramValue=array[1];
                                paramMap.put(paramName,paramValue);
                            }
                        }
                    }
                }
            }

            Param param=new Param(paramMap);

            //调用Action方法
            Method actionMethod=handler.getActionMethod();
            Object result;
            if (param.getParamMap().isEmpty()) {
                result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
            } else {
                result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
            }
            //处理Action方法返回值
            if (result instanceof View){
                //返回JSP页面
                handleViewResult((View) result, req, resp);
            }else if (result instanceof Data){
                handleDataResult((Data) result, resp);
            }
        }
    }

    private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = view.getPath();
        if (StringUtil.isNotEmpty(path)) {
            if (path.startsWith("/")) {
                response.sendRedirect(request.getContextPath() + path);
            } else {
                Map<String, Object> model = view.getModel();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
            }
        }
    }

    private void handleDataResult(Data data, HttpServletResponse response) throws IOException {
        Object model = data.getModel();
        if (model != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json = JsonUtil.toJSONString(model);
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化相关的helper类
        HelperLoader.init();

        //获取ServletContext对象（用于注册Servlet）
        ServletContext servletContext=config.getServletContext();

        //注册处理JSP的Servlet
        registerServlet(servletContext);
    }

    private void registerServlet(ServletContext servletContext) {
        //注册处理JSP的servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping("/hello.jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        //注册处理静态资源的默认的servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        //defaultServlet.addMapping("/favicon.ico");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }
}
