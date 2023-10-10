package com.smdb.spatialmeta.utils;

public class ServiceException extends RuntimeException {
    private String code = null;
    private String message = null;
    private Object value = null;

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public ServiceException(String code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public ServiceException(String code, String message, Object value) {
        super(message);
        this.message = message;
        this.code = code;
        this.value = value;
    }

    public ServiceException(String message, Object value) {
        super(message);
        this.message = message;
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
