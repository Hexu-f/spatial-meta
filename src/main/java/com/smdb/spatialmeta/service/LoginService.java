package com.smdb.spatialmeta.service;

import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;

public interface LoginService {

    void sendRegisterMail(String mail);

    void register(String mail, String code, String password);

    String login(String mail, String pass);


}
