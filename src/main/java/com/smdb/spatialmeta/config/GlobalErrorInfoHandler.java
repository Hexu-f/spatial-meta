package com.smdb.spatialmeta.config;

import cn.hutool.core.util.StrUtil;
import com.smdb.spatialmeta.dto.ApiResultDTO;
import com.smdb.spatialmeta.enums.ErrorCodeEnum;
import com.smdb.spatialmeta.utils.ParamErrorInfoException;
import com.smdb.spatialmeta.utils.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalErrorInfoHandler {

    @ExceptionHandler(value = ParamErrorInfoException.class)
    public ApiResultDTO paramErrorInfoException(HttpServletRequest request, Exception e) {
        ApiResultDTO result;

        ParamErrorInfoException error = (ParamErrorInfoException) e;
        result = ApiResultDTO.failure(error.getCode(), error.getMessage());

        return result;
    }

    @ExceptionHandler(value = ServiceException.class)
    public ApiResultDTO ServiceErrorHandlerOverJson(HttpServletRequest request, Exception e) {
        ApiResultDTO result;

        ServiceException error = (ServiceException) e;
        if (null != error.getCode()) {
            result = ApiResultDTO.failure(error.getCode(), error.getMessage(), error.getValue());
        } else {
            result = ApiResultDTO.failure(ErrorCodeEnum.ex_error.getCode(), error.getMessage(), error.getValue());
        }

        return result;
    }

    @ExceptionHandler(value = Exception.class)
    public ApiResultDTO errorHandlerOverJson(HttpServletRequest request, Exception e) {
        ApiResultDTO result;
        log.debug("其它异常" + e.getClass().getName());
        result = ApiResultDTO.failure(ErrorCodeEnum.ex_error.getCode(), e.getMessage());

        log.error(result.getMessage());

        return result;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResultDTO handleMethodArgumentNotValidException(ConstraintViolationException e) {
        log.error(e.getMessage());
        String message = e.getMessage();
        if (StrUtil.isNotBlank(message) && message.contains(":")) {
            message = StrUtil.subAfter(message, ":", false).trim();
        }
        return ApiResultDTO.failure(ErrorCodeEnum.ex_error.getCode(), message);
    }

    @ExceptionHandler(BindException.class)
    public ApiResultDTO handleBindException(BindException e) {
        log.error(e.getMessage());
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ApiResultDTO.failure(ErrorCodeEnum.ex_error.getCode(), message);
    }

}
