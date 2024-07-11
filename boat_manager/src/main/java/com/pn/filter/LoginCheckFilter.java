package com.pn.filter;

import com.alibaba.fastjson.JSON;
import com.pn.entity.Result;
import com.pn.utils.WarehouseConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

//自定义的登录过滤器
public class LoginCheckFilter implements Filter {

    private StringRedisTemplate redisTemplate;

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //定义白名单,白名
        List<String> urlList = new ArrayList<>();
        urlList.add("/captcha/captchaImage");
        urlList.add("/login");
        urlList.add("/product/img-upload");
        String url = request.getServletPath();
        if(urlList.contains(url)||url.contains("/img/upload")){
            //过滤器的放行代码
            filterChain.doFilter(request,response);
            return;
        }

        //判断是否有token，并且判断redis中是否有token的键
        String token = request.getHeader(WarehouseConstants.HEADER_TOKEN_NAME);
        if(StringUtils.hasText(token)&&redisTemplate.hasKey(token)){
            //前端携带token并且redis中有对应的token,请求放行
            filterChain.doFilter(request,response);
            return;
        }

        //没有登录（没有携带token）或登陆过期（redis中没有该键）
        Result result = Result.err(Result.CODE_ERR_UNLOGINED, "您尚未登录！");
        //将Result转成json字符串响应给前端
        String strJson = JSON.toJSONString(result);//将result转成json格式的字符串
        //设置相应类型和编码
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();//获得响应对象字符输出流，因为要向前端响应字符串文本
        out.write(strJson);
        out.flush();
        out.close();


    }
}
