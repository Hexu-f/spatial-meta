package com.smdb.spatialmeta.utils;


import com.smdb.spatialmeta.dto.ApiResultDTO;

public class ParamErrorInfoException extends Exception {
    private String code = null;

    public ParamErrorInfoException(String message) {
        super(message);
    }

    public ParamErrorInfoException(String code, String message) {
        super(message);
        this.code = code;
    }

    private ApiResultDTO apiResult;

    public ApiResultDTO getErrorInfo() {
        return apiResult;
    }

    public ParamErrorInfoException(ApiResultDTO apiResult) {
        this.apiResult = apiResult;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
