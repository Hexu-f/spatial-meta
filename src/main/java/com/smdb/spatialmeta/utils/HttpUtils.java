package com.smdb.spatialmeta.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smdb.spatialmeta.dto.ApiResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Slf4j
public class HttpUtils {

    private HttpUtils() {
    }

    /**
     * 将URL的参数和body参数合并
     *
     * @param request
     * @author show
     * @date 14:24 2019/5/29
     */
    public static SortedMap<String, Object> getAllParams(HttpServletRequest request) throws IOException {

        SortedMap<String, Object> result = new TreeMap<>();
        //获取URL上的参数
        Map<String, String> urlParams = getUrlParams(request);
        for (Map.Entry entry : urlParams.entrySet()) {
            result.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        Map<String, Object> allRequestParam = new HashMap<>(16);
        // get请求不需要拿body参数
        if (!HttpMethod.GET.name().equals(request.getMethod())) {
            allRequestParam = getAllRequestParam(request);
        }
        //将URL的参数和body参数进行合并
        if (allRequestParam != null) {
            for (Map.Entry entry : allRequestParam.entrySet()) {
                result.put(String.valueOf(entry.getKey()), entry.getValue());
            }
        }
        return result;
    }

    /**
     * 获取 Body 参数
     *
     * @param request
     * @author show
     * @date 15:04 2019/5/30
     */
    public static Map<String, Object> getAllRequestParam(final HttpServletRequest request) throws IOException {


        if (request.getContentType() != null && request.getContentType().toLowerCase().contains("json")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String str = "";
            StringBuilder wholeStr = new StringBuilder();
            //一行一行的读取body体里面的内容；
            while ((str = reader.readLine()) != null) {
                wholeStr.append(str);
            }
            //转化成json对象
            return JSONObject.parseObject(wholeStr.toString(), Map.class);
        }

        Map<String, Object> pmap = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> {
            pmap.put(k, v[0]);
        });


        return pmap;
    }

    /**
     * 将URL请求参数转换成Map
     *
     * @param request
     * @author show
     */
    public static Map<String, String> getUrlParams(HttpServletRequest request) {

        Map<String, String> result = new HashMap<>(16);
        String param = "";
        try {
            if (StringUtils.isBlank(request.getQueryString())) {
                return result;
            }
            param = URLDecoder.decode(request.getQueryString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("url转码异常", e);
//            throw new KaException(ResCodeEnum.IO_ERROR.getCode(),"url转码异常");
        }
        String[] params = param.split("&");
        for (String s : params) {
            int index = s.indexOf("=");
            result.put(s.substring(0, index), s.substring(index + 1));
        }
        return result;
    }

    /**
     * response返回消息
     *
     * @param response
     * @throws IOException
     */
    public static void setResponsemessage(HttpServletResponse response, ApiResultDTO dto) throws IOException {
        //校验失败返回前端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        log.info(JSON.toJSONString(dto));
        out.append(JSON.toJSONString(dto));
    }

    /**
     * response返回消息
     *
     * @param response
     * @throws IOException
     */
    public static void setResponsemessageOp(HttpServletResponse response, ApiResultDTO dto) throws IOException {
        //校验失败返回前端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.append(JSON.toJSONString(dto));
    }

}
