package com.smdb.spatialmeta.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.beans.Transient;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("Api接口通用返回")
public class ApiResultDTO<T> implements Serializable {
    private static final long serialVersionUID = 7741130044036793402L;
    public static final String OK = "200";
    public static final String ERROR = "1";
    @ApiModelProperty(
            value = "200表示成功，非0表示失败",
            required = true
    )
    private String code;
    @ApiModelProperty("异常消息")
    private String message;
    @ApiModelProperty("业务数据")
    private T value;

    public ApiResultDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ApiResultDTO success(T value) {
        return new ApiResultDTO("200", "成功", value);
    }

    public static <T> ApiResultDTO success() {
        return new ApiResultDTO("200", "成功");
    }

    public static <T> ApiResultDTO failure(String msg) {
        return new ApiResultDTO("1", msg);
    }

    public static <T> ApiResultDTO failure() {
        return new ApiResultDTO("1", "");
    }

    public static <T> ApiResultDTO failure(String code, String msg) {
        return new ApiResultDTO(code, msg);
    }

    public static <T> ApiResultDTO failure(String code, String msg, T value) {
        return new ApiResultDTO(code, msg, value);
    }

    @Transient
    @JsonIgnore
    public boolean isSuccess() {
        return this.code.equalsIgnoreCase("200");
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public T getValue() {
        return this.value;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setValue(T value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return "ApiResultDTO(code=" + this.getCode() + ", message=" + this.getMessage() + ", value=" + this.getValue() + ")";
    }

    public ApiResultDTO(String code, String message, T value) {
        this.code = code;
        this.message = message;
        this.value = value;
    }

    public ApiResultDTO() {
    }
}
