package com.smdb.spatialmeta.config;

import cn.hutool.extra.mail.MailAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class EmailConfig {

    @Value("${email.mailHost}")
    private String mailHost;

    @Value("${email.mailPort}")
    private Integer mailPort;

    @Value("${email.mailFrom}")
    private String mailFrom;

    @Value("${email.mailUser}")
    private String mailUser;

    @Value("${email.mailPass}")
    private String mailPass;


    @Bean
    public MailAccount mailAccount() {
        MailAccount account = new MailAccount();
        account.setHost(mailHost);
        account.setPort(mailPort);
        account.setAuth(true);
        account.setFrom(mailFrom);
        account.setUser(mailUser);
        account.setPass(mailPass);
        account.setSslEnable(true);
        return account;
    }

}
