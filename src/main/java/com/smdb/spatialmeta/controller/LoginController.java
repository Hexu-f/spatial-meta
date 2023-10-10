package com.smdb.spatialmeta.controller;

import com.alibaba.fastjson.JSON;
import com.smdb.spatialmeta.dto.ApiResultDTO;
import com.smdb.spatialmeta.mapper.UserMapper;
import com.smdb.spatialmeta.model.User;
import com.smdb.spatialmeta.service.LoginService;
import com.smdb.spatialmeta.utils.RedisHelper;
import com.smdb.spatialmeta.utils.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;


@Api(tags = "登录注册相关api")
@RestController
@Slf4j
public class LoginController {


    @Autowired
    private LoginService loginService;


    //fixme 这里协议content-type和前端商量，我为了方便调试post都是x-www-form-urlencoded
    @ApiOperation(
            value = "v1 发送注册验证码"
    )
    @PostMapping(value = "/api/v1/login/send/mail", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ApiResultDTO sendMail(@ApiParam(value = "邮箱地址", required = true)
                                 @RequestParam(value = "mail")
                                 String mall) {

        try {
            loginService.sendRegisterMail(mall);
            return ApiResultDTO.success();
        } catch (ServiceException se) {
            log.error("逻辑异常", se.getMessage());
            return new ApiResultDTO<>(se.getCode(), se.getMessage());
        } catch (Exception e) {
            log.error("程序异常", e);
            return ApiResultDTO.failure("未知错误");
        }

    }

    @ApiOperation(
            value = "v1 注册"
    )
    @PostMapping(value = "/api/v1/login/register/mail", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ApiResultDTO registerMail(@ApiParam(value = "邮箱地址", required = true)
                                     @RequestParam(value = "mail")
                                     String mail,
                                     @ApiParam(value = "验证码", required = true)
                                     @RequestParam(value = "code")
                                     String code,
                                     @ApiParam(value = "密码", required = true)
                                     @RequestParam(value = "password")
                                     String password) {

        try {
            loginService.register(mail, code, password);
            return ApiResultDTO.success();
        } catch (ServiceException se) {
            log.error("逻辑异常", se.getMessage());
            return new ApiResultDTO<>(se.getCode(), se.getMessage());
        } catch (Exception e) {
            log.error("程序异常", e);
            return ApiResultDTO.failure("未知错误");
        }

    }

    @ApiOperation(
            value = "v1 登录"
    )
    @PostMapping(value = "/api/v1/login/mail", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ApiResultDTO sendMall(@ApiParam(value = "邮箱地址", required = true)
                                 @RequestParam(value = "mail")
                                 String mail,
                                 @ApiParam(value = "密码", required = true)
                                 @RequestParam(value = "password")
                                 String password) {

        try {
            String token = loginService.login(mail, password);
            return ApiResultDTO.success(token);
        } catch (ServiceException se) {
            log.error("逻辑异常", se.getMessage());
            return new ApiResultDTO<>(se.getCode(), se.getMessage());
        } catch (Exception e) {
            log.error("程序异常", e);
            return ApiResultDTO.failure("未知错误");
        }

    }


    @ApiOperation(
            value = "v1 业务接口，登录权限测试"
    )
    @GetMapping(value = "/api/v1/login/testlogin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResultDTO testlogin() {
        //已登录返回成功，未登录返回需要登录
        return ApiResultDTO.success();
    }

}
