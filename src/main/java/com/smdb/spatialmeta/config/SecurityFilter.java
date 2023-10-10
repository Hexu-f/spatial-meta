package com.smdb.spatialmeta.config;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smdb.spatialmeta.dto.ApiResultDTO;
import com.smdb.spatialmeta.enums.ErrorCodeEnum;
import com.smdb.spatialmeta.enums.RedisCacheKey;
import com.smdb.spatialmeta.model.User;
import com.smdb.spatialmeta.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.MimeHeaders;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@WebFilter(filterName = "first", urlPatterns = "/*")
@Order(1)
@Slf4j
public class SecurityFilter implements Filter {

    @Autowired
    private RedisHelper redisHelper;


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        String url = request.getRequestURI();
        if (url.contains("download")) {
            chain.doFilter(request, response);
        }

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");


        //处理前端http验证请求
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            ApiResultDTO dto = ApiResultDTO.success(null);
            HttpUtils.setResponsemessageOp(response, dto);
            return;
        }


        HttpServletRequest requestWrapper = new RequestWrapper(request);
        ResponseWrapper responseWrapper = new ResponseWrapper(response);

        //fixme 配置请求唯一id 方便和前端联调


        //fixme 利用封装好的HttpUtils获取全部参数(包括URL和body上的) 打印log方便联调,也方便做签名验证


        //fixme 签名验证，和前端制定签名算法，并做安全性验证


        //token 验证
        ApiResultDTO<User> userDto = tokenVerify(requestWrapper);
        if (!userDto.isSuccess()) {
            //报错
            HttpUtils.setResponsemessage(response, userDto);
            return;
        }


        chain.doFilter(requestWrapper, responseWrapper);

        String result = new String(responseWrapper.getResponseData());
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(result.getBytes());
        outputStream.flush();
        outputStream.close();

        //打印返回，方便和前端联调
        log.info(result);
    }

    private ApiResultDTO<User> tokenVerify(HttpServletRequest request) {
        String url = request.getRequestURI();
        if (HttpWhite.inTokenList(url)) {
            return ApiResultDTO.success(null);
        }

        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            return ApiResultDTO.failure(ErrorCodeEnum.token_error.getCode(), ErrorCodeEnum.token_error.getDesc());
        }

        String key = RedisCacheKey.user_token + token;
        String json = redisHelper.getString(key);
        if (StrUtil.isBlank(json)) {
            return ApiResultDTO.failure(ErrorCodeEnum.token_error.getCode(), ErrorCodeEnum.token_error.getDesc());
        }

        User user = JSONObject.parseObject(json, User.class);
        return ApiResultDTO.success(user);

    }

    private void reflectSetHeader(HttpServletRequest request, String key, String value) {
        Class<? extends HttpServletRequest> requestClass = request.getClass();
        try {
            Field requestField = requestClass.getDeclaredField("request");
            requestField.setAccessible(true);
            Object requestObj = requestField.get(request);
            Field coyoteRequestField = requestObj.getClass().getDeclaredField("coyoteRequest");
            coyoteRequestField.setAccessible(true);
            Object coyoteRequestObj = coyoteRequestField.get(requestObj);
            Field headersField = coyoteRequestObj.getClass().getDeclaredField("headers");
            headersField.setAccessible(true);
            MimeHeaders headersObj = (MimeHeaders) headersField.get(coyoteRequestObj);
            headersObj.removeHeader(key);
            headersObj.addValue(key).setString(value);
        } catch (Exception e) {
            log.error("reflect set header {} error {}", key, e);
//            logger.error("reflect set header {} error {}", key, e);
        }
    }

}
