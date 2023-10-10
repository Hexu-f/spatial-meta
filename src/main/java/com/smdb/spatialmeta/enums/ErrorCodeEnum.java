package com.smdb.spatialmeta.enums;

public enum ErrorCodeEnum {

    //fixme 这里我随意定义一下，你自己定义自己的

    data_null("10001", "数据不存在"),
    email_err("10002", "邮箱地址异常"),
    email_to_many("10003", "发送太频繁"),
    have_user("10004", "用户已存在"),
    email_code_err("10005", "邮箱验证码异常"),
    user_error("30006", "用户异常"),

    mail_or_pass_err("30007", "用户名或密码错误"),
    token_error("90004", "token错误，请重新登录"),


    ex_error("90003", "未知错误"),


    ;
    private String code;
    private String desc;

    ErrorCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        if (code == null || code.equals("")) {
            return null;
        }
        for (ErrorCodeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
