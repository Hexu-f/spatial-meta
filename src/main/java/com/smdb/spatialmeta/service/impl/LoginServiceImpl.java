package com.smdb.spatialmeta.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smdb.spatialmeta.enums.ErrorCodeEnum;
import com.smdb.spatialmeta.enums.RedisCacheKey;
import com.smdb.spatialmeta.mapper.UserMapper;
import com.smdb.spatialmeta.model.User;
import com.smdb.spatialmeta.service.LoginService;
import com.smdb.spatialmeta.utils.RedisHelper;
import com.smdb.spatialmeta.utils.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.Executor;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisHelper redisHelper;

    @Autowired
    private MailAccount mailAccount;

    @Resource
    private UserMapper userMapper;

    @Resource
    @Qualifier("asyncExecutor")
    private Executor executor;


    //发送注册验证码
    @Override
    public void sendRegisterMail(String mail) {

        //验证邮箱地址
        boolean isEmail = Validator.isEmail(mail);
        if (!isEmail) {
            throw new ServiceException(ErrorCodeEnum.email_err.getCode(), ErrorCodeEnum.email_err.getDesc());
        }

        //验证发送次数
        countLock(mail);
        //验证是否用户已存在
        haveUser(mail);


        //生成随机验证码
        String code = RandomUtil.randomNumbers(6);
        //记录验证码，过期时间15分钟
        String key = RedisCacheKey.mail_captcha + mail;
        redisHelper.setEx(key, code, 15 * 60);


        //fixme 内容标题自己替换
        String title = "邮件标题,验证码为" + code;
        String content = "邮件内容,验证码为" + code;

        executor.execute(() -> {
            //使用线程池发送邮件，其他耗时操作都这么干
            try {
                MailUtil.send(mailAccount, mail, title, content, false);
            } catch (Exception e) {
                log.error("邮件发送异常", e);
            }

        });

    }

    //用验证码注册
    @Override
    public void register(String mail, String code, String password) {
        //fixme 验证密码安全性


        //验证 验证码
        String key = RedisCacheKey.mail_captcha + mail;
        String redisCode = redisHelper.getString(key);

        if (StrUtil.isBlank(redisCode) || !redisCode.equals(code)) {
            throw new ServiceException(ErrorCodeEnum.email_code_err.getCode(), ErrorCodeEnum.email_code_err.getDesc());
        }

        //删除使用过的验证码
        redisHelper.del(key);

        //fixme 这里根据需要自己补充
        User user = new User();
        user.setEmail(mail);
        user.setPassword(password);

        userMapper.insert(user);
    }

    //邮箱密码登录
    @Override
    public String login(String mail, String pass) {
        User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getEmail, mail).eq(User::getPassword, pass).last("limit 1"));
        if (ObjectUtil.isEmpty(user)) {
            throw new ServiceException(ErrorCodeEnum.mail_or_pass_err.getCode(), ErrorCodeEnum.mail_or_pass_err.getDesc());
        }


        //生成token，1小时过期
        String token = RandomUtil.randomString(32);
        String key = RedisCacheKey.user_token + token;
        redisHelper.setEx(key, JSON.toJSONString(user), 60 * 60);

        //fixme 需要删除之前token，或者顶号等操作，自己实现

        return token;
    }

    private void haveUser(String mall) {
        User oldUser = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getEmail, mall).last("limit 1"));
        if (oldUser != null) {
            throw new ServiceException(ErrorCodeEnum.have_user.getCode(), ErrorCodeEnum.have_user.getDesc());
        }
    }


    private void countLock(String mail) {
        //fixme 用redis验证邮箱时间单位内发送次数
        if (false) {
            //如果次数过多
            throw new ServiceException(ErrorCodeEnum.email_to_many.getCode(), ErrorCodeEnum.email_to_many.getDesc());
        }
    }
}
