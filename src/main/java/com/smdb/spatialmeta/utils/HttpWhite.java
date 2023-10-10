package com.smdb.spatialmeta.utils;

import java.util.Arrays;
import java.util.List;

public class HttpWhite {

    //fixme 签名验证白名单，这里自己扩展
    private static final List<String> httpList = Arrays.asList(
            "/v2/api-docs*",
            "swagger*");

    //fixme token验证白名单，这里自己扩展
    private static final List<String> tokenList = Arrays.asList(
            "swagger*",
            "/api/v1/login/mail",
            "/api/v1/login/register/mail",
            "/v2/api-docs*",
            "/api/v1/login/send/mail");

    public static boolean inHttpList(String url) {
        String finalUrl = url.toLowerCase();

        for (String e : httpList) {
            if (e.contains("*")) {
                String te = e.replace("*", "");
                if (finalUrl.contains(te.toLowerCase())) {
                    return true;
                }
            }
            if (finalUrl.contains(e.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean inTokenList(String url) {
        String finalUrl = url.toLowerCase();

        for (String e : tokenList) {
            if (e.contains("*")) {
                String te = e.replace("*", "");
                if (finalUrl.contains(te.toLowerCase())) {
                    return true;
                }
            }
            if (finalUrl.contains(e.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
